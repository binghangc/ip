package bingy.commands;

import bingy.exceptions.BingyException;
import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;

public interface Command {
    /**
     * Executes the command and returns a string response to show the user.
     */
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws bingy.exceptions.BingyException;

    /**
     * Returns true if this command should cause the application to exit.
     */
    default boolean isExit() {
        return false;
    }
}
