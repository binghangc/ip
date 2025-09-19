package mario.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import mario.tasks.Deadline;
import mario.tasks.Events;
import mario.tasks.Task;
import mario.tasks.ToDo;


/**
 * Handles all user interface interactions for the Bingy application.
 * <p>
 * Responsible for displaying messages, task lists, greetings, and status updates
 * in a consistent format. This class does not contain business logic; it simply
 * renders information passed from other components such as {@link mario.util.TaskManager}
 * and task types like {@link mario.tasks.Task}, {@link mario.tasks.ToDo},
 * {@link mario.tasks.Deadline}, and {@link mario.tasks.Events}.
 */
public class Ui {
    private static final String LINE = "";
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
        return message;
    }

    /**
     * Prints a farewell banner when the application exits.
     */
    public String sayGoodbye() {
        return "Consider yourself dismissed, plumber.";
    }


    /**
     * Displays list of matched tasks and enumerates matching tasks.
     *
     * @param tasks list of matching {@link mario.tasks.Task}.
     */
    public String showMatches(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("I found what you were looking for. Of course, you couldn't find it yourself:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d. %s%n", i + 1, tasks.get(i)));
        }
        return sb.toString();
    }

    /**
     * Displays all tasks in the given list, numbered from 1.
     *
     * @param tasks the list of {@link mario.tasks.Task} to display.
     */
    public String showTasks(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("Here's the list of chores you will NOT complete MUAHAHAH\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d. %s%n", i + 1, tasks.get(i)));
        }
        sb.append(LINE);
        return sb.toString();
    }

    /**
     * Displays a confirmation after adding a {@link mario.tasks.ToDo},
     * along with the updated task count from the {@link mario.util.TaskManager}.
     *
     * @param task  the {@link mario.tasks.ToDo} that was added.
     * @param tasks the current {@link mario.util.TaskManager} used to compute task count.
     */
    public String showAdded(ToDo task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        return sendMessage(String.format("A new demand has been added to your pathetic list. "
                + "It's my will that you...\n  %s\n%s", task, status));
    }

    /**
     * Displays a confirmation after adding a {@link mario.tasks.Deadline},
     * along with the updated task count from the {@link mario.util.TaskManager}.
     *
     * @param task  the {@link mario.tasks.Deadline} that was added.
     * @param tasks the current {@link mario.util.TaskManager} used to compute task count.
     */
    public String showDeadline(Deadline task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        return sendMessage(String.format("Time is tickin'!\n  %s\n%s", task, status));
    }

    /**
     * Displays a confirmation after adding an {@link mario.tasks.Events} task,
     * along with the updated task count from the {@link mario.util.TaskManager}.
     *
     * @param task  the {@link mario.tasks.Events} task that was added.
     * @param tasks the current {@link mario.util.TaskManager} used to compute task count.
     */
    public String showEvent(Events task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        return sendMessage(String.format("Looks like you have a new 'event' to attend to:\n   %s\n%s", task, status));
    }

    /**
     * Displays a confirmation after deleting a {@link mario.tasks.Task},
     * along with the updated task count from the {@link mario.util.TaskManager}.
     *
     * @param task  the {@link mario.tasks.Task} that was deleted.
     * @param tasks the current {@link mario.util.TaskManager} used to compute task count.
     */
    public String showDelete(Task task, TaskManager tasks) {
        String taskWord = (tasks.getSize() == 1) ? "task" : "tasks";
        String status = String.format("Now you have %d %s in the list", tasks.getSize(), taskWord);
        return sendMessage(String.format("Pathetic. I've erased that one for you:\n  %s\n%s", task, status));
    }
    /**
     * Displays a daily schedule view of timed tasks (Deadlines and Events) for a given date.
     *
     * @param tasks the list of timed tasks to display
     * @param date  the date for which the schedule is being shown
     * @return formatted string representation of the schedule
     */
    public String showSchedule(List<? extends Task> tasks, LocalDate date) {
        StringBuilder sb = new StringBuilder();
        sb.append("MY DEMANDS FOR ").append(date.format(DateTimeFormatter.ofPattern("EEE, MMM d yyyy"))).append(":\n");

        if (tasks.isEmpty()) {
            sb.append("  (No events or deadlines)\n");
        } else {
            for (Task t : tasks) {
                sb.append("  ").append(t).append("\n");
            }
        }

        sb.append(LINE);
        return sb.toString();
    }
}
