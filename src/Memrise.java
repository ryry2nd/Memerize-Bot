import memriseAi.Ai;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Memrise {
    private ChromeDriver driver;
    private String username, password, link;
    private Ai ai;

    public Memrise() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("config.properties"));

        Boolean SADL = props.getProperty("SADL").equals("true");
        username = props.getProperty("username");
        password = props.getProperty("password");
        link = props.getProperty("link"); 
        driver = new ChromeDriver();

        log_in();

        ai = new Ai(driver, SADL);
    }

    private void log_in() {
        driver.get(link);
        WebElement un = driver.findElement(By.name("username"));
        WebElement passwd = driver.findElement(By.name("password"));
        WebElement submit = driver.findElement(By.xpath("//button[@data-testid='signinFormSubmit']"));

        un.sendKeys(username);
        passwd.sendKeys(password);

        submit.click();
    }

    public void start() {
        ai.start();
    }

    

    public static void main(String[] args) throws Exception {
        Memrise bot = new Memrise();
        bot.start();
    }
}
