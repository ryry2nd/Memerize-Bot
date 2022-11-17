#imports
from selenium import webdriver
from selenium.webdriver.common.by import By
from pysondb import PysonDB
from ai import Ai
import time, json, os

#get password
filePath = os.path.join("config.json")
if not os.path.exists(filePath):
    open(filePath, 'x').close()
    file = open(filePath, 'w')
    file.write(json.dumps({"username": None, "password": None, "StopAfterDoneLearning": True}, indent=4, sort_keys=True, ensure_ascii=False))
    file.close()

file = open("config.json")
config = json.load(file)
file.close()

USERNAME = config["username"]
PASSWORD = config["password"]
SADL = config["StopAfterDoneLearning"]

#logs in
def log_in(driver: webdriver.Chrome):
    time.sleep(0.5)
    userName = driver.find_element(value="username")
    password = driver.find_element(value="password")
    submit = driver.find_element(By.XPATH, "//button[@data-testid='signinFormSubmit']")

    userName.send_keys(USERNAME)
    password.send_keys(PASSWORD)

    submit.click()
    time.sleep(5)

#main method
def main():
    driver = webdriver.Chrome('C:/Program Files/Google/Chrome/Application/chromedriver.exe')

    driver.get("https://app.memrise.com/groups/")

    log_in(driver)
    Ai(driver, sadl=SADL)

if __name__ == '__main__':
    main()