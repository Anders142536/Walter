package Walter.Settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
        assertEquals("Unnamed", t.getName());
        assertFalse(t.hasStartDate());
        assertEquals("Undefined", t.getStartDate());
        assertNull(t.getStartDateValue());
    }

    @Test
    public void setValidName() {
        t.setName("Testname");

        assertEquals("Testname", t.getName());
        assertFalse(t.hasStartDate());
        assertEquals("Undefined", t.getStartDate());
        assertNull(t.getStartDateValue());
    }

    @Test
    public void setEmptyName() {
        t.setName("");

        assertEquals("Unnamed", t.getName());
        assertFalse(t.hasStartDate());
        assertEquals("Undefined", t.getStartDate());
        assertNull(t.getStartDateValue());
    }

    @Test
    public void setLinebreakName() {
        t.setName("\n");
        assertEquals("Unnamed", t.getName());
        assertFalse(t.hasStartDate());
        assertEquals("Undefined", t.getStartDate());
        assertNull(t.getStartDateValue());
    }

    @Test
    public void setStartDate() {
        LocalDateTime startDate = LocalDateTime.now();
        t.setStartDate(startDate);

        assertTrue(t.hasStartDate());
        assertEquals(startDate, t.getStartDateValue());
        assertEquals(startDate.toString(), t.getStartDate());
        assertEquals("Unnamed", t.getName());
    }
}
