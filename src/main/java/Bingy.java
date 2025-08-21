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
    private List<Task> tasks;

    public TaskManager(int size) {
        tasks = new ArrayList<>();
    }

    public void markDone(int index) {
        tasks.get(index).markDone();
    }

    public void markUndone(int index) {
        tasks.get(index).markUndone();
    }

    public void addTask(String task) {
        tasks.add(new Task(task));
    }

    public List<Task> getTasks() {
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
        System.out.println(" WHAT caan't I doooOoo for yoou?");
        System.out.println(line);
    }

    private void handleInput(String input) {
        String trimmed = input.trim();


        if (trimmed.equalsIgnoreCase("bye")) {
            sayGoodbye();
            running = false;
            return;
        }


        if (trimmed.equalsIgnoreCase("list")) {
            System.out.println(line);
            System.out.println("Here's the list of chores you will NOT complete MUAHAHAH");
            List<Task> tasks = taskManager.getTasks();
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(String.format("%d. %s", i + 1, tasks.get(i).toString()));
            }
            System.out.println(line);
            return;
        }


        String[] parts = trimmed.split("\\s+");
        String command = parts[0];


        if (command.equalsIgnoreCase("mark") || command.equalsIgnoreCase("unmark")) {
            if (parts.length < 2) {
                System.out.println(line);
                System.out.println("What the helly. Give me a task number like \"Mark 2\" or something. Quick");
                System.out.println(line);
                return;
            }
            int taskIndex;
            try {
                taskIndex = Integer.parseInt(parts[1]) - 1; // to 0-based
            } catch (NumberFormatException e) {
                System.out.println(line);
                System.out.println(" Give me a task number. Numbers are characters like 1 or 2. Hope that helps!");
                System.out.println(line);
                return;
            }
            List<Task> tasks = taskManager.getTasks();

            Task t = tasks.get(taskIndex);
            if (command.equalsIgnoreCase("mark")) {
                taskManager.markDone(taskIndex);
                System.out.println(line);
                System.out.println(" Hopefully you did the task properly... Marked it for you");
                System.out.println("   " + t);
                System.out.println(line);
            } else {
                taskManager.markUndone(taskIndex);
                System.out.println(line);
                System.out.println(" Ha! I knew you couldn't do it. Unmarked it! Welcome");
                System.out.println("   " + t);
                System.out.println(line);
            }
            return;
        }


        taskManager.addTask(trimmed);
        System.out.println(line);
        System.out.println("added: " + trimmed);
        System.out.println(line);
    }


    private void echo(String input) {
        System.out.println(line + "\n " + input + "\n" + line);
    }

    private void sayGoodbye() {
        System.out.println(line + "\n bbbbYEE. hope to scareee you again soooooOOon! \n" + line);
    }


}