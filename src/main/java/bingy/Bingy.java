package bingy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import bingy.commands.Command;
import bingy.exceptions.BingyException;
import bingy.tasks.Task;
import bingy.util.Parser;
import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;

/**
 * Represents the main entry point for the Bingy chatbot application.
 * It coordinates user input, parsing, task management, and UI.
 */
public class Bingy {
    /** Default maximum number of tasks the TaskManager can hold. */
    private static final int DEFAULT_CAPACITY = 100;

    /** Default storage file used to persist tasks. */
    private static final String DEFAULT_STORAGE_FILE = "tasks.txt";

    private static final TaskManager taskManager = new TaskManager(DEFAULT_CAPACITY);
    private static final Storage storage = new Storage(DEFAULT_STORAGE_FILE);
    private final Ui ui = new Ui();
    private boolean isRunning = true;
    private String commandType = "UNKNOWN";


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
            this.commandType = c.getClass().getSimpleName();
            String out = c.execute(taskManager, storage, ui);

            if (c.isExit()) {
                this.isRunning = false;
            }

            return out;
        } catch (BingyException e) {
            return ui.sendMessage(e.getMessage());
        }
    }

    /**
     * Returns the type of the last successfully parsed command as a String.
     */
    public String getCommandType() {
        return this.commandType;
    }

    public static void main(String[] args) {
        Bingy bot = new Bingy();
        bot.run();
    }
}
