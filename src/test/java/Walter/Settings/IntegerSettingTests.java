package Walter.Settings;

import Walter.exceptions.ReasonedException;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class IntegerSettingTests {
    private IntegerSetting t;
    private Random random = new Random();

    private void resetIntegerSetting() {
        t = new IntegerSetting();
    }

    private void resetIntegerSetting(int max, int min) {
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

        //fuzzy
        for (int i = 0; i < 1000; i++) {
            final int temp = random.nextInt();
            assertDoesNotThrow(() -> t.setDefault(temp));
            assertEquals(temp, t.getValue());
        }
    }

    @Test
    public void setDefaultParameterized() {
        resetIntegerSetting(5, 0);

        assertDoesNotThrow(() -> t.setDefault(2));
        assertEquals(2, t.getValue());
    }

    @Test
    public void setDefaultParameterizedWithNoGap() {
        resetIntegerSetting(0, 0);

        assertDoesNotThrow(() -> t.setDefault(0));
        assertEquals(0, t.getValue());
    }

    @Test
    public void setWrongParameters() {
        assertThrows(AssertionError.class, () -> resetIntegerSetting(-5, 5));
        assertNull(t);
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

    @Test
    public void setTooLowDefault() {
        resetIntegerSetting(5, -5);

        assertThrows(ReasonedException.class, () -> t.setDefault(-6));
        assertThrows(AssertionError.class, () -> t.getValue());
    }

    @Test
    public void setTooHighDefault() {
        resetIntegerSetting(5, -5);

        assertThrows(ReasonedException.class, () -> t.setDefault(6));
        assertThrows(AssertionError.class, () -> t.getValue());
    }

    @Test
    public void setValueParameterless() {
        resetIntegerSetting();

        //fuzzy
        for (int i = 0; i < 1000; i++) {
            int temp = random.nextInt();
            assertDoesNotThrow(() -> t.setValue(temp + ""));
            assertEquals(temp, t.getValue());
        }
    }

    @Test
    public void setValueParameterized() {
        resetIntegerSetting(5, -5);

        assertDoesNotThrow(() -> t.setValue("1"));
        assertEquals(1, t.getValue());
    }

    @Test
    public void setMinLimitValueParameterless() {
        resetIntegerSetting();

        assertDoesNotThrow(() -> t.setValue(Integer.MIN_VALUE + ""));
        assertEquals(Integer.MIN_VALUE, t.getValue());
    }

    @Test
    public void setMaxLimitValueParameterless() {
        resetIntegerSetting();

        assertDoesNotThrow(() -> t.setValue(Integer.MAX_VALUE + ""));
        assertEquals(Integer.MAX_VALUE, t.getValue());
    }

    @Test
    public void setMinLimitValueParameterized() {
        resetIntegerSetting(501, -3);

        assertDoesNotThrow(() -> t.setValue("-3"));
        assertEquals(-3, t.getValue());
    }

    @Test
    public void setMaxLimitValueParameterized() {
        resetIntegerSetting(3, -239);

        assertDoesNotThrow(() -> t.setValue("3"));
        assertEquals(3, t.getValue());
    }

    @Test
    public void setTooLowValue() {
        resetIntegerSetting(234, 1);

        assertThrows(ReasonedException.class, () -> t.setValue("0"));
        assertThrows(AssertionError.class, () -> t.getValue());
    }

    @Test
    public void setTooHighValue() {
        resetIntegerSetting(-1, -54);

        assertThrows(ReasonedException.class, () -> t.setValue("83"));
        assertThrows(AssertionError.class, () -> t.getValue());
    }

    @Test
    public void getValueIfValueAndDefaultAreGiven() {
        resetIntegerSetting();

        //fuzzy
        for (int i = 0; i < 1000; i++) {
            int temp = random.nextInt();
            assertDoesNotThrow(() -> t.setDefault(random.nextInt()));
            assertDoesNotThrow(() -> t.setValue(temp + ""));
            assertEquals(temp, t.getValue());
        }
    }
}
