package bingy.commands;

import java.time.LocalDate;
import java.util.List;

import bingy.exceptions.BingyException;
import bingy.tasks.TimedTask;
import bingy.util.Storage;
import bingy.util.TaskManager;
import bingy.util.Ui;

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
    public String execute(TaskManager tasks, Storage storage, Ui ui) throws BingyException {
        List<TimedTask> schedule = tasks.getScheduleFor(date);
        return ui.showSchedule(schedule, date);
    }
}
