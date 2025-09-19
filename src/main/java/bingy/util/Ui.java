package bingy.util;

import java.util.List;

import bingy.tasks.Deadline;
import bingy.tasks.Events;
import bingy.tasks.Task;
import bingy.tasks.ToDo;


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
    public String greet() {
        StringBuilder sb = new StringBuilder();
        sb.append(LOGO).append("\n")
          .append(LINE).append("\n")
          .append(" Boo! I'm Bingy\n")
          .append(" WHAT caan't I doooOoo for yoou?\n")
          .append(LINE);
        return sb.toString();
    }

    /**
     * Prints a message wrapped by horizontal separator lines for consistent formatting.
     *
     * @param message the text to display to the user.
     */
    public String sendMessage(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE).append("\n")
          .append(message).append("\n")
          .append(LINE);
        return sb.toString();
    }

    /**
     * Prints a farewell banner when the application exits.
     */
    public String sayGoodbye() {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE)
          .append("\nBye bye! \n")
          .append(LINE);
        return sb.toString();
    }


    /**
     * Displays list of matched tasks and enumerates matching tasks.
     *
     * @param tasks list of matching {@link bingy.tasks.Task}.
     */
    public String showMatches(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE).append("\n");
        sb.append("Here are the matches:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d. %s%n", i + 1, tasks.get(i)));
        }
        sb.append(LINE);
        return sb.toString();
    }

    /**
     * Displays all tasks in the given list, numbered from 1.
     *
     * @param tasks the list of {@link bingy.tasks.Task} to display.
     */
    public String showTasks(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE).append("\n");
        sb.append("Here's the list of chores you will NOT complete MUAHAHAH\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d. %s%n", i + 1, tasks.get(i)));
        }
        sb.append(LINE);
        return sb.toString();
    }

    /**
     * Displays a confirmation after adding a {@link bingy.tasks.ToDo},
     * along with the updated task count from the {@link bingy.util.TaskManager}.
     *
     * @param task  the {@link bingy.tasks.ToDo} that was added.
     * @param tasks the current {@link bingy.util.TaskManager} used to compute task count.
     */
    public String showAdded(ToDo task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        return sendMessage(String.format("Added this task:\n  %s\n%s", task, status));
    }

    /**
     * Displays a confirmation after adding a {@link bingy.tasks.Deadline},
     * along with the updated task count from the {@link bingy.util.TaskManager}.
     *
     * @param task  the {@link bingy.tasks.Deadline} that was added.
     * @param tasks the current {@link bingy.util.TaskManager} used to compute task count.
     */
    public String showDeadline(Deadline task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        return sendMessage(String.format("Time is tickin'!\n  %s\n%s", task, status));
    }

    /**
     * Displays a confirmation after adding an {@link bingy.tasks.Events} task,
     * along with the updated task count from the {@link bingy.util.TaskManager}.
     *
     * @param task  the {@link bingy.tasks.Events} task that was added.
     * @param tasks the current {@link bingy.util.TaskManager} used to compute task count.
     */
    public String showEvent(Events task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        return sendMessage(String.format("Eventing!\n   %s\n%s", task, status));
    }

    /**
     * Displays a confirmation after deleting a {@link bingy.tasks.Task},
     * along with the updated task count from the {@link bingy.util.TaskManager}.
     *
     * @param task  the {@link bingy.tasks.Task} that was deleted.
     * @param tasks the current {@link bingy.util.TaskManager} used to compute task count.
     */
    public String showDelete(Task task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        return sendMessage(String.format("Noted. I've removed this task:\n  %s\n%s", task, status));
    }
}
