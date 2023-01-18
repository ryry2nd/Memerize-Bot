import memriseAi.Ai;

import exceptions.UnPassNotFoundException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.chrome.ChromeOptions;



public class Memrise {
    private Ai ai;



    public Memrise() throws FileNotFoundException, IOException, UnPassNotFoundException, InterruptedException {
        Properties props = new Properties();
        props.load(new FileInputStream("config.properties"));

        Boolean SADL = props.getProperty("SADL").equals("true");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        String filePath = props.getProperty("configPath");
        String link = props.getProperty("link");

        if (!filePath.equals("")) {props = new Properties();props.load(new FileInputStream(filePath));username = props.getProperty("username");password = props.getProperty("password");}
        if (link.equals("")) {link = "https://www.memrise.com/";}

        ChromeOptions options = new ChromeOptions();

        ai = new Ai(options, username, password, SADL, link);
    }

    public void start() throws UnPassNotFoundException, InterruptedException {ai.start();}
    public void quit() {ai.quit();}

    public static void main(String[] args) throws Exception {
        Memrise bot = new Memrise();
        bot.start();
        bot.quit();
    }
}