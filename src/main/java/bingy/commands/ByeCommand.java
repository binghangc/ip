package bingy.commands;

import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;

public class ByeCommand implements Command {
    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) {
        return ui.sayGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}