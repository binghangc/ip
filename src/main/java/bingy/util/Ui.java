package bingy.util;

import bingy.tasks.Task;
import bingy.tasks.ToDo;
import bingy.tasks.Deadline;
import bingy.tasks.Events;

import java.util.List;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String LOGO =
            ".-. .-')                .-') _                         \n"
          + "\\  ( OO )              ( OO ) )                        \n"
          + " ;-----.\\   ,-.-') ,--./ ,--,'  ,----.      ,--.   ,--.\n"
          + " | .-.  |   |  |OO)|   \\ |  |\\ '  .-./-')    \\  `.'  / \n"
          + " | '-' /_)  |  |  \\|    \\|  | )|  |_( O- ) .-')     /  \n"
          + " | .-. `.   |  |(_/|  .     |/ |  | .--, \\\\(OO  \\   /   \n"
          + " | |  \\  | ,|  |_.'|  |\\    | (|  | '. (_/ |   /  /\\_  \n"
          + " | '--'  /(_|  |   |  | \\   |  |  '--'  |  `-./  /.__) \n"
          + " `------'   `--'   `--'  `--'   `------'     `--'      \n";

    public void greet() {
        System.out.println(LOGO);
        System.out.println(LINE);
        System.out.println(" Boo! I'm Bingy");
        System.out.println(" WHAT caan't I doooOoo for yoou?");
        System.out.println(LINE);
    }

    public void sendMessage(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    public void sayGoodbye() {
        System.out.println(LINE + "\n bbbbYEE. hope to scareee you again soooooOOon! \n" + LINE);
    }

    public void echo(String input) {
        System.out.println(LINE + "\n " + input + "\n" + LINE);
    }

    /**
     * Displays list of matched tasks and enumerates matching tasks.
     *
     * @param tasks list of matching {@link bingy.tasks.Task}.
     */
    public void showMatches(List<Task> tasks) {
        System.out.println(LINE);
        System.out.println("Here are the matches:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, tasks.get(i).toString()));
        }
        System.out.println(LINE);
    }

    public void showTasks(List<Task> tasks) {
        System.out.println(LINE);
        System.out.println("Here's the list of chores you will NOT complete MUAHAHAH");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, tasks.get(i).toString()));
        }
        System.out.println(LINE);
    }

    public void showAdded(ToDo task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        sendMessage(String.format("Added this task:\n  %s\n%s", task, status));
    }

    public void showDeadline(Deadline task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        sendMessage(String.format("Time is tickin'!\n  %s\n%s", task, status));
    }

    public void showEvent(Events task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        sendMessage(String.format("Eventing!\n   %s\n%s", task, status));
    }
}
