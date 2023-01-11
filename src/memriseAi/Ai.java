package memriseAi;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Ai {
    private ChromeDriver driver;
    private Boolean SADL;
    private static HashMap<String, String> words = new HashMap<String, String>();

    public Ai(ChromeDriver driver) {
        this.driver = driver;
        this.SADL = true;
    }

    public Ai(ChromeDriver driver, Boolean SADL) {
        this.driver = driver;
        this.SADL = SADL;
    }

    private void skipReminder(){
        driver.findElement(By.xpath("//button[@class='sc-1dxc4vq-2 fjYiwU']")).click();
    }

    private void pressEnter() {
        driver.findElement(By.xpath("//html")).sendKeys(Keys.ENTER);
    }

    private String getQuestion() {
        try {
            return driver.findElement(By.xpath("//h2[@class='sc-af59h9-2 hDpNkj']")).getAccessibleName();
        }
        catch(NoSuchElementException e) {
            return "";
        }
    }

    private void typeInBox() {
        final String QUESTION = getQuestion();

        if (QUESTION.equals("")) {return;}

        final WebElement BOX = driver.findElement(By.xpath("//input[@class='sc-1v1crxt-4 kHCLct']"));
        final String ANS = BasicFunctions.findAns(words, QUESTION);

        BOX.sendKeys(ANS);
        pressEnter();
    }
    
    private void selectBoxes() {
        final String QUESTION = getQuestion();

        if (QUESTION == "") {return;}

        HashMap<Integer, WebElement> toBeSorted = new HashMap<Integer, WebElement>();
        TreeMap<Integer, WebElement> sortedMap;
        List<WebElement> answerBoxes;
        String[] boxAns;
        String preBoxAns = "";

        answerBoxes = driver.findElements(By.xpath("//button[@class='sc-1umog8t-0 kFaJKr']"));

        boxAns = preBoxAns.split(" ");

        int i = 0;
        for (WebElement answerBox : answerBoxes) {
            if (BasicFunctions.isIn(boxAns, answerBox.getAccessibleName())) {
                toBeSorted.put(i, answerBox);
            }
            i++;
        }
        
        sortedMap = BasicFunctions.sortbykey(toBeSorted);

        for (Entry<Integer, WebElement> entry : sortedMap.entrySet()) {
            entry.getValue().click();
        }

        pressEnter();
    }

    private void multipleChoice() {
        final String QUESTION = getQuestion();

        if (QUESTION.equals("")) {return;}

        final String CORRECTANSWER = BasicFunctions.findAns(words, QUESTION);
        
        if (!CORRECTANSWER.equals("")) {
            List<WebElement> answers = driver.findElements(By.xpath("//button[@class='sc-bcXHqe iDigtw']"));;
            String answersName;
            String preAnswersName;
            Boolean isFirstSpace;

            for (int i = 0; i < answers.size(); i++) {
                preAnswersName = answers.get(i).getAccessibleName();
                answersName = "";
                isFirstSpace = true;
                
                for (char ch : preAnswersName.toCharArray()) {
                    if (!isFirstSpace) {answersName += ch;}
                    if (ch == ' ') {isFirstSpace = false;}
                }

                if (answersName.equals(CORRECTANSWER)) {
                    answers.get(i).click();
                    break;
                }
            }
        }

        pressEnter();
    }

    private void learn() {
        final String QUESTION = driver.findElement(By.xpath("//h3[@class='sc-18hl9gu-6 hjLhBn']")).getAccessibleName();
        final String ANS = driver.findElement(By.xpath("//h2[@class='sc-18hl9gu-5 gXQFYZ']")).getAccessibleName();
        
        if (!words.containsKey(QUESTION)) {words.put(QUESTION, ANS);}
        
        pressEnter();
    }

    public void start() {
        while (true) {
            try {
                while (true){
                    if (!driver.findElements(By.xpath("//input[@class='sc-1v1crxt-4 kHCLct']")).isEmpty()) {
                        typeInBox();}

                    else if (!driver.findElements(By.xpath("//button[@class='sc-bcXHqe iDigtw']")).isEmpty()) {
                        multipleChoice();}

                    else if (!driver.findElements(By.xpath("//button[@class='sc-1umog8t-0 kFaJKr']")).isEmpty()) {
                        selectBoxes();}
                    
                    else if (!driver.findElements(By.xpath("//button[@class='sc-1dxc4vq-2 fjYiwU']")).isEmpty()) {
                        skipReminder();}

                    else if (!SADL && !driver.findElements(By.xpath("//div[@data-testid='course-leaderboard']")).isEmpty()) {
                        pressEnter();}

                    else if (!driver.findElements(By.xpath("//a[@aria-label='Learn new words']")).isEmpty()) {
                        pressEnter();}
                    
                    else if (!driver.findElements(By.xpath("//h2[@class='sc-18hl9gu-5 gXQFYZ']")).isEmpty()) {
                        learn();}
                }
            }
            catch (NoSuchWindowException e) {break;}
            catch (Exception e) {driver.navigate().refresh(); System.out.println(e);}
        }
    }
}