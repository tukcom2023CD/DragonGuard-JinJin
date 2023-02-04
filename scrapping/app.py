from flask import Flask, request
from flask_restx import Resource, Api
from flask_cors import CORS
from kafka import KafkaProducer 
from json import dumps
from base_url import KAFKA_BASE_URL
from selenium import webdriver
from selenium.webdriver.common.by import By
from bs4 import BeautifulSoup
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import *
from time import sleep
import requests, selenium

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}}, supports_credentials=True)
app.config['CORS_HEADERS'] = 'Content-Type'
api = Api(app)

ns = api.namespace('/', description='GitRank API')

producer = KafkaProducer(acks=0,
            compression_type='gzip',
            bootstrap_servers=[KAFKA_BASE_URL],
            value_serializer=lambda x: dumps(x).encode('utf-8'),
            retries=1
          )

chrome_options = webdriver.ChromeOptions()
chrome_options.add_argument('--headless')
chrome_options.add_argument('--no-sandbox')
chrome_options.add_argument("--single-process")
chrome_options.add_argument("--disable-dev-shm-usage")
chrome_options.add_argument("disable-gpu")
chrome_driver = webdriver.Chrome('/usr/src/chrome/chromedriver', options=chrome_options)
chrome_driver.get('https://github.com')
chrome_driver.close()

@ns.route('/scrap/search', methods=['GET'])
class Search(Resource):
    
    '''레포명 또는 유저명으로 검색한 페이지를 스크래핑한다'''
    def get(self):
        results = []
        
        name = request.args.get('name')
        search_type = request.args.get('type')
        page = request.args.get('page')
        
        result = requests.get('https://github.com/search?q=' + name + '&type=' + search_type + '&p=' + page)
        result.raise_for_status()
        soup = BeautifulSoup(result.text, "lxml")
        
        if search_type == 'repositories':
            tag_list = self.get_repository(soup)
        elif search_type == 'users':
            tag_list = self.get_user(soup)
            
        for tag in tag_list:
            results.append(tag.text)

        response = {}
        response['result'] = [{'name' : result} for result in results]
        kafka_response = response
        kafka_response['search'] = {'name' : name, 'type' : search_type, 'page' : page}
        producer.send('gitrank.to.backend.result', value=kafka_response)
        
        return (response, 200)
    
    def get_repository(self, soup):
        repo_list = soup.find('ul', attrs={"class" : 'repo-list'})
        return repo_list.find_all('a', attrs={"class" : "v-align-middle"})
        
    def get_user(self, soup):
        user_list = soup.find_all('a', attrs={"class" : 'mr-1'})
        return user_list

@ns.route('/scrap/commits', methods=['GET'])
class MemberCommit(Resource):
    
    '''유저 페이지의 커밋 수를 스크래핑한다'''
    def get(self):
        member = request.args.get('member')
        year = request.args.get('year')
        
        result = requests.get('https://github.com/' + member + '?tab=overview&from=' + year + '-01-01')
        result.raise_for_status()
        
        soup = BeautifulSoup(result.text, "lxml")
        h2 = soup.find('h2', attrs={"class" : "f4 text-normal mb-2"})
        name = soup.find('span', attrs={"class" : "p-name vcard-fullname d-block overflow-hidden"}).text.strip()
        image = soup.find('img', attrs={ "class" : "avatar avatar-user width-full border color-bg-default"})['src']
        commit_num = int(h2.text.strip().split(' ')[0].rstrip())
        
        member = { "name" : name, "commitNum" : commit_num, "githubId" : member, "profileImage" : image}
        
        producer.send('gitrank.to.backend.commit', value=member)

        return (member, 200)
    
@ns.route('/scrap/git-repos', methods=['GET'])
class GitRepos(Resource):
    
    '''유저 페이지의 커밋 수를 스크래핑한다'''
    def get(self):
        name = request.args.get('name')
        year = request.args.get('year')
        
        chrome_driver = webdriver.Chrome(options=chrome_options)
        
        chrome_driver.get('https://github.com/' + name + '/graphs/contributors?from=2022-01-01&to=' + year + '-12-31&type=c')
        
        try:
            WebDriverWait(chrome_driver, 7.5).until(EC.presence_of_all_elements_located((By.CSS_SELECTOR, '#contributors > ol')))
        except selenium.common.exceptions.TimeoutException as e:
            print('Error Occured')
        
        response = {}
        i, cnt = 1, 1
        while True:
            try:
                commits = chrome_driver.find_element(By.CSS_SELECTOR, '#contributors > ol > li:nth-child(' + str(i) + ') > span > h3 > span.f6.d-block.color-fg-muted > span > div > a')
                member_name = chrome_driver.find_element(By.CSS_SELECTOR, '#contributors > ol > li:nth-child(' + str(i) + ') > span > h3 > a.text-normal')
                addition = chrome_driver.find_element(By.CSS_SELECTOR, '#contributors > ol > li:nth-child(' + str(i) + ') > span > h3 > span.f6.d-block.color-fg-muted > span > div > span.color-fg-success.text-normal')
                deletion = chrome_driver.find_element(By.CSS_SELECTOR, '#contributors > ol > li:nth-child(' + str(i) + ') > span > h3 > span.f6.d-block.color-fg-muted > span > div > span.color-fg-danger.text-normal')
                response[member_name.get_attribute('innerText')] = { 'commits' : int(commits.get_attribute('innerText').split(' ')[0]), 
                                                                    'addition' : int(addition.get_attribute('innerText').split(' ')[0].replace(',', '')), 
                                                                    'deletion' : int(deletion.get_attribute('innerText').split(' ')[0].replace(',', '')),
                                                                    'gitRepo' : name}
            except selenium.common.exceptions.NoSuchElementException as e:
                if cnt == 3:
                    break
                cnt += 1
            finally:
                i += 1
        
        chrome_driver.close()
        producer.send('gitrank.to.backend.git-repos', value=response)
        
        return (response, 200) if response else ('No Content', 200)
        
if __name__ == "__main__":
    app.run(port=5000, debug=True)
