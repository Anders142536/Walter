package Walter.Parsers;

import Walter.exceptions.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FlagTests {

    private Flag t;
    StringOption param = new StringOption("name e", "name d", "des e", "des d");

    public void resetFlag() {
        resetFlag(null);
    }
    public void resetFlag(Option parameter) {
        t = new Flag('t', "test", "des e", "des d", parameter);
    }

    @Test
    public void correctParameterlessStartup() {
        resetFlag();

        assertFalse(t.isGiven());
        assertFalse(t.hasParameter());
        assertNull(t.getParameter());
        assertEquals('t', t.getShortName());
        assertEquals("test", t.getLongName());
        assertEquals("-t, --test     des e", t.getDescriptionEnglish());
        assertEquals("-t, --test     des d", t.getDescriptionGerman());
    }

    @Test
    public void correctParameterizedStartup() {
        resetFlag(param);
        assertFalse(t.isGiven());
        assertTrue(t.hasParameter());
        assertSame(param, t.getParameter());
    }

    @Test
    public void given() {
        resetFlag();
        assertDoesNotThrow(() -> t.given());
        assertThrows(ParseException.class, () -> t.given());

        assertTrue(t.isGiven());
    }

    @Test
    public void reset() {
        resetFlag();
        t.reset();

        assertFalse(t.isGiven());
    }

    @Test
    public void resetOnGiven() {
        resetFlag();
        assertDoesNotThrow(() -> t.given());
        t.reset();

        assertFalse(t.isGiven());
    }

    @Test
    public void resetOnGivenParameter() {
        resetFlag(param);
        assertDoesNotThrow(() -> t.given());
        assertDoesNotThrow(() -> t.getParameter().setValue("test"));
        t.reset();

        assertFalse(t.isGiven());
        assertFalse(t.getParameter().hasValue());
    }

}
