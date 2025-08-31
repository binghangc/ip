package bingy.util;

import bingy.tasks.Task;
import bingy.tasks.ToDo;
import bingy.tasks.Deadline;
import bingy.tasks.Events;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class TaskManagerTest {

    @Test
    void addToDo_increasesSizeAndStoresTask() {
        TaskManager manager = new TaskManager(10);
        ToDo todo = manager.addToDo("buy milk");

        assertEquals(1, manager.getSize());
        assertInstanceOf(ToDo.class, manager.getTasks().get(0));
        assertEquals("buy milk", ((ToDo) manager.getTasks().get(0)).getDescription());
    }

    @Test
    void addDeadline_storesDeadlineWithCorrectDate() {
        TaskManager manager = new TaskManager(10);
        LocalDate date = LocalDate.of(2025, 12, 31);
        Deadline deadline = manager.addDeadline("submit report", date);

        assertEquals(1, manager.getSize());
        assertInstanceOf(Deadline.class, manager.getTasks().get(0));
        assertTrue(deadline.toString().contains("submit report"));
        assertTrue(deadline.toString().contains("2025"));
    }

    @Test
    void addEvents_storesEventWithCorrectFields() {
        TaskManager manager = new TaskManager(10);
        Events e = manager.addEvents("party", "2pm", "5pm");

        assertEquals(1, manager.getSize());
        assertInstanceOf(Events.class, manager.getTasks().get(0));
        assertTrue(e.toString().contains("party"));
        assertTrue(e.toString().contains("2pm"));
        assertTrue(e.toString().contains("5pm"));
    }

    @Test
    void markDoneAndUndone_changesTaskStatus() {
        TaskManager manager = new TaskManager(10);
        manager.addToDo("read book");

        assertTrue(manager.getTasks().get(0).toString().contains("[ ]"));
        manager.markDone(0);
        assertTrue(manager.getTasks().get(0).toString().contains("[X]"));
        manager.markUndone(0);
        assertTrue(manager.getTasks().get(0).toString().contains("[ ]"));
    }

    @Test
    void deleteTask_removesTask() {
        TaskManager manager = new TaskManager(10);
        manager.addToDo("task 1");
        manager.addToDo("task 2");

        assertEquals(2, manager.getSize());
        manager.deleteTask(0);
        assertEquals(1, manager.getSize());
        assertTrue(manager.getTasks().get(0).toString().contains("task 2"));
    }

    @Test
    void addAll_addsMultipleTasks() {
        TaskManager manager = new TaskManager(10);
        ToDo t1 = new ToDo("milk");
        ToDo t2 = new ToDo("bread");
        manager.addAll(java.util.List.of(t1, t2));

        assertEquals(2, manager.getSize());
        assertInstanceOf(ToDo.class, manager.getTasks().get(0));
        assertInstanceOf(ToDo.class, manager.getTasks().get(1));
    }
}
