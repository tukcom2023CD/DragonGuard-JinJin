from selenium.webdriver.common.by import By
from bs4 import BeautifulSoup
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import *
from selenium import webdriver
from config.firefox_config import DRIVER, FIREFOX_OPTS
from config.faust import app, result_topic, contribution_topic, git_repos_topic, issue_topic
import requests, selenium

@app.agent(result_topic)
async def search(result):
    async for res in result:

        results = []
        
        name = str(res['name'])
        search_type = str(res['type'])
        page = str(res['page'])
        
        result = requests.get('https://github.com/search?q=' + name + '&type=' + search_type + '&p=' + page)
        result.raise_for_status()
        soup = BeautifulSoup(result.text, "lxml")
        
        tag_list = []
        
        if search_type.upper() == 'REPOSITORIES':
            repo_list = soup.find('ul', attrs={"class" : 'repo-list'})
            tag_list = repo_list.find_all('a', attrs={"class" : "v-align-middle"})
        elif search_type.upper() == 'USERS':
            tag_list = soup.find_all('a', attrs={"class" : 'mr-1'})
            
        for tag in tag_list:
            results.append(tag.text)

        response = {}
        response["result"] = [{"name" : result} for result in results]
        response["search"] = {"name" : name, "type" : search_type, "page" : int(page.replace(',', ''))}
        sink = app.topic('gitrank.to.backend.result', value_type=dict)
        await sink.send(value=response)


@app.agent(contribution_topic)
async def commits(member):
    async for mem in member:
        
        m = str(mem['githubId'])
        year = str(mem['year'])
        
        result = requests.get('https://github.com/' + m + '?tab=overview&from=' + year + '-01-01')
        result.raise_for_status()
        
        soup = BeautifulSoup(result.text, "lxml")
        h2 = soup.find('h2', attrs={"class" : "f4 text-normal mb-2"})
        name = soup.find('span', attrs={"class" : "p-name vcard-fullname d-block overflow-hidden"}).text.strip()
        image = soup.find('img', attrs={ "class" : "avatar avatar-user width-full border color-bg-default"})['src']
        contribution = int(h2.text.strip().split(' ')[0].rstrip().replace(',', ''))
        
        response = { "name" : name, "contribution" : contribution, "githubId" : m, "profileImage" : image}
        
        sink = app.topic('gitrank.to.backend.contribution', value_type=dict)
        await sink.send(value=response)
        

@app.agent(issue_topic)
async def issues(issues):
    async for issue in issues:
        name = str(issue['name'])
        
        result = requests.get('https://github.com/' + name + '/issues')
        result.raise_for_status()
        
        soup = BeautifulSoup(result.text, "lxml")
        issue_result = int(soup.find('a', attrs={"data-ga-click" : "Issues, Table state, Closed"}).text.strip().split(" ")[0].strip().replace(',', ''))
        
        response = { "name" : name, "closedIssue" : issue_result}
        
        sink = app.topic('gitrank.to.backend.issues', value_type=dict)
        await sink.send(value=response)


@app.agent(git_repos_topic)
async def git_repos(git_repos):
    async for repo in git_repos:

        name = str(repo['name'])
        year = str(repo['year'])
        
        DRIVER = webdriver.Firefox(options=FIREFOX_OPTS)
        DRIVER.get('https://github.com/' + name + '/graphs/contributors?from=2023-01-01&to=' + year + '-12-31&type=c')
        
        try:
            WebDriverWait(DRIVER, 15).until(EC.presence_of_all_elements_located((By.CSS_SELECTOR, '#contributors > ol')))
        except selenium.common.exceptions.TimeoutException as e:
            print('Error Occured')
        
        response = {}
        i = 1
        
        while True:
            try:
                commits = DRIVER.find_element(By.CSS_SELECTOR, 
                                                '#contributors > ol > li:nth-child(' + str(i) + ') > span > h3 > span.f6.d-block.color-fg-muted > span > div > a')
                
                member_name = DRIVER.find_element(By.CSS_SELECTOR, 
                                                    '#contributors > ol > li:nth-child(' + str(i) + ') > span > h3 > a.text-normal')
                
                addition = DRIVER.find_element(By.CSS_SELECTOR, 
                                                '#contributors > ol > li:nth-child(' + str(i) + ') > span > h3 > span.f6.d-block.color-fg-muted > span > div > span.color-fg-success.text-normal')
                
                deletion = DRIVER.find_element(By.CSS_SELECTOR, 
                                                '#contributors > ol > li:nth-child(' + str(i) + ') > span > h3 > span.f6.d-block.color-fg-muted > span > div > span.color-fg-danger.text-normal')
                
                response[member_name.get_attribute('innerText')] = { "commits" : int(commits.get_attribute('innerText').split(' ')[0].replace(',', '')), 
                                                                    "addition" : int(addition.get_attribute('innerText').split(' ')[0].replace(',', '')), 
                                                                    "deletion" : int(deletion.get_attribute('innerText').split(' ')[0].replace(',', '')),
                                                                    "gitRepo" : name}
                
            except selenium.common.exceptions.NoSuchElementException as e:
                    break
            finally:
                i += 1
                
        DRIVER.close()
        
        sink = app.topic('gitrank.to.backend.git-repos', value_type=dict)
        await sink.send(value=response)


if __name__ == "__main__":
    app.main()
    DRIVER.get('https://github.com')
    DRIVER.close()
