package Walter.Parsers;

import Walter.exceptions.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntegerOptionTest {
    private IntegerOption t;

    @BeforeEach
    public void resetTests() {
        t = new IntegerOption(new String[] {"name e", "name d"}, null);
    }

    @Test
    public void correctStartup() {
        assertEquals(OptionType.INT, t.getType());
        assertFalse(t.hasValue());
        assertNull(t.getValue());
    }

    @Test
    public void setValue() {
        assertDoesNotThrow(() -> t.setValue("1"));

        assertTrue(t.hasValue());
        assertEquals(1, t.getValue());
    }

    @Test
    public void setIncorrectValue() {
        assertThrows(ParseException.class, () -> t.setValue("a"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
    }

    @Test
    public void reset() {
        assertDoesNotThrow(() -> t.setValue("1"));
        t.reset();

        assertFalse(t.hasValue());
        assertNull(t.getValue());

    }
}
