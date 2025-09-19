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
 * Represents a command to mark a task as done.
 * When executed, it marks the specified task in the task list as completed,
 * saves the updated task list to storage, and returns a confirmation message.
 */
public class MarkCommand implements Command {
    private final int index;

    /**
     * Constructs a MarkCommand with a one-based task index.
     * The index is converted to zero-based for internal use.
     *
     * @param oneBasedIndex the one-based index of the task to mark as done
     */
    public MarkCommand(int oneBasedIndex) {
        this.index = oneBasedIndex - 1;
    }

    @Override
    public Type getType() {
        return Type.MARK;
    }

    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws BingyException {
        var list = tasks.getTasks();
        if (index < 0 || index >= list.size()) {
            throw new InvalidTaskIndexException(
                String.format("Index %d is out of bounds (valid range: 1 to %d)", index + 1, list.size()));
        }
        Task t = list.get(index);
        tasks.markDone(index);
        try {
            storage.save(new ArrayList<>(tasks.getTasks()));
        } catch (IOException e) {
            throw new BingyException("Couldn't save tasks after marking.");
        }
        return ui.sendMessage("Nice! I've marked this task as done:\n  " + t);
    }
}
