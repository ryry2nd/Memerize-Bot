package memriseAi;

import java.util.HashMap;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import exceptions.UnPassNotFoundException;



public class BasicFunctions {
    public static String findAns(HashMap<String, String> words, String question) {
        String ans = words.get(question);
        if (ans == null) {return "";}
        return ans;
    }

    public static void log_in(WebDriver driver, String username, String password, String link) throws UnPassNotFoundException, InterruptedException{
        WebElement un, passwd, submit;

        if (username.equals("") || password.equals("")) {
            throw new UnPassNotFoundException("username or password not found");
        }

        while (true) {
            try {
                driver.get(link);
                break;
            } catch (NoSuchElementException e) {}
        }
        Thread.sleep(1000);
        while (true) {
            try {
                un = driver.findElement(By.name("username"));
                passwd = driver.findElement(By.name("password"));
                submit = driver.findElement(By.xpath("//button[@data-testid='signinFormSubmit']"));
                break;
            } catch (NoSuchElementException|NoSuchWindowException e) {}
        }

        un.sendKeys(username);
        passwd.sendKeys(password);
        submit.click();
    }

    public static void pressEnter(WebDriver driver) {
        driver.findElement(By.xpath("//html")).sendKeys(Keys.ENTER);
    }
    public static String getQuestion(WebDriver driver) {
        return driver.findElement(By.xpath("//h2[@class='sc-af59h9-2 hDpNkj']")).getAccessibleName();
    }
    public static void skipReminder(WebDriver driver) {
        driver.findElement(By.xpath("//button[@class='sc-1dxc4vq-2 fjYiwU']")).click();
    }
}