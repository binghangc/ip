package bingy.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bingy.tasks.Deadline;
import bingy.tasks.Events;
import bingy.tasks.Task;
import bingy.tasks.ToDo;


/**
 * TaskManager encapsulates the list of tasks and provides operations to mutate it.
 */
public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();

    public TaskManager(int size) {
        // size ignored but kept to preserve existing constructor signature
    }

    /**
     * Marks the task at the given index as complete.
     *
     * @param index the position of the {@link bingy.tasks.Task} in the list (0-based).
     */
    public void markDone(int index) {
        tasks.get(index).markDone();
    }

    /**
     * Marks the task at the given index as incomplete
     *
     * @param index the position of the {@link bingy.tasks.Task} in the list (0-based).
     */
    public void markUndone(int index) {
        tasks.get(index).markUndone();
    }

    public void deleteTask(int index) {
        tasks.remove(index);
    }

    /**
     * Creates and adds a new {@link bingy.tasks.ToDo} to the task list.
     *
     * @param description the description of the to-do task.
     * @return the {@link bingy.tasks.ToDo} that was created and added.
     */
    public ToDo addToDo(String description) {
        ToDo task = new ToDo(description);
        tasks.add(task);
        return task;
    }

    /**
     * Creates and adds a new {@link bingy.tasks.Deadline} to the task list.
     *
     * @param description the description of the deadline task.
     * @param deadline the {@link java.time.LocalDate} by which the task is due.
     * @return the {@link bingy.tasks.Deadline} that was created and added.
     */
    public Deadline addDeadline(String description, LocalDate deadline) {
        Deadline task = new Deadline(description, deadline);
        tasks.add(task);
        return task;
    }

    /**
     * Creates and adds a new {@link bingy.tasks.Events} to the task list.
     *
     * @param description the description of the event task.
     * @param start the start time of the event.
     * @param end the end time of the event.
     * @return the {@link bingy.tasks.Events} that was created and added.
     */
    public Events addEvent(String description, String start, String end) {
        Events task = new Events(description, start, end);
        tasks.add(task);
        return task;
    }

    /**
     * Returns an unmodifiable view of the current tasks.
     *
     * @return an unmodifiable {@link java.util.List} of {@link bingy.tasks.Task}.
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return the total number of tasks.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Searches for tasks whose description contains the given keyword.
     * The search is case-insensitive.
     *
     * @param keyword the string to search for in task descriptions.
     * @return a list of {@link bingy.tasks.Task} whose descriptions contain the keyword.
     */
    public List<Task> find(String keyword) {
        String needle = keyword.toLowerCase();
        List<Task> matches = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getDescription().toLowerCase().contains(needle)) {
                matches.add(t);
            }
        }
        return matches;
    }

    /**
     * Adds all tasks from the given list into the task manager.
     *
     * @param items the list of {@link bingy.tasks.Task} to add; ignored if null.
     */
    public void addAll(List<Task> items) {
        if (items != null) {
            tasks.addAll(items);
        }
    }
}
