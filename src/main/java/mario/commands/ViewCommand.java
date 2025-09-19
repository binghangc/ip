package mario.commands;

import java.time.LocalDate;
import java.util.List;

import mario.exceptions.MarioException;
import mario.tasks.TimedTask;
import mario.util.Storage;
import mario.util.TaskManager;
import mario.util.Ui;

/**
 * Represents a command to view schedule at a particular date (default today)
 */
public class ViewCommand implements Command {
    private final LocalDate date;

    public ViewCommand(LocalDate date) {
        this.date = date;
    }
    @Override
    public Type getType() {
        return Type.VIEW;
    }

    @Override
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws MarioException {
        List<TimedTask> schedule = tasks.getScheduleFor(date);
        return ui.showSchedule(schedule, date);
    }
}
