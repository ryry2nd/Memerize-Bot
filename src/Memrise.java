import memriseAi.Ai;

import exceptions.UnPassNotFoundException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;



public class Memrise {
    private WebDriver driver;
    private String username, password, link;
    private Ai ai;



    public Memrise() throws FileNotFoundException, IOException, UnPassNotFoundException, InterruptedException {
        Properties props = new Properties();
        props.load(new FileInputStream("config.properties"));

        Boolean SADL = props.getProperty("SADL").equals("true");
        username = props.getProperty("username");
        password = props.getProperty("password");
        String filePath = props.getProperty("configPath");
        link = props.getProperty("link");

        if (!filePath.equals("")) {props = new Properties();props.load(new FileInputStream(filePath));username = props.getProperty("username");password = props.getProperty("password");}
        if (link.equals("")) {link = "https://www.memrise.com/";}

        ChromeOptions options = new ChromeOptions();

        driver = new ChromeDriver(options);

        log_in();

        ai = new Ai(driver, SADL, link);
    }

    private void log_in() throws UnPassNotFoundException, InterruptedException{
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

    public void start() {ai.start();}
    public void quit() {ai.quit();}

    public static void main(String[] args) throws Exception {
        Memrise bot = new Memrise();
        bot.start();
        bot.quit();
    }
}