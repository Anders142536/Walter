package Walter.Parsers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FlushOptionTests {
    private FlushOption t;

    @BeforeEach
    public void resetTests() {
        t = new FlushOption("name e", "name d", "des e", "des d");
    }

    @Test
    public void correctStartup(){
        assertEquals(OptionType.FLUSH, t.getType());
        assertFalse(t.hasValue());
        assertNull(t.getValue());
    }

    @Test
    public void setValue() {
        t.setValue("test flush input");

        assertTrue(t.hasValue());
        assertEquals("test flush input", t.getValue());
    }

    @Test
    public void reset() {
        t.setValue("test flush input");
        t.reset();

        assertFalse(t.hasValue());
        assertNull(t.getValue());
    }
}
