package Walter.commands;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class patchTests {

    @Test
    public void patchmessageLengthTest() {
        patch patch = new patch();

        int patchMessagelength = patch.getPatchMessage().length();
        assertTrue(patchMessagelength < 2000);
    }
}
