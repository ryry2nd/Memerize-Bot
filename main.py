#imports
from selenium import webdriver
from selenium.webdriver.common.by import By
from memriseAi import Ai
import json, os

options = webdriver.ChromeOptions()

#get configs
filePath = "config.json"
if not os.path.exists(filePath):
    open(filePath, 'x').close()
    file = open(filePath, 'w')
    file.write(json.dumps({"username": None, "password": None, "StopAfterDoneLearning": True, "hidden": True, "url": None}))
    file.close()

file = open("config.json")
config = json.load(file)
file.close()

USERNAME = config["username"]
PASSWORD = config["password"]
SADL = config["StopAfterDoneLearning"]
HIDDEN = config["hidden"]
URL = config["url"]

if HIDDEN:
    options.add_argument('headless')

#logs in
def log_in(driver: webdriver.Chrome):
    userName = driver.find_element(value="username")
    password = driver.find_element(value="password")
    submit = driver.find_element(By.XPATH, "//button[@data-testid='signinFormSubmit']")

    userName.send_keys(USERNAME)
    password.send_keys(PASSWORD)

    submit.click()

#main method
def main():
    driver = webdriver.Chrome(executable_path="chromedriver.exe", options=options)

    if not URL:
        driver.get("https://app.memrise.com/groups/")
    else:
        driver.get(URL)

    log_in(driver)
    Ai(driver, sadl=SADL)

    driver.quit()

if __name__ == '__main__':
    main()