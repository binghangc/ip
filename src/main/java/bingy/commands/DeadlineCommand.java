package bingy.commands;

import bingy.exceptions.BingyException;
import bingy.exceptions.EmptyDeadlineTimeException;
import bingy.exceptions.EmptyTaskException;
import bingy.tasks.Deadline;
import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


public class DeadlineCommand implements Command {
    private final String description;
    private final LocalDate byTime;

    public DeadlineCommand(String description, LocalDate byTime) {
        this.description = description;
        this.byTime = byTime;
    }

    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws BingyException {
        if (this.description == null || this.description.isBlank()) {
            throw new EmptyTaskException("deadline");
        }
        if (this.byTime == null) {
            throw new EmptyDeadlineTimeException();
        }
        Deadline deadline = tasks.addDeadline(description.trim(), byTime);
        try {
            storage.save(new ArrayList<>(tasks.getTasks()));
        } catch (IOException e) {
            throw new BingyException("Couldn't save tasks after adding deadline.");
        }
        return ui.showDeadline(deadline, tasks);
    }
}
