package bingy.commands;

import bingy.exceptions.BingyException;
import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;

/**
 * Represents the command to list all tasks.
 * When executed, it retrieves all tasks from the TaskManager
 * and returns a formatted string of these tasks using the Ui.
 */
public class ListCommand implements Command {


    @Override
    public Type getType() {
        return Type.LIST;
    }
    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws BingyException {
        return ui.showTasks(tasks.getTasks());
    }
}
