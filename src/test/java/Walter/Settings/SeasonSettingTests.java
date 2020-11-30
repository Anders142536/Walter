package Walter.Settings;

import Walter.WalterTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

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
        assertNull(t.getServerLogoFile());
        assertNull(t.getWalterLogoFile());
        assertNull(t.getMemberColor());
        assertEquals("name:         Unnamed\n" +
                "start date:   DEFAULT\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.toString());
    }

    @Test
    public void invalidFileForServerLogo() {
        assertThrows(FileNotFoundException.class, () -> t.setServerLogoFile(""));
        assertFalse(t.hasServerLogoFile());
        assertNull(t.getServerLogoFile());
    }

    @Test
    public void validFileForServerLogo() {
        getTestfileFolder();
//        assertDoesNotThrow(() -> t.setServerLogoFile(""));
    }
}
