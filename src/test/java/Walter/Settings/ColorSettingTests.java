package Walter.Settings;

import Walter.exceptions.ReasonedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class ColorSettingTests {
    ColorSetting t;

    @BeforeEach
    public void resetSetting() { t = new ColorSetting(); }

    @Test
    public void correctStartup() {
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValue() {
        Color test = new Color(0, 0, 255);
        assertDoesNotThrow(() -> t.setValue("#0000fF"));

        assertTrue(t.hasValue());
        assertEquals(test, t.getValue());
        assertEquals("#0000ff", t.getValueString());
    }

    @Test
    public void setInvalidValueAlphaChannel() {
        assertThrows(ReasonedException.class, () -> t.setValue("#0000aaff"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setInvalidValueNoHash() {
        assertThrows(ReasonedException.class, () -> t.setValue("0000aa"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setInvalidValueNotHex() {
        assertThrows(ReasonedException.class, () -> t.setValue("#0G00aa"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void resetSettingNull() {
        assertDoesNotThrow(() -> t.setValue("#002211"));
        assertDoesNotThrow(() -> t.setValue(null));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }
}
