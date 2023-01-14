package memriseAi;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Ai {
    private WebDriver driver;
    private Boolean SADL;
    private Console console;
    private static HashMap<String, String> words = new HashMap<String, String>();

    public Ai(WebDriver driver) {
        this.driver = driver;
        this.SADL = true;
        console = new Console();
    }

    public Ai(WebDriver driver, Boolean SADL) {
        this.driver = driver;
        this.SADL = SADL;
        console = new Console();
    }

    public void quit() {
        console.escape = true;
        System.out.println("Program stopped, press enter to exit");
    }

    public static HashMap<String, String> getWords() {return words;}

    private void skipReminder(){
        driver.findElement(By.xpath("//button[@class='sc-1dxc4vq-2 fjYiwU']")).click();
    }

    private void pressEnter() {
        driver.findElement(By.xpath("//html")).sendKeys(Keys.ENTER);
    }

    private String getQuestion() {
        return driver.findElement(By.xpath("//h2[@class='sc-af59h9-2 hDpNkj']")).getAccessibleName();
    }

    private void typeInBox() {
        driver.findElement(By.xpath("//input[@class='sc-1v1crxt-4 kHCLct']")).sendKeys(BasicFunctions.findAns(words, getQuestion()));
        pressEnter();
    }
    
    private void selectBoxes() {
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
        final String CORRECTANSWER = BasicFunctions.findAns(words, getQuestion());
        
        if (!CORRECTANSWER.equals("")) {
            List<WebElement> answers = driver.findElements(By.xpath("//button[@class='sc-bcXHqe iDigtw']"));;
            String answersName, preAnswersName;
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
        console.start();

        while (true) {
            try {
                while (!console.escape){
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
                return;
            }
            catch (NoSuchWindowException e) {break;}
            catch (Exception e) {System.out.println(e);}

            try {
                driver.navigate().refresh();
            } catch (Exception e) {System.out.println("FATAL");break;}
        }
    }
}