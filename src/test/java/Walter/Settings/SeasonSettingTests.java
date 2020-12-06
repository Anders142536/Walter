package Walter.Settings;

import Walter.Walter;
import Walter.WalterTest;
import Walter.exceptions.ReasonedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SeasonSettingTests extends WalterTest {

    private SeasonSetting t;


    @BeforeEach
    public void resetSetting() {
        t = new SeasonSetting();
    }

    @Test
    public void correctStartup() {
        assertFalse(t.memberColor.hasValue());
        assertFalse(t.serverLogoFile.hasValue());
        assertFalse(t.walterLogoFile.hasValue());
        assertNull(t.serverLogoFile.getValue());
        assertNull(t.walterLogoFile.getValue());
        assertNull(t.memberColor.getValue());
        assertEquals("name:         Unnamed\n" +
                "start date:   Undefined\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.toString());
    }

    @Test
    public void allCorrectlyGiven() {
        assertDoesNotThrow(() -> t.serverLogoFile.setValue("server.png"));
        assertDoesNotThrow(() -> t.walterLogoFile.setValue("walter.png"));
        assertDoesNotThrow(() -> t.memberColor.setValue("#0DEAD0"));

        assertTrue(t.serverLogoFile.hasValue());
        assertTrue(t.walterLogoFile.hasValue());
        assertTrue(t.memberColor.hasValue());

        assertEquals("name:         Unnamed\n" +
                "start date:   Undefined\n" +
                "member color: #0dead0\n" +
                "server logo:  " + Walter.location + "/events/server.png\n" +
                "walter logo:  " + Walter.location + "/events/walter.png", t.toString());
    }
}
