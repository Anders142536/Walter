package Walter.Settings;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringSettingTests {
    private StringSetting t;

    @BeforeEach
    public void resetStringSetting() {
        t = new StringSetting();
    }

    @Test
    public void correctStartup() {
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void getValue() {
        t.setValue("teststring");

        assertTrue(t.hasValue());
        assertEquals("teststring", t.getValue());
        assertEquals("teststring", t.getValueString());
    }
}
