package Walter.Quoter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuoteMessageTests {

    @Test
    public void multilineQuoteMessageTest() {
        /*
        WannaCryToday at 23:12
        genau das mit der zeit ist gerade mein problem haha
        aber ja das stimmt eh und hilft definitiv
         */
        QuoteMessage msg = new QuoteMessage("WannaCry", "Today at 23:12",
                "genau das mit der zeit ist gerade mein problem haha\n" +
                        "aber ja das stimmt eh und hilft definitiv");

        assertEquals("**WannaCry** *Today at 23:12*\n" +
                "> genau das mit der zeit ist gerade mein problem haha\n" +
                "> aber ja das stimmt eh und hilft definitiv", msg.toString());
    }
}
