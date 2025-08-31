package bingy.util;

import bingy.tasks.Task;
import bingy.tasks.ToDo;
import bingy.tasks.Deadline;
import bingy.tasks.Events;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TaskManager encapsulates the list of tasks and provides operations to mutate it.
 */
public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();

    public TaskManager(int size) {
        // size ignored but kept to preserve existing constructor signature
    }

    public void markDone(int index) {
        tasks.get(index).markDone();
    }

    public void markUndone(int index) {
        tasks.get(index).markUndone();
    }

    public void deleteTask(int index) {
        tasks.remove(index);
    }

    public ToDo addToDo(String description) {
        ToDo task = new ToDo(description);
        tasks.add(task);
        return task;
    }

    public Deadline addDeadline(String description, LocalDate deadline) {
        Deadline task = new Deadline(description, deadline);
        tasks.add(task);
        return task;
    }

    public Events addEvents(String description, String start, String end) {
        Events task = new Events(description, start, end);
        tasks.add(task);
        return task;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public int getSize() {
        return tasks.size();
    }

    public void addAll(List<Task> items) {
        if (items != null) {
            tasks.addAll(items);
        }
    }
}
