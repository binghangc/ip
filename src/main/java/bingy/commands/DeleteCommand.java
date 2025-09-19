package bingy.commands;

import java.io.IOException;
import java.util.ArrayList;

import bingy.exceptions.BingyException;
import bingy.exceptions.InvalidTaskIndexException;
import bingy.tasks.Task;
import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;

/**
 * Represents a command to delete a task from the task list.
 * It handles the deletion of the task at the specified index,
 * updates the storage, and provides feedback via the UI.
 */
public class DeleteCommand implements Command {
    private final int index;


    public DeleteCommand(int oneBasedIndex) {
        this.index = oneBasedIndex - 1;
    }


    @Override
    public Type getType() {
        return Type.DELETE;
    }


    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws BingyException {
        var list = tasks.getTasks();
        if (index < 0 || index >= list.size()) {
            throw new InvalidTaskIndexException(
                String.format("Index %d is out of bounds (valid range: 1 to %d)", index + 1, list.size()));
        }
        Task removed = list.get(index);
        tasks.deleteTask(index);
        try {
            storage.save(new ArrayList<>(tasks.getTasks()));
        } catch (IOException e) {
            throw new BingyException("Couldn't save tasks after deleting.");
        }
        return ui.showDelete(removed, tasks);
    }
}
