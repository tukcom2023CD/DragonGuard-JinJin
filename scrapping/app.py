from flask import Flask, request
from flask_restx import Resource, Api
from flask_cors import CORS
from kafka import KafkaProducer 
from json import dumps
from base_url import KAFKA_BASE_URL, CHROME_BASE_URL
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities

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

options = Options()
options.headless = True
options.add_argument("--window-size=1920,1200")
driver = webdriver.Remote(command_executor=CHROME_BASE_URL, options=options, desired_capabilities=DesiredCapabilities.CHROME)


@ns.route('/scrap/search', methods=['GET'])
class Search(Resource):
    
    '''레포명 또는 유저명으로 검색한 페이지를 스크래핑한다'''
    def get(self):
        results = []
        
        name = request.args.get('name')
        search_type = request.args.get('type')
        page = request.args.get('page')
        
        driver.get('https://github.com/search?q=' + name + '&type=' + search_type + '&p=' + page)
        
        if search_type == 'repositories':
            tag_list = self.get_repository()
        elif search_type == 'users':
            tag_list = self.get_user()
            
        for tag in tag_list:
            results.append(tag.text)

        response = {}
        response['result'] = [{'name' : result} for result in results]
        kafka_response = response
        kafka_response['search'] = {'name' : name, 'type' : search_type, 'page' : page}
        producer.send('gitrank.to.backend.result', value=kafka_response)
        
        return (response, 200)
    
    def get_repository(self):
        repo_list = driver.find_element(By.CLASS_NAME, 'repo-list')
        return repo_list.find_elements(By.CLASS_NAME, "v-align-middle")
        
    def get_user(self):
        return driver.find_elements(By.CLASS_NAME, 'mr-1')

@ns.route('/scrap/commits', methods=['GET'])
class MemberCommit(Resource):
    
    '''유저 페이지의 커밋 수를 스크래핑한다'''
    def get(self):
        member = request.args.get('member')
        year = request.args.get('year')
        
        driver.get('https://github.com/' + member + '?tab=overview&from=' + year + '-01-01')
        
        h2 = driver.find_element(By.CLASS_NAME, 'f4 text-normal mb-2')
        
        profile_img = driver.find_element(By.CLASS_NAME, 'avatar avatar-user width-full border color-bg-default')
        profile_img = profile_img['src']
        
        name = driver.find(By.CLASS_NAME, 'p-name vcard-fullname d-block overflow-hidden').text
        
        commit_num = int(h2.text.strip().split(' ')[0].rstrip())
        
        producer.send('gitrank.to.backend.commit', value={"commitNum" : commit_num, "name" : name, "githubId" : member, "profileImage" : profile_img})

        return ('success', 200)
        
if __name__ == "__main__":
    app.run(port=5000, debug=True)
