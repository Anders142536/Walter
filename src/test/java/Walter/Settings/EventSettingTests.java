package Walter.Settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventSettingTests {
    private EventSetting t;

    @BeforeEach
    public void resetEventSetting() {
        t = new EventSetting("Testevent");
    }

    @Test
    public void correctStartup() {
        assertEquals("Testevent", t.getName());
    }
}
