package Walter.Settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SettingTests {
    Setting t;

    @BeforeEach
    public void resetSetting() {
        t = new IntegerSetting();
    }

    @Test
    public void correctStartup() {
        assertEquals("Unnamed", t.getName());
    }

    @Test
    public void setName() {
        t.setName("Testname");
        assertEquals("Testname", t.getName());
    }

    @Test
    public void setNameToNull() {
        t.setName(null);
        assertEquals("Unnamed", t.getName());
    }
}
