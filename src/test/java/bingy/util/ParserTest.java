package bingy.util;

/*
 * AI-ASSISTED NOTE (A-AiAssisted):
 * The tests in this file were created with help from ChatGPT.
 */

import static org.junit.jupiter.api.Assertions.*;

import bingy.commands.DeadlineCommand;
import bingy.commands.EventCommand;
import bingy.commands.ViewCommand;
import bingy.exceptions.BingyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParserTest {

    private final Parser parser = new Parser();

    @Test
    @DisplayName("deadline: parses ISO date yyyy-MM-dd")
    void deadline_parsesIsoDate() throws Exception {
        assertParsesTo("deadline return book /by 2025-09-25", DeadlineCommand.class);
    }

    @Test
    @DisplayName("deadline: missing /by payload throws")
    void deadline_missingBy_throws() {
        assertThrows(BingyException.class, () -> parser.parse("deadline just text no by"));
        assertThrows(BingyException.class, () -> parser.parse("deadline desc /by   "));
    }

    @Test
    @DisplayName("event: parses ISO datetimes for /from and /to")
    void event_parsesIsoDateTimes() throws Exception {
        assertParsesTo("event party /from 2025-09-25T18:00 /to 2025-09-25T21:00", EventCommand.class);
    }


    @Test
    @DisplayName("view: defaults to today when no date specified")
    void view_defaultsToToday() throws Exception {
        assertParsesTo("view", ViewCommand.class);
    }

    @Test
    @DisplayName("view: accepts explicit ISO date")
    void view_acceptsExplicitDate() throws Exception {
        assertParsesTo("view 2025-09-25", ViewCommand.class);
    }

    // --- helper ---
    private void assertParsesTo(String input, Class<?> expectedClass) throws Exception {
        var cmd = parser.parse(input);
        assertNotNull(cmd, "Parser returned null for: " + input);
        assertTrue(expectedClass.isInstance(cmd),
                () -> "Expected " + expectedClass.getSimpleName() + " but was " + cmd.getClass().getSimpleName());
    }
}
