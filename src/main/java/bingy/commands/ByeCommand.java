package bingy.commands;

import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;

/**
 * Represents the command to exit the application.
 * When executed, it returns a goodbye message and signals the application to terminate.
 */
public class ByeCommand implements Command {
    /**
     * Executes the bye command, returning a goodbye message to the user.
     *
     * @param tasks the task manager containing current tasks
     * @param storage the storage handler for saving tasks
     * @param ui the user interface handler for generating messages
     * @return a string containing the goodbye message
     */
    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) {
        return ui.sayGoodbye();
    }


    @Override
    public Type getType() {
        return Type.BYE;
    }


    @Override
    public boolean isExit() {
        return true;
    }
}
