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
        assertEquals("Undefined", t.getValue());
    }

    @Test
    public void getValue() {
        t.setValue("teststring");

        assertEquals("teststring", t.getValue());
    }
}
