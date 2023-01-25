from flask import Flask, request
from flask_restx import Resource, Api
from flask_cors import CORS
from bs4 import BeautifulSoup
from models import Search, Result
import requests

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}}, supports_credentials=True)
app.config['CORS_HEADERS'] = 'Content-Type'
api = Api(app)

ns = api.namespace('/', description='GitRank API')

@ns.route('/api/search/git-repos', methods=['GET'])
class scrapping(Resource):
    
    '''레포명으로 검색한 페이지를 스크래핑한다'''
    def get(self):
        results = []
        
        repository = request.args.get('name')
        page = int(request.args.get('page'))
        
        s = Search(search_word=repository, search_type='repository', search_page=page)
        
        result = requests.get('https://github.com/search?q=' + repository + '&type=Repositories&p=' + str(page))
        result.raise_for_status()
        soup = BeautifulSoup(result.text, "lxml")
        repo_list = soup.find('ul', attrs={"class" : "repo-list"})
        em_tag_list = repo_list.find_all('a', attrs={"class" : "v-align-middle"})
            
        for em_tag in em_tag_list:
            repository_name = em_tag.text
            
            r = Result(result=repository_name)
            results.append(r)

        response = {}
        response['search'] = s.serialize()
        response['result'] = [result.serialize() for result in results]
        
        return ((response), 200)


if __name__ == "__main__":
    app.run(port=5000, debug=True, host='0.0.0.0')
