package mario.commands;

import mario.exceptions.MarioException;
import mario.util.Storage;
import mario.util.TaskManager;
import mario.util.Ui;

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
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws MarioException {
        return ui.showTasks(tasks.getTasks());
    }
}
