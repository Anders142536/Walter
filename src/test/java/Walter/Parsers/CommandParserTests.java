package Walter.Parsers;

import Walter.exceptions.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommandParserTests {
    private CommandParser t;

    @BeforeEach
    public void resetTests() { t = new CommandParser(); }

    @Test
    public void isCommandTest() {
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
        assertTrue(CommandParser.isCommand("!test --all"));
        assertFalse(CommandParser.isCommand("!test -dYusQ"));
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
    public void correctStartupTest() {
        assertThrows(ParseException.class, () -> t.parseCommandName());
        assertThrows(ParseException.class, () -> t.parse(null, null));
    }

    @Test
    public void parseCommandNameTest() {
        t.setStringToParse("!test");
        assertDoesNotThrow(() -> assertEquals("test", t.parseCommandName()));

        resetTests();
        t.setStringToParse("!test");
        assertDoesNotThrow(() -> assertEquals("test", t.parseCommandName()));

        resetTests();
        t.setStringToParse("!test test");
        assertDoesNotThrow(() -> assertEquals("test", t.parseCommandName()));

        resetTests();
        t.setStringToParse("!test -5.5");
        assertDoesNotThrow(() -> assertEquals("test", t.parseCommandName()));

        resetTests();
        t.setStringToParse("!test -t 234 ");
        assertDoesNotThrow(() -> assertEquals("test", t.parseCommandName()));

    }

    private Option getTestOption(OptionType type) {
        switch (type) {
            case STRING:
                return new StringOption("test", "test", "test", "test");
            case INT:
                return new IntegerOption("test", "test", "test", "test");
            case FLUSH:
                return new FlushOption("test", "test", "test", "test");
        }
        return null;
    }

    private Flag getTestFlag() {
        return getTestFlag("test");
    }

    private Flag getTestFlag(String name) {
        return new Flag(name.charAt(0), name, name, name);
    }

    @Test
    public void parseTestNoOptionsNoFlags() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        options.add(getTestOption(OptionType.STRING));
        flags.add(getTestFlag());
        t.setStringToParse("!test");

        assertDoesNotThrow(() -> t.parse(null, null));
        assertFalse(options.get(0).hasValue());
        assertFalse(flags.get(0).isGiven());

        assertDoesNotThrow(() -> t.parse(null, flags));
        assertFalse(options.get(0).hasValue());
        assertFalse(flags.get(0).isGiven());

        assertThrows(ParseException.class, () -> t.parse(options, null));
        assertFalse(options.get(0).hasValue());
        assertFalse(flags.get(0).isGiven());

        assertThrows(ParseException.class, () -> t.parse(options, flags));
        assertFalse(options.get(0).hasValue());
        assertFalse(flags.get(0).isGiven());
    }

    @Test
    public void parseTestStringOptionNoFlags() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        options.add(getTestOption(OptionType.STRING));
        flags.add(getTestFlag());
        t.setStringToParse("!test 02");

        assertThrows(ParseException.class, () -> t.parse(null, null));
        assertFalse(options.get(0).hasValue());
        assertFalse(flags.get(0).isGiven());

        assertThrows(ParseException.class, () -> t.parse(null, flags));
        assertFalse(options.get(0).hasValue());
        assertFalse(flags.get(0).isGiven());

        assertDoesNotThrow(() -> t.parse(options, null));
        StringOption third = (StringOption)options.get(0);
        assertTrue(third.hasValue());
        assertEquals(third.getValue(), "02");
        assertFalse(flags.get(0).isGiven());

        assertDoesNotThrow(() -> t.parse(options, flags));
        StringOption fourth = (StringOption)options.get(0);
        assertTrue(fourth.hasValue());
        assertEquals(fourth.getValue(), "02");
        assertFalse(flags.get(0).isGiven());
    }

    @Test
    public void parseTestIntOptionNoFlags() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        options.add(getTestOption(OptionType.INT));
        flags.add(getTestFlag());
        t.setStringToParse("!test 2");

        assertThrows(ParseException.class, () -> t.parse(null, null));
        assertFalse(options.get(0).hasValue());
        assertFalse(flags.get(0).isGiven());

        assertThrows(ParseException.class, () -> t.parse(null, flags));
        assertFalse(options.get(0).hasValue());
        assertFalse(flags.get(0).isGiven());

        assertDoesNotThrow(() -> t.parse(options, null));
        IntegerOption third = (IntegerOption) options.get(0);
        assertTrue(third.hasValue());
        assertEquals(third.getValue(), 2);
        assertFalse(flags.get(0).isGiven());

        assertDoesNotThrow(() -> t.parse(options, flags));
        IntegerOption fourth = (IntegerOption) options.get(0);
        assertTrue(fourth.hasValue());
        assertEquals(fourth.getValue(), 2);
        assertFalse(flags.get(0).isGiven());
    }

    @Test
    public void parseTestFlushOptionNoFlags() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        options.add(getTestOption(OptionType.FLUSH));
        flags.add(getTestFlag());
        t.setStringToParse("!test Lorem Ipsum\nLorem Ipsum\n");

        assertThrows(ParseException.class, () -> t.parse(null, null));
        assertFalse(options.get(0).hasValue());
        assertFalse(flags.get(0).isGiven());

        assertThrows(ParseException.class, () -> t.parse(null, flags));
        assertFalse(options.get(0).hasValue());
        assertFalse(flags.get(0).isGiven());

        assertDoesNotThrow(() -> t.parse(options, null));
        FlushOption third = (FlushOption) options.get(0);
        assertTrue(third.hasValue());
        assertEquals(third.getValue(), "Lorem Ipsum\nLorem Ipsum\n");
        assertFalse(flags.get(0).isGiven());

        assertDoesNotThrow(() -> t.parse(options, flags));
        FlushOption fourth = (FlushOption) options.get(0);
        assertTrue(fourth.hasValue());
        assertEquals(fourth.getValue(), "Lorem Ipsum\nLorem Ipsum\n");
        assertFalse(flags.get(0).isGiven());
    }

    @Test
    public void parseTestNoOptionsShortFlag() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        options.add(getTestOption(OptionType.STRING));
        flags.add(getTestFlag());

        t.setStringToParse("!test -t");
        assertThrows(ParseException.class, () -> t.parse(null, null));
        assertFalse(options.get(0).hasValue());
        assertFalse(flags.get(0).isGiven());

        assertDoesNotThrow(() -> t.parse(null, flags));
        assertFalse(options.get(0).hasValue());
        assertTrue(flags.get(0).isGiven());

        flags.get(0).reset(); //has to be done manually as the flags list is not given and therefore not reset by the parse method

        assertThrows(ParseException.class, () -> t.parse(options, null));
        assertFalse(options.get(0).hasValue());
        assertFalse(flags.get(0).isGiven());

        assertThrows(ParseException.class, () -> t.parse(options, flags));
        assertFalse(options.get(0).hasValue());
        assertTrue(flags.get(0).isGiven());
    }

    @Test
    public void parseTestIntQuoteOptionShortFlag() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        options.add(getTestOption(OptionType.INT));
        options.add(getTestOption(OptionType.STRING));
        flags.add(getTestFlag());

        t.setStringToParse("!test 4 -t \"test\"");
        assertDoesNotThrow(() -> t.parse(options, flags));
        IntegerOption test = (IntegerOption) options.get(0);
        StringOption testString = (StringOption) options.get(1);
        assertEquals(4, test.getValue());
        assertEquals("test", testString.getValue());
        assertTrue(flags.get(0).isGiven());
    }

}

