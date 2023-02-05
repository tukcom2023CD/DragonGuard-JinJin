from selenium import webdriver
from selenium.webdriver.firefox.options import Options
from webdriver_manager.firefox import GeckoDriverManager

FIREFOX_OPTS = Options()
FIREFOX_OPTS.add_argument('--headless')
FIREFOX_OPTS.add_argument('--no-sandbox')
FIREFOX_OPTS.add_argument("--single-process")
FIREFOX_OPTS.add_argument("--disable-dev-shm-usage")
FIREFOX_OPTS.add_argument("--disable-gpu")

DRIVER = webdriver.Firefox(executable_path=GeckoDriverManager().install(), options=FIREFOX_OPTS) 
