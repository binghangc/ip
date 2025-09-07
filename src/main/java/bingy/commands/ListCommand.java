package bingy.commands;

import bingy.exceptions.BingyException;
import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;

public class ListCommand implements Command {

    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws BingyException {
        return ui.showTasks(tasks.getTasks());
    }
}
