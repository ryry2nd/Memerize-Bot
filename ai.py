from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.common.exceptions import *
from selenium.webdriver.common.keys import Keys
from pysondb import PysonDB
import collections

class Ai:
    def __init__(self, driver: webdriver.Chrome, sadl=True, fName="data.json"):
        self.data = PysonDB(fName)
        self.SADL = sadl
        self.driver = driver
        self.start()

    #finds the answer
    def findAns(self, question):
        for x, y in self.data.get_by_query(lambda x: x['question'] == question).items():
            return y["ans"]
        for x, y in self.data.get_by_query(lambda x: x['ans'] == question).items():
            return y["question"]
    
    def skipReminder(self):
        self.driver.find_element(By.XPATH, "//button[@class='sc-1dxc4vq-2 fjYiwU']").click()

    def pressEnter(self):
        self.driver.find_element(By.XPATH, "//html").send_keys(Keys.ENTER)
    
    def typeInBox(self):
        question = self.driver.find_element(By.XPATH, "//h2[@class='sc-af59h9-2 hDpNkj']").accessible_name
        box = self.driver.find_element(By.XPATH, "//input[@class='sc-1v1crxt-4 kHCLct']")

        ans = self.findAns(question)

        if ans != None:
            box.send_keys(ans)

        self.pressEnter()
    
    def selectBoxes(self):
        toBeSorted = {}

        question = self.driver.find_element(By.XPATH, "//h2[@class='sc-af59h9-2 hDpNkj']").accessible_name
        words = self.driver.find_elements(By.XPATH, "//button[@class='sc-1umog8t-0 kFaJKr']")
        
        preAns = self.findAns(question)
        if preAns == None:
            ans = ""
        else:
            ans = preAns.replace(".", "").replace("?", "").replace("!", "").replace("¡", "").replace("¿", "").split(" ")
        
        for word in words:
            if word.accessible_name in ans:
                toBeSorted[ans.index(word.accessible_name)] = word
        
        sort = collections.OrderedDict(sorted(toBeSorted.items()))

        for id, d in sort.items():
            d.click()

        self.pressEnter()
    
    def multipleChoice(self):
        question = self.driver.find_element(By.XPATH, "//h2[@class='sc-af59h9-2 hDpNkj']").accessible_name

        preAnswers = self.driver.find_elements(By.XPATH, "//button[@class='sc-bcXHqe iDigtw']")

        answers = []
        for i in preAnswers:
            ans = i.accessible_name.split(' ')
            ans.pop(0)
            answers.append(' '.join(ans))

        correctAnswer = self.findAns(question)

        for i in range(len(answers)):
            if answers[i] == correctAnswer:
                preAnswers[i].click()
                break

        self.pressEnter()
    
    def learn(self):
        preAns = self.driver.find_elements(By.XPATH, "//h2[@class='sc-18hl9gu-5 gXQFYZ']")
        ans = preAns[0].accessible_name

        question = self.driver.find_element(By.XPATH, "//h3[@class='sc-18hl9gu-6 hjLhBn']").accessible_name

        questionExists = self.data.get_by_query(lambda x: x['question'] == question)
        if questionExists:
            ansExists = questionExists[list(questionExists)[0]]["ans"] == ans

        if questionExists and not(ansExists):
            self.data.delete_by_query(lambda x: x['question'] == question)
            self.data.add({"question": question, "ans": ans})
        elif not(questionExists):
            self.data.add({"question": question, "ans": ans})
        
        self.pressEnter()

    def start(self):
        while True:
            if self.driver.find_elements(By.XPATH, "//button[@class='sc-1dxc4vq-2 fjYiwU']"):
                self.skipReminder()

            elif self.driver.find_elements(By.XPATH, "//a[@aria-label='classic_review']") and not self.SADL:
                self.pressEnter()

            elif self.driver.find_elements(By.XPATH, "//a[@aria-label='Learn new words']"):
                self.pressEnter()

            elif self.driver.find_elements(By.XPATH, "//input[@class='sc-1v1crxt-4 kHCLct']"):
                self.typeInBox()

            elif self.driver.find_elements(By.XPATH, "//button[@class='sc-1umog8t-0 kFaJKr']"):
                self.selectBoxes()

            elif self.driver.find_elements(By.XPATH, "//button[@class='sc-bcXHqe iDigtw']"):
                self.multipleChoice()
            
            elif self.driver.find_elements(By.XPATH, "//h2[@class='sc-18hl9gu-5 gXQFYZ']"):
                self.learn()