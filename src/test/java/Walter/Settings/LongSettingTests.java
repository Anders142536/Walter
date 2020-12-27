package Walter.Settings;

import Walter.exceptions.ReasonedException;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class LongSettingTests {
    private LongSetting t;
    private Random random = new Random();

    private void resetLongSetting() { t = new LongSetting(); }

    private void resetLongSetting(long max, long min) { t = new LongSetting(max, min); }

    @Test
    public void correctParameterlessStartup() {
        resetLongSetting();

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void correctParameterizedStartup() {
        resetLongSetting(6L, 2L);

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValueUndefined() {
        resetLongSetting();

        assertDoesNotThrow(() -> t.setValue("Undefined"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setDefaultParameterless() {
        resetLongSetting();

        //fuzzy
        for (int i = 0; i < 1000; i++) {
            final long temp = random.nextLong();
            assertDoesNotThrow(() -> t.setDefault(temp));
            assertFalse(t.hasValue());
            assertEquals(temp, t.getValue());
            assertEquals(String.valueOf(temp), t.getValueString());
        }
    }

    @Test
    public void setDefaultParameterized() {
        resetLongSetting(32L, 22L);

        assertDoesNotThrow(() -> t.setDefault(24L));
        assertFalse(t.hasValue());
        assertEquals(24L, t.getValue());
        assertEquals("24", t.getValueString());
    }

    @Test
    public void setDefaultParameterizedWithNoGap() {
        resetLongSetting(0L, 0L);

        assertDoesNotThrow(() -> t.setDefault(0L));
        assertFalse(t.hasValue());
        assertEquals(0L, t.getValue());
        assertEquals("0", t.getValueString());
    }

    @Test
    public void setWrongParameters() {
        assertThrows(AssertionError.class, () -> resetLongSetting(-32L, 23L));
        assertNull(t);
    }

    @Test
    public void setMinLimitDefaultParameterless() {
        resetLongSetting();

        assertDoesNotThrow(() -> t.setDefault(Long.MIN_VALUE));
        assertFalse(t.hasValue());
        assertEquals(Long.MIN_VALUE, t.getValue());
        assertEquals(String.valueOf(Long.MIN_VALUE), t.getValueString());
    }

    @Test
    public void setMaxLimitDefaultParameterless() {
        resetLongSetting();

        assertDoesNotThrow(() -> t.setDefault(Long.MAX_VALUE));
        assertFalse(t.hasValue());
        assertEquals(Long.MAX_VALUE, t.getValue());
        assertEquals(String.valueOf(Long.MAX_VALUE), t.getValueString());
    }

    @Test
    public void setMinLimitDefaultParameterized() {
        resetLongSetting(5L, -5L);

        assertDoesNotThrow(() -> t.setDefault(-5L));
        assertFalse(t.hasValue());
        assertEquals(-5L, t.getValue());
        assertEquals("-5", t.getValueString());
    }

    @Test
    public void setMaxLimitDefaultParameterized() {
        resetLongSetting(5L, -5L);

        assertDoesNotThrow(() -> t.setDefault(5L));
        assertFalse(t.hasValue());
        assertEquals(5L, t.getValue());
        assertEquals("5", t.getValueString());
    }

    @Test
    public void setTooLowDefault() {
        resetLongSetting(5L, -5L);

        assertThrows(ReasonedException.class, () -> t.setDefault(-6L));
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setTooHighDefault() {
        resetLongSetting(5L, -5L);

        assertThrows(ReasonedException.class, () -> t.setDefault(6L));
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValueParameterless() {
        resetLongSetting();

        //fuzzy
        for (int i = 0; i < 1000; i++) {
            long temp = random.nextLong();
            assertDoesNotThrow(() -> t.setValue(String.valueOf(temp)));
            assertTrue(t.hasValue());
            assertEquals(temp, t.getValue());
            assertEquals(String.valueOf(temp), t.getValueString());
        }
    }

    @Test
    public void setValueParameterized() {
        resetLongSetting(5L, -5L);

        assertDoesNotThrow(() -> t.setValue("1"));
        assertTrue(t.hasValue());
        assertEquals(1, t.getValue());
        assertEquals("1", t.getValueString());
    }

    @Test
    public void setMinLimitValueParameterless() {
        resetLongSetting();

        assertDoesNotThrow(() -> t.setValue(String.valueOf(Long.MIN_VALUE)));
        assertTrue(t.hasValue());
        assertEquals(Long.MIN_VALUE, t.getValue());
        assertEquals(String.valueOf(Long.MIN_VALUE), t.getValueString());
    }

    @Test
    public void setMaxLimitValueParameterless() {
        resetLongSetting();

        assertDoesNotThrow(() -> t.setValue(String.valueOf(Long.MAX_VALUE)));
        assertTrue(t.hasValue());
        assertEquals(Long.MAX_VALUE, t.getValue());
        assertEquals(String.valueOf(Long.MAX_VALUE), t.getValueString());
    }

    @Test
    public void setMinLimitValueParameterized() {
        resetLongSetting(23L, -27);

        assertDoesNotThrow(() -> t.setValue("-27"));
        assertTrue(t.hasValue());
        assertEquals(-27L, t.getValue());
        assertEquals("-27", t.getValueString());
    }

    @Test
    public void setMaxLimitValueParameterized() {
        resetLongSetting(732L, -23);

        assertDoesNotThrow(() -> t.setValue("732"));
        assertTrue(t.hasValue());
        assertEquals(732L, t.getValue());
        assertEquals("732", t.getValueString());
    }

    @Test
    public void setValueLowerThanLimit() {
        resetLongSetting(234L, 3L);

        assertThrows(ReasonedException.class, () -> t.setValue("0"));
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValueHigherThanLimit() {
        resetLongSetting(-832L, -92347L);

        assertThrows(ReasonedException.class, () -> t.setValue("7843"));
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValueLowerThanLongLimit() {
        resetLongSetting();

        assertThrows(ReasonedException.class, () -> t.setValue(Long.MIN_VALUE + "0"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValueHigherThanLongLimit() {
        resetLongSetting();

        assertThrows(ReasonedException.class, () -> t.setValue(Long.MAX_VALUE + "0"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void getValueIfValueAndDefaultAreGiven() {
        resetLongSetting();

        //fuzzy
        for (int i = 0; i < 1000; i++) {
            final long temp = random.nextLong();
            assertDoesNotThrow(() -> t.setDefault(random.nextLong()));
            assertDoesNotThrow(() -> t.setValue(String.valueOf(temp)));
            assertTrue(t.hasValue());
            assertEquals(temp, t.getValue());
            assertEquals(String.valueOf(temp), t.getValueString());
        }
    }
}
