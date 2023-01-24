from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import create_engine
from flask import Flask, request
from dotenv import load_dotenv
from flask_restx import Resource, Api
from flask_cors import CORS
from bs4 import BeautifulSoup
from models import Search, Result
import os, requests

app = Flask(__name__)
load_dotenv()
CORS(app, resources={r"/*": {"origins": "*"}}, supports_credentials=True)
app.config['CORS_HEADERS'] = 'Content-Type'
api = Api(app)

MYSQL_USER = os.environ.get("MYSQL_USER")
MYSQL_PASSWORD = os.environ.get("MYSQL_PASSWORD")
MYSQL_ROOT_PASSWORD = os.environ.get("MYSQL_ROOT_PASSWORD")
MYSQL_USER = os.environ.get("MYSQL_USER")
MYSQL_DATABASE = os.environ.get("MYSQL_DATABASE")
MYSQL_HOST = os.environ.get("MYSQL_HOST")
MYSQL_PORT = os.environ.get("MYSQL_PORT")

sqlurl = 'mysql+pymysql://' + MYSQL_USER + ':' + MYSQL_PASSWORD + '@' + MYSQL_HOST + ':' + MYSQL_PORT + '/' + MYSQL_DATABASE
engine = create_engine(sqlurl)

app.config['MYSQL_DB'] = MYSQL_DATABASE
app.config['MYSQL_USER'] = MYSQL_USER
app.config['MYSQL_PASSWORD'] = MYSQL_PASSWORD
app.config['MYSQL_HOST'] = MYSQL_HOST
app.config['SQLALCHEMY_DATABASE_URI'] = sqlurl
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)
ns = api.namespace('/', description='GitRank API')

@ns.route('/api/search/git-repos', methods=['POST'])
class scrapping(Resource):
    
    '''레포명으로 검색한 페이지를 스크래핑한다'''
    def post(self):
        results = []
        
        req = request.get_json()
        repository = req['git-repo']
        page = int(request.args.get('page'))
        
        s = Search(search_word=repository, search_type='repository', search_page=page)
        
        for i in range(page, page + 2):
            result = requests.get('https://github.com/search?q=' + repository + '&type=Repositories&p=' + str(i))
            result.raise_for_status()
            soup = BeautifulSoup(result.text, "lxml")
            repo_list = soup.find('ul', attrs={"class" : "repo-list"})
            em_tag_list = repo_list.find_all('a', attrs={"class" : "v-align-middle"})
            
            for em_tag in em_tag_list:
                repository_name = em_tag.text
                
                r = Result(result=repository_name)
                results.append(r)
                
                if r not in s.results:
                    s.results.append(r)

                db.session.add(s)
                
        db.session.commit()
        db.session.flush()
        
        response = {}
        response['search'] = s.serialize()
        response['result'] = [result.serialize() for result in results]
        
        return ((response), 200)


if __name__ == "__main__":
    app.run(port=5000, debug=True, host='0.0.0.0')
