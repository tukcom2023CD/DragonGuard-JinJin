from flask import Flask, request
from flask_restx import Resource, Api
from flask_cors import CORS
from bs4 import BeautifulSoup
import requests
from kafka import KafkaProducer 
from json import dumps
from base_url import KAFKA_BASE_URL

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
        profile_imgs = soup.find_all('img', attrs={'class' : 'avatar avatar-user width-full border color-bg-default'})
        for img in profile_imgs:
            profile_img = img['src']
        
        name = soup.find('span', attrs={"itemprop" : "name"}).text.strip()
        
        commit_num = int(h2.text.strip().split(' ')[0].rstrip())
        
        producer.send('gitrank.to.backend.commit', value={"commitNum" : commit_num, "name" : name, "githubId" : member, "profileImage" : profile_img})

        return ('success', 200)
        
if __name__ == "__main__":
    app.run(port=5000, debug=True)
