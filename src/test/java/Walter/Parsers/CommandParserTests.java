package Walter.Parsers;

import Walter.exceptions.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommandParserTests {
    private CommandParser t;

    @BeforeEach
    public void resetTests() { t = new CommandParser(); }

    @Test
    public void isCommand() {
        assertFalse(CommandParser.isCommand(""));
        assertFalse(CommandParser.isCommand("!"));
        assertFalse(CommandParser.isCommand("?"));
        assertFalse(CommandParser.isCommand(" !test"));
        assertFalse(CommandParser.isCommand(" ?test"));
        assertFalse(CommandParser.isCommand("test"));
        assertFalse(CommandParser.isCommand("$test"));
        assertFalse(CommandParser.isCommand("%test"));
        assertFalse(CommandParser.isCommand("! test"));
        assertFalse(CommandParser.isCommand("!t3st"));
        assertFalse(CommandParser.isCommand("!t-st"));

        //prefixes
        assertTrue(CommandParser.isCommand("!test"));
        assertTrue(CommandParser.isCommand("?test"));
        assertTrue(CommandParser.isCommand("!test   "));

        //normal text argument
        assertTrue(CommandParser.isCommand("!test test"));

        //number argument
        assertTrue(CommandParser.isCommand("!test 42"));
        assertTrue(CommandParser.isCommand("!test -42"));
        assertTrue(CommandParser.isCommand("!test 42.42"));
        assertTrue(CommandParser.isCommand("!test 42,42"));
        assertTrue(CommandParser.isCommand("!test -42.42"));
        assertTrue(CommandParser.isCommand("!test -42,42"));
        assertTrue(CommandParser.isCommand("!test .3"));
        assertTrue(CommandParser.isCommand("!test -.5"));
        assertFalse(CommandParser.isCommand("!test -0.5,2"));
        assertFalse(CommandParser.isCommand("!test -0,3e10"));

        //flag
        assertTrue(CommandParser.isCommand("!test -a"));
        assertTrue(CommandParser.isCommand("!test -dYusQ"));
        assertFalse(CommandParser.isCommand("!test --all"));
        assertFalse(CommandParser.isCommand("!test -"));

        //quotes
        assertTrue(CommandParser.isCommand("!test \"\""));
        assertTrue(CommandParser.isCommand("!test \"test\""));
        assertTrue(CommandParser.isCommand("!test \"test\ntest\""));
        assertTrue(CommandParser.isCommand("!test \"-a 9.2.1 \""));
        assertFalse(CommandParser.isCommand("!test \""));
        assertFalse(CommandParser.isCommand("!test \"\"\""));

        //several arguments and flags combined
        assertTrue(CommandParser.isCommand("!test test test test"));
        assertTrue(CommandParser.isCommand("!test -92.5 \"test\ntest\" -a"));
        assertFalse(CommandParser.isCommand("!test 0,5 test \""));
        assertFalse(CommandParser.isCommand("!test -0,4.6 test"));
    }

    @Test
    public void correctStartup() {
        assertThrows(ParseException.class, () -> t.parseFirstArgument());
        assertThrows(ParseException.class, () -> t.parse(null, null));
    }

    @Test
    public void parseFirstArgument() {
        t.setStringToParse("!test");
        assertDoesNotThrow(() -> t.parseFirstArgument());
        try {
            assertEquals("test", t.parseFirstArgument());
        } catch (ParseException e) {
            fail();
        }
    }
}

