package Walter.Parsers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OptionTests {

    private StringOption t;

    @BeforeEach
    public void resetTests() {
        t = new StringOption("name e", "name d", "des e", "des d");
    }

    @Test
    public void correctStartup() {
        assertTrue(t.isRequired());
        assertFalse(t.hasValue());
        assertEquals("name e", t.getNameEnglish());
        assertEquals("name d", t.getNameGerman());
        assertEquals("name e         des e", t.getDescriptionEnglish());
        assertEquals("name d         des d", t.getDescriptionGerman());
    }
}
