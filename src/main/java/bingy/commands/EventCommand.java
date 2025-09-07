package bingy.commands;

import bingy.exceptions.BingyException;
import bingy.exceptions.EmptyTaskException;
import bingy.exceptions.EmptyEventTimeException;
import bingy.tasks.Events;
import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;
import java.io.IOException;
import java.util.ArrayList;

public class EventCommand implements Command {
    private final String description;
    private final String fromTime;
    private final String toTime;

    public EventCommand(String description, String fromTime, String toTime) {
        this.description = description;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws BingyException {
        if (description == null || description.trim().isEmpty()) {
            throw new EmptyTaskException("event");
        }
        if (fromTime == null || toTime == null) {
            throw new EmptyEventTimeException();
        }
        Events event = tasks.addEvent(description.trim(), fromTime, toTime);
        try {
            storage.save(new ArrayList<>(tasks.getTasks()));
        } catch (IOException e) {
            throw new BingyException("Failed to save tasks: " + e.getMessage());
        }
        return ui.showEvent(event, tasks);
    }
}
