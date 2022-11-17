#imports
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.common.exceptions import *
from selenium.webdriver.common.keys import Keys
from pysondb import PysonDB
import time, json, os, collections

#get password
filePath = os.path.join("config.json")
if not os.path.exists(filePath):
    open(filePath, 'x').close()
    file = open(filePath, 'w')
    file.write(json.dumps({"username": None, "password": None}, indent=4, sort_keys=True, ensure_ascii=False))
    file.close()

file = open("config.json")
config = json.load(file)
file.close()

USERNAME = config["username"]
PASSWORD = config["password"]

#load db
data = PysonDB("data.json")

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

#tests if question is not in the db
def testIfNotExists(question):
    for id, d in data.get_all().items():
        if d["question"] == question:
            return False
    return True

#finds the answer
def findAns(question):
    for id, d in data.get_all().items():
        if d["question"] == question:
            return d["ans"]
        elif d["ans"] == question:
            return d["question"]

#the ai
def ai(driver: webdriver.Chrome):
    while True:
        time.sleep(0.1)
        
        if driver.find_elements(By.XPATH, "//button[@class='sc-1dxc4vq-2 fjYiwU']"):
            driver.find_element(By.XPATH, "//button[@class='sc-1dxc4vq-2 fjYiwU']").click()

        elif driver.find_elements(By.XPATH, "//a[@aria-label='Classic review']"):
            driver.find_element(By.XPATH, "//html").send_keys(Keys.ENTER)

        elif driver.find_elements(By.XPATH, "//a[@aria-label='Learn new words']"):
            driver.find_element(By.XPATH, "//html").send_keys(Keys.ENTER)

        elif driver.find_elements(By.XPATH, "//button[@class='sc-1umog8t-0 kFaJKr']"):
            toBeSorted = {}

            question = driver.find_element(By.XPATH, "//h2[@class='sc-af59h9-2 hDpNkj']").accessible_name
            words = driver.find_elements(By.XPATH, "//button[@class='sc-1umog8t-0 kFaJKr']")
            
            preAns = findAns(question).replace(".", "").replace("?", "").replace("!", "").replace("¡", "").replace("¿", "")
            ans = preAns.split(" ")
            
            for word in words:
                if word.accessible_name in ans:
                    toBeSorted[ans.index(word.accessible_name)] = word
            
            sort = collections.OrderedDict(sorted(toBeSorted.items()))

            for id, d in sort.items():
                d.click()

            driver.find_element(By.XPATH, "//html").send_keys(Keys.ENTER)

        elif driver.find_elements(By.XPATH, "//input[@class='sc-1v1crxt-4 kHCLct']"):
            question = driver.find_element(By.XPATH, "//h2[@class='sc-af59h9-2 hDpNkj']").accessible_name
            box = driver.find_element(By.XPATH, "//input[@class='sc-1v1crxt-4 kHCLct']")

            ans = findAns(question)

            if ans != None:
                box.send_keys(ans)

            driver.find_element(By.XPATH, "//html").send_keys(Keys.ENTER)

        elif driver.find_elements(By.XPATH, "//button[@class='sc-bcXHqe iDigtw']"):
            question = driver.find_element(By.XPATH, "//h2[@class='sc-af59h9-2 hDpNkj']").accessible_name

            preAnswers = driver.find_elements(By.XPATH, "//button[@class='sc-bcXHqe iDigtw']")

            answers = []
            for i in preAnswers:
                ans = i.accessible_name.split(' ')
                ans.pop(0)
                answers.append(' '.join(ans))

            correctAnswer = findAns(question)

            for i in range(len(answers)):
                if answers[i] == correctAnswer:
                    preAnswers[i].click()

            driver.find_element(By.XPATH, "//html").send_keys(Keys.ENTER)
        
        elif driver.find_elements(By.XPATH, "//h2[@class='sc-18hl9gu-5 gXQFYZ']"):
            possibleElements = ["class='sc-18hl9gu-6 hjLhBn'"]
            preAns = driver.find_elements(By.XPATH, "//h2[@class='sc-18hl9gu-5 gXQFYZ']")
            
            if not preAns:
                preAns = driver.find_elements(By.XPATH, f"//h3[{possibleElements[0]}]")
            
            ans = preAns[0].accessible_name

            question = driver.find_element(By.XPATH, "//h3[@class='sc-18hl9gu-6 hjLhBn']").accessible_name

            if testIfNotExists(question):
                data.add({"question": question, "ans": ans})
            
            driver.find_element(By.XPATH, "//html").send_keys(Keys.ENTER)

#main method
def main():
    driver = webdriver.Chrome('C:/Program Files/Google/Chrome/Application/chromedriver.exe')

    driver.get("https://app.memrise.com/groups/")

    log_in(driver)
    ai(driver)

if __name__ == '__main__':
    main()