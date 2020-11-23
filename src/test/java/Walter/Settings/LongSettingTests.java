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

        assertEquals(Long.MIN_VALUE, t.getValue());
    }

    @Test
    public void correctParameterizedStartup() {
        resetLongSetting(6L, 2L);

        assertEquals(2L, t.getValue());
    }

    @Test
    public void setDefaultParameterless() {
        resetLongSetting();

        //fuzzy
        for (int i = 0; i < 1000; i++) {
            final long temp = random.nextLong();
            assertDoesNotThrow(() -> t.setDefault(temp));
            assertEquals(temp, t.getValue());
        }
    }

    @Test
    public void setDefaultParameterized() {
        resetLongSetting(32L, 22L);

        assertDoesNotThrow(() -> t.setDefault(24L));
        assertEquals(24L, t.getValue());
    }

    @Test
    public void setDefaultParameterizedWithNoGap() {
        resetLongSetting(0L, 0L);

        assertDoesNotThrow(() -> t.setDefault(0L));
        assertEquals(0L, t.getValue());
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
        assertEquals(Long.MIN_VALUE, t.getValue());
    }

    @Test
    public void setMaxLimitDefaultParameterless() {
        resetLongSetting();

        assertDoesNotThrow(() -> t.setDefault(Long.MAX_VALUE));
        assertEquals(Long.MAX_VALUE, t.getValue());
    }

    @Test
    public void setMinLimitDefaultParameterized() {
        resetLongSetting(5L, -5L);

        assertDoesNotThrow(() -> t.setDefault(-5L));
        assertEquals(-5L, t.getValue());
    }

    @Test
    public void setMaxLimitDefaultParameterized() {
        resetLongSetting(5L, -5L);

        assertDoesNotThrow(() -> t.setDefault(5L));
        assertEquals(5L, t.getValue());
    }

    @Test
    public void setTooLowDefault() {
        resetLongSetting(5L, -5L);

        assertThrows(ReasonedException.class, () -> t.setDefault(-6L));
        assertEquals(-5L, t.getValue());
    }

    @Test
    public void setTooHighDefault() {
        resetLongSetting(5L, -5L);

        assertThrows(ReasonedException.class, () -> t.setDefault(6L));
        assertEquals(-5, t.getValue());
    }

    @Test
    public void setValueParameterless() {
        resetLongSetting();

        //fuzzy
        for (int i = 0; i < 1000; i++) {
            long temp = random.nextLong();
            assertDoesNotThrow(() -> t.setValue(temp + ""));
            assertEquals(temp, t.getValue());
        }
    }

    @Test
    public void setValueParameterized() {
        resetLongSetting(5L, -5L);

        assertDoesNotThrow(() -> t.setValue("1"));
        assertEquals(1, t.getValue());
    }

    @Test
    public void setMinLimitValueParameterless() {
        resetLongSetting();

        assertDoesNotThrow(() -> t.setValue(Long.MIN_VALUE + ""));
        assertEquals(Long.MIN_VALUE, t.getValue());
    }

    @Test
    public void setMaxLimitValueParameterless() {
        resetLongSetting();

        assertDoesNotThrow(() -> t.setValue(Long.MAX_VALUE + ""));
        assertEquals(Long.MAX_VALUE, t.getValue());
    }

    @Test
    public void setMinLimitValueParameterized() {
        resetLongSetting(23L, -27);

        assertDoesNotThrow(() -> t.setValue("-27"));
        assertEquals(-27L, t.getValue());
    }

    @Test
    public void setMaxLimitValueParameterized() {
        resetLongSetting(732L, -23);

        assertDoesNotThrow(() -> t.setValue("732"));
        assertEquals(732L, t.getValue());
    }

    @Test
    public void setTooLowValue() {
        resetLongSetting(234L, 3L);

        assertThrows(ReasonedException.class, () -> t.setValue("0"));
        assertEquals(3L, t.getValue());
    }

    @Test
    public void setTooHighValue() {
        resetLongSetting(-832L, -92347L);

        assertThrows(ReasonedException.class, () -> t.setValue("7843"));
        assertEquals(-92347L, t.getValue());
    }

    @Test
    public void getValueIfValueAndDefaultAreGiven() {
        resetLongSetting();

        //fuzzy
        for (int i = 0; i < 1000; i++) {
            final long temp = random.nextLong();
            assertDoesNotThrow(() -> t.setDefault(random.nextLong()));
            assertDoesNotThrow(() -> t.setValue(temp + ""));
            assertEquals(temp, t.getValue());
        }
    }
}
