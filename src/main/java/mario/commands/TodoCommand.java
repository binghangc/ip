package mario.commands;

import java.io.IOException;
import java.util.ArrayList;

import mario.exceptions.MarioException;
import mario.exceptions.EmptyTaskException;
import mario.tasks.ToDo;
import mario.util.Storage;
import mario.util.TaskManager;
import mario.util.Ui;


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
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws MarioException {
        if (description == null || description.isBlank()) {
            throw new EmptyTaskException("todo");
        }
        ToDo todo = tasks.addToDo(description.trim());
        try {
            storage.save(new ArrayList<>(tasks.getTasks()));
        } catch (IOException e) {
            throw new MarioException("Couldn't save tasks after adding todo.");
        }
        return ui.showAdded(todo, tasks);
    }
}
