package Walter.Parsers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StringOptionTests {
    private StringOption t;

    @BeforeEach
    public void resetTests() {
        t = new StringOption(new String[] {"name e", "name d"}, null);
    }

    @Test
    public void correctStartup() {
        assertEquals(OptionType.STRING, t.getType());
        assertFalse(t.hasValue());
        assertNull(t.getValue());
    }

    @Test
    public void setValue() {
        t.setValue("test");

        assertTrue(t.hasValue());
        assertEquals("test", t.getValue());
    }

    @Test
    public void reset() {
        t.setValue("test");
        t.reset();

        assertFalse(t.hasValue());
        assertNull(t.getValue());
    }
}
