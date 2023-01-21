package memriseAi;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import exceptions.UnPassNotFoundException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.List;



public class Ai extends BasicFunctions{
    private WebDriver driver;
    private ChromeOptions options;
    private boolean SADL;
    private Console console;
    private String username, password;
    private static HashMap<String, String> words = new HashMap<String, String>();



    public Ai(ChromeOptions options, String username, String password) {
        this.options = options;
        this.username = username;
        this.password = password;
        this.SADL = true;
        console = new Console("https://www.memrise.com/");
    }
    public Ai(ChromeOptions options, String username, String password, boolean SADL, String link) {
        this.options = options;
        this.username = username;
        this.password = password;
        this.SADL = SADL;
        console = new Console(link);
    }
    

    public void quit() {
        console.escape = true;
        driver.quit();
        System.out.println("Program stopped, press enter to exit");
    }

    public static HashMap<String, String> getWords() {return words;}


    private void typeInBox() {
        driver.findElement(By.xpath("//input[@class='sc-1v1crxt-4 kHCLct']")).sendKeys(findAns(words, getQuestion(driver)));
        pressEnter(driver);
    }
    private void multipleChoice() {
        final String CORRECTANSWER = findAns(words, getQuestion(driver));
        
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

        pressEnter(driver);
    }
    private void learn() {
        final String QUESTION = driver.findElement(By.xpath("//h3[@class='sc-18hl9gu-6 hjLhBn']")).getAccessibleName();
        final String ANS = driver.findElement(By.xpath("//h2[@class='sc-18hl9gu-5 gXQFYZ']")).getAccessibleName();
        
        if (!words.containsKey(QUESTION)) {
            words.put(QUESTION, ANS);words.put(ANS, QUESTION);}
        
        pressEnter(driver);
    }


    public void start() throws UnPassNotFoundException, InterruptedException {
        long clock;
        
        console.start();

        driver = new ChromeDriver(options);
        log_in(driver, username, password, console.getLink());

        while (true) {
            clock = System.currentTimeMillis();
            try {
                while (!console.escape){
                    if (!driver.findElements(By.xpath("//input[@class='sc-1v1crxt-4 kHCLct']")).isEmpty()) {
                        typeInBox();}

                    else if (!SADL && !driver.findElements(By.xpath("//div[@data-testid='course-leaderboard']")).isEmpty()) {
                        if (((System.currentTimeMillis() - clock) > (5 * 60000)) || console.refresh) {driver.get(console.getLink()); clock = System.currentTimeMillis(); console.refresh = false;} else {pressEnter(driver);}}

                    else if (!driver.findElements(By.xpath("//button[@class='sc-bcXHqe iDigtw']")).isEmpty()) {
                        multipleChoice();}
                    
                    else if (!driver.findElements(By.xpath("//button[@class='sc-1umog8t-0 kFaJKr']")).isEmpty()) {
                        pressEnter(driver);}
                    
                    else if (!driver.findElements(By.xpath("//button[@class='sc-1dxc4vq-2 fjYiwU']")).isEmpty()) {
                        skipReminder(driver);}

                    else if (!driver.findElements(By.xpath("//a[@aria-label='Learn new words']")).isEmpty()) {
                        pressEnter(driver);}

                    else if (!driver.findElements(By.xpath("//div[@class='sc-s6iyrn-2 hldCEU']")).isEmpty()) {
                        driver.get(console.getLink());}
                    
                    else if (!driver.findElements(By.xpath("//h2[@class='sc-18hl9gu-5 gXQFYZ']")).isEmpty()) {
                        learn();}

                    else if (console.refresh) {driver.get(console.getLink()); console.refresh = false;}
                }
                return;
            }

            catch (NoSuchWindowException e) {break;}
            catch (Exception e) {System.out.println(e);}

            try {driver.get(console.getLink());} catch (Exception e) {System.out.println("FATAL");break;}
        }
    }
}