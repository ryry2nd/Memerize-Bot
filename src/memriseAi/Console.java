package memriseAi;

import java.util.Scanner;

public class Console extends Thread {
    private Scanner sc;
    public boolean escape;

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

            System.out.println();
        }
    }
}
