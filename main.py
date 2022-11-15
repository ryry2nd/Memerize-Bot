from selenium import webdriver
from selenium.webdriver.common.by import By
import time, json, os

filePath = os.path.join("config.json")

if not os.path.exists(filePath):
    open(filePath, 'x').close()
    file = open(filePath, 'w')
    file.write(json.dumps({"username": None, "password": None}))
    file.close()

file = open("config.json")

config = json.load(file)

file.close()

USERNAME = config["username"]
PASSWORD = config["password"]

def log_in(driver: webdriver.Chrome):
    userName = driver.find_element(value="username")
    password = driver.find_element(value="password")
    submit = driver.find_element(By.XPATH, "//button[@data-testid='signinFormSubmit']")

    userName.send_keys(USERNAME)
    password.send_keys(PASSWORD)

    submit.click()

def main():
    driver = webdriver.Chrome('C:/Program Files/Google/Chrome/Application/chromedriver.exe')

    driver.get("https://app.memrise.com/aprender/learn?course_id=6274442")

    log_in(driver)

    button = driver.find_elements(By.XPATH, "//a[@class='btn btn-small btn-success big']")

    print("Here")
    while True:
        time.sleep(100)

if __name__ == '__main__':
    main()