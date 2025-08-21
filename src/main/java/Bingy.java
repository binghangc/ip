import java.util.Scanner;

public class Bingy {
    public static void main(String[] args) {
        BingyBot bot = new BingyBot();
        bot.run();
    }
}

class BingyBot {
    private static final String line = "____________________________________________________________";
    private static final String logo =
            ".-. .-')                .-') _                         \n"
          + "\\  ( OO )              ( OO ) )                        \n"
          + " ;-----.\\   ,-.-') ,--./ ,--,'  ,----.      ,--.   ,--.\n"
          + " | .-.  |   |  |OO)|   \\ |  |\\ '  .-./-')    \\  `.'  / \n"
          + " | '-' /_)  |  |  \\|    \\|  | )|  |_( O- ) .-')     /  \n"
          + " | .-. `.   |  |(_/|  .     |/ |  | .--, \\\\(OO  \\   /   \n"
          + " | |  \\  | ,|  |_.'|  |\\    | (|  | '. (_/ |   /  /\\_  \n"
          + " | '--'  /(_|  |   |  | \\   |  |  '--'  |  `-./  /.__) \n"
          + " `------'   `--'   `--'  `--'   `------'     `--'      \n";
    private boolean running = true;


    public void run() {
        greet();
        Scanner sc = new Scanner(System.in);
        while (running) {
            String input = sc.nextLine();
            handleInput(input);
        }
        sc.close();
    }

    private void greet() {
        System.out.println(logo);
        System.out.println(line);
        System.out.println(" Boo! I'm Bingy");
        System.out.println(" WHAT caan I doooOoo for yoou?");
        System.out.println(line);
    }

    private void handleInput(String input) {
        if (input.trim().equalsIgnoreCase("bye")) {
            sayGoodbye();
            running = false;
        } else {
            echo(input);
        }
    }


    private void echo(String input) {
        System.out.println(line + "\n " + input + "\n" + line);
    }

    private void sayGoodbye() {
        System.out.println(line + "\n bbbbYEE. hope to scareee you again soooooOOon! \n" + line);
    }


}