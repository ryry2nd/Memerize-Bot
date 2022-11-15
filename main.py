from selenium import webdriver
from selenium.webdriver.common.by import By
import time, json

#config = open("config.json")

def main():
    driver = webdriver.Chrome('C:/Program Files/Google/Chrome/Application/chromedriver.exe')
    driver.get("https://app.memrise.com/groups/")

    userName = driver.find_element(value="username")
    password = None

    #userName.send_keys()
    

    print("Here")
    time.sleep(100)

if __name__ == '__main__':
    main()