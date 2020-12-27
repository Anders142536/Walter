package Walter.Settings;

import Walter.Walter;
import Walter.WalterTest;
import Walter.exceptions.ReasonedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class FileSettingTests extends WalterTest {
    private FileSetting t;

    @BeforeEach
    public void resetSetting() { t = new FileSetting("/events/"); }

    @Test
    public void correctStartup() {
        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setValidFilename() {
        assertDoesNotThrow(() -> t.setValue("server.png"));

        assertTrue(t.hasValue());
        assertEquals(Walter.location + "/events/server.png", t.getValue());
        assertEquals(Walter.location + "/events/server.png", t.getValueString());
    }

    @Test
    public void setValueUndefined() {
        assertDoesNotThrow(() -> t.setValue("Undefined"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void setInvalidFilename() {
        assertThrows(ReasonedException.class, () -> t.setValue(""));
        assertThrows(ReasonedException.class, () -> t.setValue("         \n   "));
        assertThrows(ReasonedException.class, () -> t.setValue("nonexistent.png"));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }

    @Test
    public void resetBySettingNullFilename() {
        assertDoesNotThrow(() -> t.setValue("server.png"));
        assertDoesNotThrow(() -> t.setValue(null));

        assertFalse(t.hasValue());
        assertNull(t.getValue());
        assertEquals("Undefined", t.getValueString());
    }
}
