package memriseAi;

import java.util.Scanner;

public class Console extends Thread {
    private Scanner sc;
    public boolean escape;

    private String help = "help: helps you\nexit: exits";

    public Console() {
        sc = new Scanner(System.in);
        escape = false;
    }

    public void run() {
        String command;
        while (!escape) {
            System.out.print(":");
            command = sc.nextLine();

            if (command.equals("exit") || command.equals("stop")) {escape = true;}
            else if (command.equals("help")) {System.out.println(help);}
            else if (command.equals("")) {}
            else if (command.equals("cactuses")) {System.out.println("Unknown command \"\033[1mCACTI\033[0m\" use /help you moron just like literally every other command line in existence");}
            else {System.out.println("Unknown command \"" + command + "\" use /help you moron just like literally every other command line in existence");}
        }
    }
}
