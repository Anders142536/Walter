package Walter.Parsers;

import Walter.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DateTimeOptionTests {
    DateTimeOption t;

    @BeforeEach
    public void resetTests() {
        t = new DateTimeOption(new String[] {"TestDateTimeOption"}, new String[] {"Testdescription"});
    }

    @Test
    public void correctStartup() {
        assertFalse(t.hasValue());
        assertNull(t.getValue());
    }

    @Test
    public void setValueFull() {
        assertDoesNotThrow(() -> t.setValue("11/11/2020 20:12:12"));
        assertTrue(t.hasValue());
        assertEquals("11/11/2020 20:12:12", t.getValue().format(Config.dateFormat));
    }

    @Test
    public void setValueFullWithDots() {
        assertDoesNotThrow(() -> t.setValue("11.11.2020 20:12:12"));
        assertTrue(t.hasValue());
        assertEquals("11/11/2020 20:12:12", t.getValue().format(Config.dateFormat));
    }

    @Test
    public void setValueNoTime() {
        assertDoesNotThrow(() -> t.setValue("11/11/2020"));
        assertTrue(t.hasValue());
        assertEquals("11/11/2020 00:00:00", t.getValue().format(Config.dateFormat));
    }

    @Test
    public void setValueNoSeconds() {
        assertDoesNotThrow(() -> t.setValue("11/11/2020 13:37"));
        assertTrue(t.hasValue());
        assertEquals("11/11/2020 13:37:00", t.getValue().format(Config.dateFormat));
    }
}
