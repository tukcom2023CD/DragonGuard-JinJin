from flask_restx import  reqparse

req1 = reqparse.RequestParser()
req1.add_argument('name', type=str, default=None, help='검색어')
req1.add_argument('type', type=str, default=None, help='users or repositories')
req1.add_argument('page', type=int, default=None, help='페이지')

req2 = reqparse.RequestParser()
req2.add_argument('member', type=str, default=None, help='github id')
req2.add_argument('year', type=str, default=None, help='현재년도')

req3 = reqparse.RequestParser()
req3.add_argument('name', type=str, default=None, help='github id')
req3.add_argument('year', type=str, default=None, help='현재년도')