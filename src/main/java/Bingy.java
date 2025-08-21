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

    public ToDo addToDo(String description) {
        ToDo task = new ToDo(description);
        tasks.add(task);
        return task;
    }

    public Deadline addDeadline(String description, String deadline) {
        Deadline task = new Deadline(description, deadline);
        tasks.add(task);
        return task;
    }

    public Events addEvents(String description, String start, String end) {
        Events task = new Events(description, start, end);
        tasks.add(task);
        return task;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public int getSize() {
        return this.getTasks().size();
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

    private void sendMessage(String message) {
        System.out.println(line);
        System.out.println(message);
        System.out.println(line);
    }

    private String listStatus() {
        int size = taskManager.getSize();
        String taskWord = (size == 1) ? "task" : "tasks";
        return String.format("Now you have %d %s in the list", size, taskWord);
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

        String[] split = trimmed.split(" ", 2);
        String action = split[0];
        String payload = split[1];


        String[] parts = trimmed.split("\\s+");
        String command = parts[0];


        if (command.equalsIgnoreCase("mark") || command.equalsIgnoreCase("unmark")) {
            if (parts.length < 2) {
                sendMessage("What the helly. Give a valid input");
                return;
            }
            int taskIndex;
            try {
                taskIndex = Integer.parseInt(parts[1]) - 1; // to 0-based
            } catch (NumberFormatException e) {
                sendMessage(" Give me a task number. Numbers are characters like 1 or 2. Hope that helps!");
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

        }

        if (command.equalsIgnoreCase("todo")) {
            ToDo newTask = taskManager.addToDo(payload.trim());
            sendMessage(String.format("Added this task:\n  %s\n%s", newTask, listStatus()));
        } else if (command.equalsIgnoreCase("deadline")) {
            String[] payloadSplit = payload.split("/by", 2);
            String description = payloadSplit[0].trim();
            String deadline = payloadSplit[1].trim();
            Deadline newTask = taskManager.addDeadline(description, deadline);
            sendMessage(String.format("Time is tickin'!\n  %s\n%s", newTask, listStatus()));
        } else if (command.equalsIgnoreCase("event")) {
            String[] payloadSplit = payload.split("/from", 2);
            String description = payloadSplit[0].trim();
            String[] timeSplit = payloadSplit[1].split("/to", 2);
            String start = timeSplit[0].trim();
            String end = timeSplit[1].trim();
            Events newTask = taskManager.addEvents(description, start, end);
            sendMessage(String.format("Eventing!\n   %s\n%s", newTask, listStatus()));
        }
    }


    private void echo(String input) {
        System.out.println(line + "\n " + input + "\n" + line);
    }

    private void sayGoodbye() {
        System.out.println(line + "\n bbbbYEE. hope to scareee you again soooooOOon! \n" + line);
    }


}