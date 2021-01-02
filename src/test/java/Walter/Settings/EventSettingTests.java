package Walter.Settings;

import Walter.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class EventSettingTests {
    private EventSetting t;

    @BeforeEach
    public void resetEventSetting() {
        t = new SeasonSetting();
    }

    @Test
    public void correctStartup() {
        assertFalse(t.hasName());
        assertEquals("Unnamed", t.getName());
        assertFalse(t.hasStartDate());
        assertEquals("Undefined", t.getStartDate());
        assertNull(t.getStartDateValue());
    }

    @Test
    public void setValidName() {
        t.setName("Testname");

        assertTrue(t.hasName());
        assertEquals("Testname", t.getName());
        assertFalse(t.hasStartDate());
        assertEquals("Undefined", t.getStartDate());
        assertNull(t.getStartDateValue());
    }

    @Test
    public void setEmptyName() {
        t.setName("");

        assertFalse(t.hasName());
        assertEquals("Unnamed", t.getName());
        assertFalse(t.hasStartDate());
        assertEquals("Undefined", t.getStartDate());
        assertNull(t.getStartDateValue());
    }

    @Test
    public void setLinebreakName() {
        t.setName("\n");

        assertFalse(t.hasName());
        assertEquals("Unnamed", t.getName());
        assertFalse(t.hasStartDate());
        assertEquals("Undefined", t.getStartDate());
        assertNull(t.getStartDateValue());
    }

    @Test
    public void setStartDateString() {
        LocalDateTime startDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        t.setStartDate(startDate.format(Config.dateFormat));

        assertTrue(t.hasStartDate());
        assertEquals(startDate, t.getStartDateValue());
        assertEquals(startDate.format(Config.dateFormat), t.getStartDate());
        assertFalse(t.hasName());
        assertEquals("Unnamed", t.getName());
    }
}
