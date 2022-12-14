import memriseAi.Ai;
import exceptions.UnPassNotFoundException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Memrise {
    private ChromeDriver driver;
    private String username, password, link;
    private Ai ai;

    public Memrise() throws FileNotFoundException, IOException, UnPassNotFoundException {
        Properties props = new Properties();
        props.load(new FileInputStream("config.properties"));

        Boolean SADL = props.getProperty("SADL").equals("true");
        username = props.getProperty("username");
        password = props.getProperty("password");
        link = props.getProperty("link"); 
        Boolean isHidden = props.getProperty("hidden").equals("true");

        ChromeOptions options = new ChromeOptions();

        if (isHidden) {options.addArguments("headless");}

        driver = new ChromeDriver(options);

        log_in();

        ai = new Ai(driver, SADL);
    }

    private void log_in() throws UnPassNotFoundException{
        WebElement un, passwd, submit;
        if (username.equals("") || password.equals("")) {
            throw new UnPassNotFoundException("username or password not found");
        }

        driver.get(link);

        while (true) {
            try {
                

                un = driver.findElement(By.name("username"));
                passwd = driver.findElement(By.name("password"));
                submit = driver.findElement(By.xpath("//button[@data-testid='signinFormSubmit']"));
                break;
            }
            catch (NoSuchElementException|NoSuchWindowException e) {System.out.println(e);}
        }

        un.sendKeys(username);
        passwd.sendKeys(password);

        submit.click();
    }

    public void start() {
        ai.start();
        try {driver.quit();} catch (Exception e) {System.out.println(e);}
    }

    public static void main(String[] args) throws Exception {
        Memrise bot = new Memrise();
        bot.start();
    }
}
