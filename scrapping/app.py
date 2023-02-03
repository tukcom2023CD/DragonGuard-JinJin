from flask import Flask, request
from flask_restx import Resource, Api
from flask_cors import CORS
from kafka import KafkaProducer 
from json import dumps
from base_url import KAFKA_BASE_URL
from selenium import webdriver
from selenium.webdriver.common.by import By
from bs4 import BeautifulSoup
import requests, selenium

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}}, supports_credentials=True)
app.config['CORS_HEADERS'] = 'Content-Type'
api = Api(app)

ns = api.namespace('/', description='GitRank API')

producer = KafkaProducer(acks=0,
            compression_type='gzip',
            bootstrap_servers=[KAFKA_BASE_URL],
            value_serializer=lambda x: dumps(x).encode('utf-8')
          )

chrome_options = webdriver.ChromeOptions()
chrome_options.add_argument('--headless')
chrome_options.add_argument('--no-sandbox')
chrome_options.add_argument("--single-process")
chrome_options.add_argument("--disable-dev-shm-usage")
chrome_options.add_argument("disable-gpu")
chrome_options.add_argument(f'user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.87 Safari/537.36')
driver = webdriver.Chrome('/usr/src/chrome/chromedriver', options=chrome_options)
driver.get('https://github.com')
driver.close()
driver.quit()

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
        commit_num = int(h2.text.strip().split(' ')[0].rstrip())
        
        producer.send('gitrank.to.backend.commit', value={"commitNum" : commit_num, "githubId" : member})

        return ('success', 200)
    
@ns.route('/scrap/git-repos', methods=['GET'])
class GitRepos(Resource):
    
    '''유저 페이지의 커밋 수를 스크래핑한다'''
    def get(self):
        name = request.args.get('name')
        year = request.args.get('year')
        
        chrome_driver = webdriver.Chrome(options=chrome_options)
        
        chrome_driver.get('https://github.com/' + name + '/graphs/contributors?from=2023-01-01&to=' + year + '-12-31&type=c')
        chrome_driver.implicitly_wait(1)
        
        while not chrome_driver.find_elements(By.CLASS_NAME, 'border-bottom p-2 lh-condensed'):
            try:
                chrome_driver.implicitly_wait(1)
                print('scrapping processing')
            except selenium.common.exceptions.TimeoutException:
                print('scrapping except occured')
        
        elements = chrome_driver.find_elements(By.CLASS_NAME, 'border-bottom p-2 lh-condensed')
        
        response = {}
        
        for e in elements:
            member_name = e.find_element(By.CLASS_NAME, 'text-normal')
            commits = e.find_element(By.CLASS_NAME, 'Link--secondary text-normal')
            response[member_name.get_attribute('innerText')] = int(commits.get_attribute('innerText').split(' ')[0])
        
        chrome_driver.close()
        chrome_driver.quit()
        
        return (response, 200) if not response else ('No Content', 200)
        
if __name__ == "__main__":
    app.run(port=5000, debug=True)
