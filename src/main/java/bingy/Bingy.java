package bingy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bingy.exceptions.EmptyDeadlineTimeException;
import bingy.exceptions.EmptyEventTimeException;
import bingy.exceptions.EmptyKeywordException;
import bingy.exceptions.EmptyTaskException;
import bingy.exceptions.InvalidCommandException;
import bingy.exceptions.InvalidTaskIndexException;
import bingy.tasks.Deadline;
import bingy.tasks.Events;
import bingy.tasks.Task;
import bingy.tasks.ToDo;
import bingy.util.Parser;
import bingy.util.Parser.ParsedCommand;
import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;

/**
 * Represents the main entry point for the Bingy chatbot application.
 * It coordinates user input, parsing, task management, and UI.
 */
public class Bingy {
    private static TaskManager taskManager = new TaskManager(100);
    private static final Storage storage = new Storage("tasks.txt");
    private final Ui ui = new Ui();
    private boolean isRunning = true;


    /**
     * Starts the main program loop.
     * Loads tasks from storage, greets the user, and continuously reads input
     * until the user exits.
     */
    public void run() {
        ui.greet();
        try {
            ArrayList<Task> loaded = storage.load();
            taskManager.addAll(loaded);
        } catch (IOException e) {
            ui.sendMessage("Starting fresh (no saved tasks found).");
        }
        Scanner sc = new Scanner(System.in);
        while (isRunning) {
            try {
                String input = sc.nextLine();
                handleInput(input);
            } catch (Exception e) {
                ui.sendMessage(e.getMessage());
            }
        }
        sc.close();
    }


    /**
     * Handles user input and executes corresponding command.
     *
     * @param input raw String user input when user presses enter.
     * @throws EmptyTaskException if the command is missing a required task description.
     *
     */
    private void handleInput(String input) throws EmptyTaskException {
        String trimmed = input.trim();

        ParsedCommand cmd = Parser.parseUserCommand(input);

        if (cmd.getType() == ParsedCommand.Type.UNKNOWN) {
            // Preserve the user's command word for a clearer error message
            String cmdWord = (cmd.getArg1() != null && !cmd.getArg1().isBlank())
                    ? cmd.getArg1()
                    : (trimmed.isEmpty() ? "" : trimmed.split("\\s+", 2)[0]);
            throw new InvalidCommandException(cmdWord);
        }


        switch (cmd.getType()) {
        case BYE:
            sayGoodbye();
            isRunning = false;
            return;

        case LIST:
            ui.showTasks(taskManager.getTasks());
            return;

        case FIND:
            if (cmd.getArg1() == null || cmd.getArg1().isEmpty()) {
                throw new EmptyKeywordException();
            }
            ui.showMatches(taskManager.find(cmd.getArg1()));
            return;

        case MARK:
        case UNMARK:
        case DELETE:
            if (cmd.getArg1() == null || cmd.getArg1().isEmpty()) {
                throw new InvalidTaskIndexException("Please provide a task number after the command. Example: mark 2");
            }
            int taskIndex;
            try {
                taskIndex = Integer.parseInt(cmd.getArg1()) - 1; // to 0-based
            } catch (NumberFormatException e) {
                ui.sendMessage(" Give me a task number. Numbers are characters like 1 or 2. Hope that helps!");
                return;
            }
            List<Task> taskList = taskManager.getTasks();
            if (taskIndex < 0 || taskIndex >= taskList.size()) {
                throw new InvalidTaskIndexException(
                        String.format("Index %d is out of bounds (valid range: 1 to %d)",
                                taskIndex + 1, taskList.size())
                );
            }

            Task t = taskList.get(taskIndex);
            if (cmd.getType() == ParsedCommand.Type.MARK) {
                taskManager.markDone(taskIndex);
                ui.sendMessage(" Hopefully you did the task properly... Marked it for you\n   " + t);
                persist();
                return;
            } else if (cmd.getType() == ParsedCommand.Type.UNMARK) {
                taskManager.markUndone(taskIndex);
                ui.sendMessage(" Ha! I knew you couldn't do it. Unmarked it! Welcome\n   " + t);
                persist();
                return;
            } else if (cmd.getType() == ParsedCommand.Type.DELETE) {
                taskManager.deleteTask(taskIndex);
                ui.sendMessage(String.format("Removing tasks on your list doesn't make it go away. Removed:\n    %s",
                        t));
                persist();
                return;
            }
            break;

        case TODO:
            if (cmd.getArg1() == null || cmd.getArg1().trim().isEmpty()) {
                throw new EmptyTaskException("todo");
            }
            ToDo newToDo = taskManager.addToDo(cmd.getArg1().trim());
            ui.showAdded(newToDo, taskManager);
            persist();
            return;

        case DEADLINE:
            if (cmd.getArg1() == null || cmd.getArg1().trim().isEmpty()) {
                throw new EmptyTaskException("deadline");
            }
            if (cmd.getDeadline() == null) {
                throw new EmptyDeadlineTimeException();
            }
            Deadline newDeadline = taskManager.addDeadline(cmd.getArg1().trim(), cmd.getDeadline());
            ui.showDeadline(newDeadline, taskManager);
            persist();
            return;

        case EVENT:
            if (cmd.getArg1() == null || cmd.getArg1().trim().isEmpty()) {
                throw new EmptyTaskException("event");
            }
            if (cmd.getArg2() == null || cmd.getArg2().trim().isEmpty()) {
                throw new EmptyEventTimeException();
            }
            if (cmd.getArg3() == null || cmd.getArg3().trim().isEmpty()) {
                throw new EmptyEventTimeException();
            }
            Events newEvent = taskManager.addEvents(cmd.getArg1().trim(), cmd.getArg2().trim(), cmd.getArg3().trim());
            ui.showEvent(newEvent, taskManager);
            persist();
            return;

        default:
            throw new InvalidCommandException(cmd.getType().toString());
        }
    }

    /**
     * Displays goodbye message to user after "bye" input
     */
    private void sayGoodbye() {
        ui.sayGoodbye();
    }

    /**
     * Saves the current list of tasks to persistent storage.
     * If saving fails, displays an error message to the user.
     */
    private void persist() {
        try {
            storage.save(new ArrayList<>(taskManager.getTasks()));
        } catch (IOException e) {
            ui.sendMessage("Failed to save tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Bingy bot = new Bingy();
        bot.run();
    }
}
