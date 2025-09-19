package bingy.commands;

import java.io.IOException;
import java.util.ArrayList;

import bingy.exceptions.BingyException;
import bingy.exceptions.EmptyTaskException;
import bingy.tasks.ToDo;
import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;


/**
 * Represents a command to add a ToDo task to the task manager.
 * Validates the task description, adds the ToDo task,
 * saves the updated task list to storage, and returns a confirmation message.
 */
public class TodoCommand implements Command {
    private final String description;

    /**
     * Constructs a TodoCommand with the specified task description.
     *
     * @param description The description of the ToDo task to be added.
     */
    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public Type getType() {
        return Type.TODO;
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
