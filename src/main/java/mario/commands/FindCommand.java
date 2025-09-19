package mario.commands;

import java.util.List;

import mario.exceptions.MarioException;
import mario.tasks.Task;
import mario.util.Storage;
import mario.util.TaskManager;
import mario.util.Ui;

/**
 * Represents a command that searches for tasks containing a specified keyword.
 */
public class FindCommand implements Command {
    private final String keyword;

    /**
     * Constructs a FindCommand with the given keyword to search for.
     *
     * @param keyword the keyword to search tasks for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public Type getType() {
        return Type.FIND;
    }

    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws MarioException {
        List<Task> result = tasks.find(keyword);
        if (result.isEmpty()) {
            return ui.sendMessage("No matching tasks found for: " + keyword);
        } else {
            return ui.showMatches(result);
        }
    }
}
