package Walter.Settings;

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
        assertFalse(t.getValue());
    }

    @Test
    public void setValueBoolean() {
        //fuzzy
        for (int i = 0; i < 1000; i++) {
            final boolean temp = random.nextBoolean();
            assertDoesNotThrow(() -> t.setValue(temp));
            assertEquals(temp, t.getValue());
        }
    }

    @Test
    public void setValueString() {
        t.setValue("true");
        assertTrue(t.getValue());

        t.setValue("false");
        assertFalse(t.getValue());
    }

    @Test
    public void setInvalidStringValue() {
        assertThrows(AssertionError.class, () -> t.setValue("true "));
        assertFalse(t.getValue());
    }

}
