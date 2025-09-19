package mario.commands;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import mario.exceptions.MarioException;
import mario.exceptions.EmptyEventTimeException;
import mario.exceptions.EmptyTaskException;
import mario.tasks.Events;
import mario.util.Storage;
import mario.util.TaskManager;
import mario.util.Ui;



/**
 * Represents a command to add an event task with a description, start time, and end time.
 * This command, when executed, adds an event to the task manager and persists it to storage.
 */
public class EventCommand implements Command {
    /**
     * The description of the event task.
     */
    private final String description;
    /**
     * The starting time of the event.
     */
    private final LocalDateTime startTime;
    /**
     * The ending time of the event.
     */
    private final LocalDateTime endTime;

    /**
     * Constructs an EventCommand with the specified description, start time, and end time.
     *
     * @param description The description of the event.
     * @param startTime The starting time of the event.
     * @param endTime The ending time of the event.
     */
    public EventCommand(String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public Type getType() {
        return Type.EVENT;
    }

    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws MarioException {
        if (description == null || description.trim().isEmpty()) {
            throw new EmptyTaskException("event");
        }
        if (startTime == null || endTime == null) {
            throw new EmptyEventTimeException();
        }
        Events event = tasks.addEvent(description.trim(), startTime, endTime);
        try {
            storage.save(new ArrayList<>(tasks.getTasks()));
        } catch (IOException e) {
            throw new MarioException("Failed to save tasks: " + e.getMessage());
        }
        return ui.showEvent(event, tasks);
    }
}
