package bingy.util;

import bingy.tasks.Task;
import bingy.tasks.ToDo;
import bingy.tasks.Deadline;
import bingy.tasks.Events;

import java.util.List;

/**
 * Handles all user interface interactions for the Bingy application.
 * <p>
 * Responsible for displaying messages, task lists, greetings, and status updates
 * in a consistent format. This class does not contain business logic; it simply
 * renders information passed from other components such as {@link bingy.util.TaskManager}
 * and task types like {@link bingy.tasks.Task}, {@link bingy.tasks.ToDo},
 * {@link bingy.tasks.Deadline}, and {@link bingy.tasks.Events}.
 */
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

    /**
     * Prints the ASCII logo and a greeting banner when the application starts.
     */
    public void greet() {
        System.out.println(LOGO);
        System.out.println(LINE);
        System.out.println(" Boo! I'm Bingy");
        System.out.println(" WHAT caan't I doooOoo for yoou?");
        System.out.println(LINE);
    }

    /**
     * Prints a message wrapped by horizontal separator lines for consistent formatting.
     *
     * @param message the text to display to the user.
     */
    public void sendMessage(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    /**
     * Prints a farewell banner when the application exits.
     */
    public void sayGoodbye() {
        System.out.println(LINE + "\n bbbbYEE. hope to scareee you again soooooOOon! \n" + LINE);
    }


    /**
     * Displays all tasks in the given list, numbered from 1.
     *
     * @param tasks the list of {@link bingy.tasks.Task} to display.
     */
    public void showTasks(List<Task> tasks) {
        System.out.println(LINE);
        System.out.println("Here's the list of chores you will NOT complete MUAHAHAH");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, tasks.get(i).toString()));
        }
        System.out.println(LINE);
    }

    /**
     * Displays a confirmation after adding a {@link bingy.tasks.ToDo},
     * along with the updated task count from the {@link bingy.util.TaskManager}.
     *
     * @param task  the {@link bingy.tasks.ToDo} that was added.
     * @param tasks the current {@link bingy.util.TaskManager} used to compute task count.
     */
    public void showAdded(ToDo task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        sendMessage(String.format("Added this task:\n  %s\n%s", task, status));
    }

    /**
     * Displays a confirmation after adding a {@link bingy.tasks.Deadline},
     * along with the updated task count from the {@link bingy.util.TaskManager}.
     *
     * @param task  the {@link bingy.tasks.Deadline} that was added.
     * @param tasks the current {@link bingy.util.TaskManager} used to compute task count.
     */
    public void showDeadline(Deadline task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        sendMessage(String.format("Time is tickin'!\n  %s\n%s", task, status));
    }

    /**
     * Displays a confirmation after adding an {@link bingy.tasks.Events} task,
     * along with the updated task count from the {@link bingy.util.TaskManager}.
     *
     * @param task  the {@link bingy.tasks.Events} task that was added.
     * @param tasks the current {@link bingy.util.TaskManager} used to compute task count.
     */
    public void showEvent(Events task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        sendMessage(String.format("Eventing!\n   %s\n%s", task, status));
    }
}
