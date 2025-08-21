import java.util.Scanner;

public class Bingy {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        String logo =
                ".-. .-')                .-') _                         \n"
                        + "\\  ( OO )              ( OO ) )                        \n"
                        + " ;-----.\\   ,-.-') ,--./ ,--,'  ,----.      ,--.   ,--.\n"
                        + " | .-.  |   |  |OO)|   \\ |  |\\ '  .-./-')    \\  `.'  / \n"
                        + " | '-' /_)  |  |  \\|    \\|  | )|  |_( O- ) .-')     /  \n"
                        + " | .-. `.   |  |(_/|  .     |/ |  | .--, \\\\(OO  \\   /   \n"
                        + " | |  \\  | ,|  |_.'|  |\\    | (|  | '. (_/ |   /  /\\_  \n"
                        + " | '--'  /(_|  |   |  | \\   |  |  '--'  |  `-./  /.__) \n"
                        + " `------'   `--'   `--'  `--'   `------'     `--'      \n";
        Scanner res = new Scanner(System.in);

        System.out.println(logo);
        System.out.println(line + "\n Boo! I'm Bingy \n");
        System.out.println(" WHAT caan I doooOoo for yoou? \n");
        String response = res.nextLine();
        while (!response.trim().equalsIgnoreCase("bye")) {
            System.out.println(line + "\n" + response + "\n" + line);
            response = res.nextLine();
        }
        System.out.println(line + "\n bbbbYEE. hope to scareee you again soooooOOon! \n" + line);
        res.close();
    }
}