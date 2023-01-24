from flask_sqlalchemy import SQLAlchemy
from flask import Flask
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from dotenv import load_dotenv
import os

app = Flask(__name__)
load_dotenv()

MYSQL_USER = os.environ.get("MYSQL_USER")
MYSQL_PASSWORD = os.environ.get("MYSQL_PASSWORD")
MYSQL_ROOT_PASSWORD = os.environ.get("MYSQL_ROOT_PASSWORD")
MYSQL_USER = os.environ.get("MYSQL_USER")
MYSQL_DATABASE = os.environ.get("MYSQL_DATABASE")
MYSQL_HOST = os.environ.get("MYSQL_HOST")
MYSQL_PORT = os.environ.get("MYSQL_PORT")

sqlurl = 'mysql+pymysql://' + MYSQL_USER + ':' + MYSQL_PASSWORD + '@' + MYSQL_HOST + '/' + MYSQL_DATABASE
    
Base = declarative_base()
engine = create_engine(sqlurl)

app.config['SQLALCHEMY_DATABASE_URI'] = sqlurl
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)
Base.metadata.reflect(engine)


class SearchResult(Base):
    __tablename__ = 'search_result'
    __table_args__ = {'extend_existing': True,
                      'mysql_collate': 'utf8_general_ci'}
    id = db.Column(db.Integer, primary_key=True)
    search_id = db.Column(db.Integer, db.ForeignKey('search.id'))
    result_id = db.Column(db.Integer, db.ForeignKey('result.id'))
    

class Search(Base):
    __tablename__ = 'search'
    __table_args__ = {'extend_existing': True,
                      'mysql_collate': 'utf8_general_ci'}
    id = db.Column(db.Integer, primary_key=True)
    search_word = db.Column(db.String(255))
    search_type = db.Column(db.String(255))
    search_page = db.Column(db.Integer)
    results = db.relationship("Result", secondary=SearchResult.__table__, backref='Search')
    
    def __init__(self, search_word, search_type, search_page):
        self.search_word = search_word
        self.search_type = search_type
        self.search_page = search_page
        
    def serialize(self):
        return {
            'id' : self.id,
            'search_word' : self.search_word,
            'search_type' : self.search_type,
            'search_page' : self.search_page
        }


class Result(Base):
    __tablename__ = 'result'
    __table_args__ = {'extend_existing': True,
                      'mysql_collate': 'utf8_general_ci'}
    id = db.Column(db.Integer, primary_key=True)
    result = db.Column(db.String(255))
    
    def __init__(self, result):
        self.result = result
        
    def serialize(self):
        return {
            'id' : self.id,
            'name' : self.result
        }
        
        
Base.metadata.create_all(engine) # 여기는 추후 삭제 필요
