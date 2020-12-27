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
        t.setLowerLimit(-5);
        t.setUpperLimit(5);
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
    public void setValueWithoutLimits() {
        t = new IntegerOption(new String[] {"name e", "name d"}, null);

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
    public void setValueHigherThanLimit() {
        assertThrows(ParseException.class, () -> t.setValue("6"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
    }

    @Test
    public void setValueLowerThanLimit() {
        assertThrows(ParseException.class, () -> t.setValue("-6"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
    }

    @Test
    public void setUpperLimitAsValue() {
        assertDoesNotThrow(() -> t.setValue("5"));

        assertTrue(t.hasValue());
        assertEquals(5, t.getValue());
    }

    @Test
    public void setLowerLimitAsValue() {
        assertDoesNotThrow(() -> t.setValue("-5"));

        assertTrue(t.hasValue());
        assertEquals(-5, t.getValue());
    }

    @Test
    public void setValueHigherThanIntMaxValue() {
        assertThrows(ParseException.class, () -> t.setValue(Integer.MAX_VALUE + "0"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
    }

    @Test
    public void setValueLowerThanIntMaxValue() {
        assertThrows(ParseException.class, () -> t.setValue(Integer.MIN_VALUE + "0"));

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
