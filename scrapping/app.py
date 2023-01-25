from flask import Flask, request
from flask_restx import Resource, Api
from flask_cors import CORS
from bs4 import BeautifulSoup
import requests

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}}, supports_credentials=True)
app.config['CORS_HEADERS'] = 'Content-Type'
api = Api(app)

ns = api.namespace('/', description='GitRank API')

@ns.route('/scrap', methods=['GET'])
class scrapping(Resource):
    
    '''레포명으로 검색한 페이지를 스크래핑한다'''
    def get(self):
        results = []
        mapper = {'users' : 'user_search_results', 'repositories' : 'repo-list'}
        
        repository = request.args.get('name')
        search_type = request.args.get('type')
        page = request.args.get('page')
        
        result = requests.get('https://github.com/search?q=' + repository + '&type=' + search_type + '&p=' + page)
        result.raise_for_status()
        soup = BeautifulSoup(result.text, "lxml")
        repo_list = soup.find('ul', attrs={"class" : mapper[search_type]})
        em_tag_list = repo_list.find_all('a', attrs={"class" : "v-align-middle"})
            
        for em_tag in em_tag_list:
            results.append(em_tag.text)

        response = {}
        response['result'] = [{'name' : result} for result in results]
        
        return (response, 200)
            

if __name__ == "__main__":
    app.run(port=5000, debug=True)
