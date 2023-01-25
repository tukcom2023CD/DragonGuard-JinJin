class Search():
    
    def __init__(self, search_word, search_type, search_page):
        self.search_word = search_word
        self.search_type = search_type
        self.search_page = search_page
        
    def serialize(self):
        return {
            'search_word' : self.search_word,
            'search_type' : self.search_type,
            'search_page' : self.search_page
        }


class Result():
    
    def __init__(self, result):
        self.result = result
        
    def serialize(self):
        return {
            'name' : self.result
        }
