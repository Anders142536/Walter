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

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void correctParameterizedStartup() {
        resetIntegerSetting(5, 0);

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValueUndefined() {
        resetIntegerSetting();

        assertDoesNotThrow(() -> t.setValue("Undefined"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setDefaultParameterless() {
        resetIntegerSetting();

        //fuzzy
        for (int i = 0; i < 1000; i++) {
            final int temp = random.nextInt();
            assertDoesNotThrow(() -> t.setDefault(temp));
            assertFalse(t.hasValue());
            assertEquals(temp, t.getValue());
            assertEquals(String.valueOf(temp), t.getValueString());
        }
    }

    @Test
    public void setDefaultParameterized() {
        resetIntegerSetting(5, 0);

        assertDoesNotThrow(() -> t.setDefault(2));
        assertFalse(t.hasValue());
        assertEquals(2, t.getValue());
        assertEquals("2", t.getValueString());
    }

    @Test
    public void setDefaultParameterizedWithNoGap() {
        resetIntegerSetting(0, 0);

        assertDoesNotThrow(() -> t.setDefault(0));
        assertFalse(t.hasValue());
        assertEquals(0, t.getValue());
        assertEquals("0", t.getValueString());
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
        assertFalse(t.hasValue());
        assertEquals(Integer.MIN_VALUE, t.getValue());
        assertEquals(String.valueOf(Integer.MIN_VALUE), t.getValueString());
    }

    @Test
    public void setMaxLimitDefaultParameterless() {
        resetIntegerSetting();

        assertDoesNotThrow(() -> t.setDefault(Integer.MAX_VALUE));
        assertFalse(t.hasValue());
        assertEquals(Integer.MAX_VALUE, t.getValue());
        assertEquals(String.valueOf(Integer.MAX_VALUE), t.getValueString());
    }

    @Test
    public void setMinLimitDefaultParameterized() {
        resetIntegerSetting(5, -5);

        assertDoesNotThrow(() -> t.setDefault(-5));
        assertFalse(t.hasValue());
        assertEquals(-5, t.getValue());
        assertEquals("-5", t.getValueString());
    }

    @Test
    public void setMaxLimitDefaultParameterized() {
        resetIntegerSetting(5, -5);

        assertDoesNotThrow(() -> t.setDefault(5));
        assertFalse(t.hasValue());
        assertEquals(5, t.getValue());
        assertEquals("5", t.getValueString());
    }

    @Test
    public void setTooLowDefault() {
        resetIntegerSetting(5, -5);

        assertThrows(ReasonedException.class, () -> t.setDefault(-6));
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setTooHighDefault() {
        resetIntegerSetting(5, -5);

        assertThrows(ReasonedException.class, () -> t.setDefault(6));
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValueParameterless() {
        resetIntegerSetting();

        //fuzzy
        for (int i = 0; i < 1000; i++) {
            int temp = random.nextInt();
            assertDoesNotThrow(() -> t.setValue(String.valueOf(temp)));
            assertTrue(t.hasValue());
            assertEquals(temp, t.getValue());
            assertEquals(String.valueOf(temp), t.getValueString());
        }
    }

    @Test
    public void setValueParameterized() {
        resetIntegerSetting(5, -5);

        assertDoesNotThrow(() -> t.setValue("1"));
        assertTrue(t.hasValue());
        assertEquals(1, t.getValue());
        assertEquals("1", t.getValueString());
    }

    @Test
    public void setMinLimitValueParameterless() {
        resetIntegerSetting();

        assertDoesNotThrow(() -> t.setValue(String.valueOf(Integer.MIN_VALUE)));
        assertTrue(t.hasValue());
        assertEquals(Integer.MIN_VALUE, t.getValue());
        assertEquals(String.valueOf(Integer.MIN_VALUE), t.getValueString());
    }

    @Test
    public void setMaxLimitValueParameterless() {
        resetIntegerSetting();

        assertDoesNotThrow(() -> t.setValue(String.valueOf(Integer.MAX_VALUE)));
        assertTrue(t.hasValue());
        assertEquals(Integer.MAX_VALUE, t.getValue());
        assertEquals(String.valueOf(Integer.MAX_VALUE), t.getValueString());
    }

    @Test
    public void setMinLimitValueParameterized() {
        resetIntegerSetting(501, -3);

        assertDoesNotThrow(() -> t.setValue("-3"));
        assertTrue(t.hasValue());
        assertEquals(-3, t.getValue());
        assertEquals("-3", t.getValueString());
    }

    @Test
    public void setMaxLimitValueParameterized() {
        resetIntegerSetting(3, -239);

        assertDoesNotThrow(() -> t.setValue("3"));
        assertTrue(t.hasValue());
        assertEquals(3, t.getValue());
        assertEquals("3", t.getValueString());
    }

    @Test
    public void setValueLowerThanLimit() {
        resetIntegerSetting(234, 1);

        assertThrows(ReasonedException.class, () -> t.setValue("0"));
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValueHigherThanLimit() {
        resetIntegerSetting(-1, -54);

        assertThrows(ReasonedException.class, () -> t.setValue("83"));
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValueLowerThanIntMinValue() {
        resetIntegerSetting();
                                                            //10 times the lower limit
        assertThrows(ReasonedException.class, () -> t.setValue(Integer.MIN_VALUE + "0"));
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValueHigherThanIntMaxValue() {
        resetIntegerSetting();

        assertThrows(ReasonedException.class, () -> t.setValue(Integer.MAX_VALUE + "0"));
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void getValueIfValueAndDefaultAreGiven() {
        resetIntegerSetting();

        //fuzzy
        for (int i = 0; i < 1000; i++) {
            final int temp = random.nextInt();
            assertDoesNotThrow(() -> t.setDefault(random.nextInt()));
            assertDoesNotThrow(() -> t.setValue(String.valueOf(temp)));
            assertTrue(t.hasValue());
            assertEquals(temp, t.getValue());
            assertEquals(String.valueOf(temp), t.getValueString());
        }
    }
}
