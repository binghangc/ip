package mario.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import mario.tasks.Deadline;
import mario.tasks.Events;
import mario.tasks.ToDo;

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
    void addEvent_storesEventWithCorrectFields() {
        TaskManager manager = new TaskManager(10);
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 17, 0);
        Events e = manager.addEvent("party", start, end);

        assertEquals(1, manager.getSize());
        assertInstanceOf(Events.class, manager.getTasks().get(0));

        // Verify description and times directly
        assertEquals("party", e.getDescription());
        assertEquals(start, e.getStart());
        assertEquals(end, e.getEnd());
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
