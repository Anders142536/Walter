package Walter.Parsers;

import Walter.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OptionTests {

    private StringOption t;

    @BeforeEach
    public void resetTests() {
        t = new StringOption(new String[] {"name e", "name d"}, null);
    }

    @Test
    public void correctStartup() {
        assertTrue(t.isRequired());
        assertFalse(t.hasValue());

        assertEquals("name e", t.getName(Language.ENGLISH));
        assertEquals("name d", t.getName(Language.GERMAN));

        //Tests the fallback to default descriptions if none are given
        assertEquals("No description", t.getDescription(Language.ENGLISH));
        assertEquals("Keine Beschreibung", t.getDescription(Language.GERMAN));
    }
}
