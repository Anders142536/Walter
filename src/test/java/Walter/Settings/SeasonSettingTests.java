package Walter.Settings;

import Walter.Walter;
import Walter.WalterTest;
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
        assertFalse(t.hasMemberColor());
        assertFalse(t.hasServerLogoFile());
        assertFalse(t.hasWalterLogoFile());
        assertNull(t.getServerLogoFileValue());
        assertNull(t.getWalterLogoFileValue());
        assertNull(t.getMemberColorValue());
        assertEquals("Undefined", t.getServerLogoFile());
        assertEquals("Undefined", t.getWalterLogoFile());
        assertEquals("Undefined", t.getMemberColor());
        assertEquals("Season", t.getType());

        assertEquals("type:         Season\n" +
                "name:         Unnamed\n" +
                "start date:   Undefined\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.toString());
    }

    @Test
    public void allCorrectlyGiven() {
        assertDoesNotThrow(() -> t.setServerLogoFile("server.png"));
        assertDoesNotThrow(() -> t.setWalterLogoFile("walter.png"));
        assertDoesNotThrow(() -> t.setMemberColor("#0DEAD0"));

        assertTrue(t.hasServerLogoFile());
        assertTrue(t.hasWalterLogoFile());
        assertTrue(t.hasMemberColor());

        assertEquals("type:         Season\n" +
                "name:         Unnamed\n" +
                "start date:   Undefined\n" +
                "member color: #0dead0\n" +
                "server logo:  " + Walter.location + "events/server.png\n" +
                "walter logo:  " + Walter.location + "events/walter.png", t.toString());
    }
}
