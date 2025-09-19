package mario.commands;

import mario.exceptions.MarioException;
import mario.util.Storage;
import mario.util.TaskManager;
import mario.util.Ui;

/**
 * Represents a command that can be executed by the Bingy application.
 * Each command performs a specific action on the task list and interacts with storage and UI components.
 */
public interface Command {
    /**
     * Executes the command and returns a string response to show the user.
     *
     * @param tasks the TaskManager managing the current list of tasks.
     * @param storage the Storage handler for saving and loading tasks.
     * @param ui the Ui component for interacting with the user.
     * @return a string response message to be displayed to the user.
     * @throws MarioException if an error occurs during command execution.
     */
    String execute(TaskManager tasks, Storage storage, Ui ui) throws MarioException;


    /**
     * Enum representing the different types of commands supported by the application.
     */
    enum Type {
        /**
         * Represents a command to add a todo task.
         */
        TODO,
        /**
         * Represents a command to add a deadline task.
         */
        DEADLINE,
        /**
         * Represents a command to add an event task.
         */
        EVENT,
        /**
         * Represents a command to mark a task as done.
         */
        MARK,
        /**
         * Represents a command to delete a task.
         */
        DELETE,
        /**
         * Represents a command to list all tasks.
         */
        LIST,
        /**
         * Represents a command to unmark a task as not done.
         */
        UNMARK,
        /**
         * Represents a command to exit the application.
         */
        BYE,
        /**
         * Represents a command to view schedule
         */
        VIEW,
        FIND,
        UNKNOWN,
    }

    /**
     * Returns the type of this command.
     *
     * @return the Type enum value corresponding to this command
     */
    Type getType();

    /**
     * Returns true if this command should cause the application to exit.
     * By default, this method returns false.
     *
     * @return true if the command signals the application to exit, false otherwise
     */
    default boolean isExit() {
        return false;
    }


}
