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
 * Represents a command to unmark a task as not done.
 * This command updates the task's status to undone and saves the changes.
 */
public class UnmarkCommand implements Command {
    private final int index;

    /**
     * Constructs an UnmarkCommand with the given one-based task index.
     * The index is converted to zero-based for internal use.
     *
     * @param oneBasedIndex The one-based index of the task to unmark.
     */
    public UnmarkCommand(int oneBasedIndex) {
        this.index = oneBasedIndex - 1;
    }

    @Override
    public Type getType() {
        return Type.UNMARK;
    }

    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws BingyException {
        var list = tasks.getTasks();
        if (index < 0 || index >= list.size()) {
            throw new InvalidTaskIndexException(
                String.format("Index %d is out of bounds (valid range: 1 to %d)", index + 1, list.size()));
        }
        Task t = list.get(index);
        tasks.markUndone(index);
        try {
            storage.save(new ArrayList<>(tasks.getTasks()));
        } catch (IOException e) {
            throw new BingyException("Couldn't save tasks after unmarking.");
        }
        return ui.sendMessage("OK, I've marked this task as not done yet:\n  " + t);
    }
}
