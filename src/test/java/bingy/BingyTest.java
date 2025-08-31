package bingy;

import bingy.tasks.ToDo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BingyTest {

    @Test
    void todo_toString_showsTypeAndDescription_initiallyUndone() {
        ToDo t = new ToDo("buy milk");
        String s = t.toString();
        assertTrue(s.startsWith("[T]"), "ToDo should start with [T]");
        assertTrue(s.contains("buy milk"), "Description should be present");
        assertTrue(s.contains("[ ]") || s.contains("[\u0020]"), "Initial status icon should be undone");
    }

    @Test
    void todo_markAndUnmark_updatesStatusIcon() {
        ToDo t = new ToDo("read book");
        String s1 = t.toString();
        assertTrue(s1.contains("[ ]"), "Should start as undone");

        t.markDone();
        String s2 = t.toString();
        assertTrue(s2.contains("[X]"), "After markDone(), status should be [X]");

        t.markUndone();
        String s3 = t.toString();
        assertTrue(s3.contains("[ ]"), "After markUndone(), status should be [ ]");
    }
}
