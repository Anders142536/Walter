package Walter.Settings;

import Walter.exceptions.ReasonedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BoolSettingTests {
    private BoolSetting t;

    private Random random = new Random();

    @BeforeEach
    public void resetBoolSetting() {
        t = new BoolSetting();
    }

    @Test
    public void correctStartup() {
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValueBoolean() {
        //fuzzy
        for (int i = 0; i < 1000; i++) {
            final boolean temp = random.nextBoolean();
            assertDoesNotThrow(() -> t.setValue(temp));
            assertTrue(t.hasValue());
            assertEquals(temp, t.getValue());
            assertEquals(String.valueOf(temp), t.getValueString());
        }
    }

    @Test
    public void setValueTrueString() {
        assertDoesNotThrow(() -> t.setValue("true"));
        assertTrue(t.hasValue());
        assertTrue(t.getValue());
        assertEquals("true", t.getValueString());
    }

    @Test
    public void setValueFalseString() {
        assertDoesNotThrow(() -> t.setValue("false"));
        assertTrue(t.hasValue());
        assertFalse(t.getValue());
        assertEquals("false", t.getValueString());
    }

    @Test
    public void setInvalidStringValue() {
        assertThrows(ReasonedException.class, () -> t.setValue("true "));
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

}
