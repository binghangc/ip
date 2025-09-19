package mario.commands;

import mario.exceptions.MarioException;
import mario.exceptions.EmptyDeadlineTimeException;
import mario.exceptions.EmptyTaskException;
import mario.tasks.Deadline;
import mario.util.Storage;
import mario.util.TaskManager;
import mario.util.Ui;
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

    public LocalDate getDeadline() {
        return this.byTime;
    }

    @Override
    public Type getType() {
        return Type.DEADLINE;
    }

    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws MarioException {
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
            throw new MarioException("Couldn't save tasks after adding deadline.");
        }
        return ui.showDeadline(deadline, tasks);
    }
}
