package memriseAi;

import java.util.Scanner;



public class Console extends Thread {
    public boolean escape;
    public boolean refresh;

    private Scanner sc;
    private String link;

    private String help = "help: helps you\nexit: exits\nchlink: changes link\nrefresh: refreshes";

    public Console(String link) {
        sc = new Scanner(System.in);
        escape = false;
        refresh = false;
        this.link = link;
    }

    public String getLink() {return link;}

    public void run() {
        int i;
        String[] wholeCommand;
        String command;
        String[] args;
        
        while (!escape) {
            System.out.print(":");

            wholeCommand = sc.nextLine().split(" ");
            command = wholeCommand[0];
            args = new String[wholeCommand.length-1];
            for (i = 1; i < wholeCommand.length; i++) {args[i - 1] = wholeCommand[i];}

            if (command.equals("")) {}
            else if (command.equals("exit") || command.equals("stop")) {escape = true;}
            else if (command.equals("help")) {System.out.println(help);}
            else if (command.equals("chlink")) {link = args[0]; refresh = true;}
            else if (command.equals("refresh")) {refresh = true;}
            else if (command.equals("cactuses")) {System.out.println("Unknown command \"\033[1mCACTI\033[0m\" use /help you moron just like literally every other command line in existence");}
            else {System.out.println("Unknown command \"" + command + "\" use /help you moron just like literally every other command line in existence");}
        }
    }
}