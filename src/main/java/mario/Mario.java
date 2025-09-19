package mario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import mario.commands.Command;
import mario.exceptions.MarioException;
import mario.tasks.Task;
import mario.util.Parser;
import mario.util.Storage;
import mario.util.TaskManager;
import mario.util.Ui;

/**
 * Represents the main entry point for the Bingy chatbot application.
 * It coordinates user input, parsing, task management, and UI.
 */
public class Mario {
    /** Default maximum number of tasks the TaskManager can hold. */
    private static final int DEFAULT_CAPACITY = 100;

    /** Default storage file used to persist tasks. */
    private static final String DEFAULT_STORAGE_FILE = "tasks.txt";

    private static final TaskManager taskManager = new TaskManager(DEFAULT_CAPACITY);
    private static final Storage storage = new Storage(DEFAULT_STORAGE_FILE);
    private final Ui ui = new Ui();
    private boolean isRunning = true;
    private Command.Type commandType = Command.Type.UNKNOWN;

    public Mario() {
        try {
            ArrayList<Task> loaded = storage.load();
            assert loaded != null : "Storage.load() should never return null";
            taskManager.addAll(loaded);
        } catch (IOException e) {
            System.out.println(ui.sendMessage("Starting fresh (no saved tasks found)."));
        }
    }

    /**
     * Starts the main program loop.
     * Loads tasks from storage, greets the user, and continuously reads input
     * until the user exits.
     */
    public void run() {
        System.out.println(ui.greet());
        try {
            ArrayList<Task> loaded = storage.load();

            assert loaded != null : "Storage.load() should never return null";

            taskManager.addAll(loaded);
        } catch (IOException e) {
            System.out.println(ui.sendMessage("Starting fresh (no saved tasks found)."));
        }
        Scanner sc = new Scanner(System.in);
        while (isRunning) {
            String input = sc.nextLine();
            String response = getResponse(input);
            System.out.println(response);
        }
        sc.close();
    }


    /**
     * Calls on {@link Parser} to parse user input and executes {@link Command},
     * generating a response to be returned.
     *
     * @param input raw user input.
     * @return a response String.
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input);
            this.commandType = c.getType();
            String out = c.execute(taskManager, storage, ui);

            if (c.isExit()) {
                this.isRunning = false;
            }

            return out;
        } catch (MarioException e) {
            return ui.sendMessage(e.getMessage());
        }
    }

    /**
     * Returns the type of the last successfully parsed command as a String.
     */
    public Command.Type getCommandType() {
        return this.commandType;
    }

    public static void main(String[] args) {
        Mario bot = new Mario();
        bot.run();
    }
}
