package bingy.commands;

import bingy.exceptions.BingyException;
import bingy.exceptions.EmptyTaskException;
import bingy.tasks.ToDo;
import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;
import java.io.IOException;
import java.util.ArrayList;

public class TodoCommand implements Command {
    private final String description;

    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws BingyException {
        if (description == null || description.isBlank()) {
            throw new EmptyTaskException("todo");
        }
        ToDo todo = tasks.addToDo(description.trim());
        try {
            storage.save(new ArrayList<>(tasks.getTasks()));
        } catch (IOException e) {
            throw new BingyException("Couldn't save tasks after adding todo.");
        }
        return ui.showAdded(todo, tasks);
    }
}
