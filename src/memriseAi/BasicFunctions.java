package memriseAi;

import java.util.HashMap;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import exceptions.UnPassNotFoundException;



public class BasicFunctions {
    public static String findAns(HashMap<String, String> words, String question) {//fixMe later
        String listQuestion, listAns;
        for (HashMap.Entry<String, String> entry : words.entrySet()) {
            listQuestion = entry.getKey();
            listAns = entry.getValue();
            if (listQuestion.equals(question)) {return listAns;}
            if (listAns.equals(question)) {return listQuestion;}
        }
        return "";
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
}