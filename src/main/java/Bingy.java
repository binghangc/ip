import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Bingy {
    public static void main(String[] args) {
        BingyBot bot = new BingyBot();
        bot.run();
    }
}

class TaskManager {
    private static final String line = "____________________________________________________________";
    private List<String> tasks;

    public TaskManager(int size) {
        tasks = new ArrayList<>();
    }

    public void addTask(String task) {
        tasks.add(task);
    }

    public List<String> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

}

class BingyBot {
    private static final String line = "____________________________________________________________";
    private static TaskManager taskManager = new TaskManager(100);
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
        } else if (input.trim().equalsIgnoreCase("list")) {
            System.out.println(line);
            List<String> tasks = taskManager.getTasks();
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(String.format("%d. %s", i + 1, tasks.get(i)));
            }
            System.out.println(line);
        } else {
            taskManager.addTask(input);
            System.out.println(line);
            System.out.println("added: " + input);
            System.out.println(line);
        }
    }


    private void echo(String input) {
        System.out.println(line + "\n " + input + "\n" + line);
    }

    private void sayGoodbye() {
        System.out.println(line + "\n bbbbYEE. hope to scareee you again soooooOOon! \n" + line);
    }


}