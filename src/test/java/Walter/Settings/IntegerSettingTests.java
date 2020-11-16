package Walter.Settings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntegerSettingTests {

    private IntegerSetting t;

    public void resetIntegerSetting() {
        t = new IntegerSetting();
    }

    public void resetIntegerSetting(int max, int min) {
        t = new IntegerSetting(max, min);
    }

    @Test
    public void correctParameterlessStartup() {
        resetIntegerSetting();

        assertThrows(AssertionError.class, () -> t.getValue());
    }

    @Test
    public void correctParameterizedStartup() {
        resetIntegerSetting(5, 0);

        assertThrows(AssertionError.class, () -> t.getValue());
    }

    @Test
    public void setDefaultParameterless() {
        resetIntegerSetting();

        assertDoesNotThrow(() -> t.setDefault(0));
        assertEquals(0, t.getValue());
    }

    @Test
    public void setDefaultParameterized() {
        resetIntegerSetting(5, 0);

        assertDoesNotThrow(() -> t.setDefault(2));
        assertEquals(2, t.getValue());
    }

    @Test
    public void setMinLimitDefaultParameterless() {
        resetIntegerSetting();

        assertDoesNotThrow(() -> t.setDefault(Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, t.getValue());
    }

    @Test
    public void setMaxLimitDefaultParameterless() {
        resetIntegerSetting();

        assertDoesNotThrow(() -> t.setDefault(Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, t.getValue());
    }

    @Test
    public void setMinLimitDefaultParameterized() {
        resetIntegerSetting(5, -5);

        assertDoesNotThrow(() -> t.setDefault(-5));
        assertEquals(-5, t.getValue());
    }

    @Test
    public void setMaxLimitDefaultParameterized() {
        resetIntegerSetting(5, -5);

        assertDoesNotThrow(() -> t.setDefault(5));
        assertEquals(5, t.getValue());
    }
}
