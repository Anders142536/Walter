package Walter.Parsers;

import Walter.Config;
import Walter.exceptions.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
        //TODO: investigate why this fails on windows
        if (!System.getProperty("os.name").toLowerCase().startsWith("win"))
            assertTrue(CommandParser.isCommand("!testßüäöÖÄÜ"));

        //normal text argument
        assertTrue(CommandParser.isCommand("!test täßtöÜ"));
        assertTrue(CommandParser.isCommand("!listening aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

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
        if (!System.getProperty("os.name").toLowerCase().startsWith("win")) {
            assertTrue(CommandParser.isCommand("!test -ä"));
            assertTrue(CommandParser.isCommand("!test --äll"));
        }
        assertFalse(CommandParser.isCommand("!test -dYusQ"));
        assertFalse(CommandParser.isCommand("!test -"));

        //quotes
        assertTrue(CommandParser.isCommand("!test \"\""));
        assertTrue(CommandParser.isCommand("!test \"test\""));
        assertTrue(CommandParser.isCommand("!test \"test\ntest\""));
        assertTrue(CommandParser.isCommand("!test \"-a 9.2.1 \""));
        assertFalse(CommandParser.isCommand("!test \"höre"));
        assertFalse(CommandParser.isCommand("!test \"\"\""));

        //several arguments and flags combined
        if (!System.getProperty("os.name").toLowerCase().startsWith("win")) {
            assertTrue(CommandParser.isCommand("!test test tÖst teßt"));
            assertTrue(CommandParser.isCommand("!test -92.5 \"test\ntest\" -ä"));
        }
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
        String[] name = new String[]{"test", "test"};
        switch (type) {
            case STRING:
                return new StringOption(name, null);
            case INT:
                return new IntegerOption(name, null);
            case DATETIME:
                return new DateTimeOption(name, null);
            case FLUSH:
                return new FlushOption(name, null);
        }
        return null;
    }

    private Flag getTestFlag() {
        return getTestFlag("test");
    }

    private Flag getTestFlag(String name) {
        return new Flag(name.charAt(0), name, null);
    }

    @Test
    public void parseTestNoOptionsNoFlags() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        StringOption testStringOption = (StringOption)getTestOption(OptionType.STRING);
        Flag testFlag = getTestFlag();

        options.add(testStringOption);
        flags.add(testFlag);
        t.setStringToParse("!test");

        assertDoesNotThrow(() -> t.parse(null, null));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertDoesNotThrow(() -> t.parse(null, flags));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertThrows(ParseException.class, () -> t.parse(options, null));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertThrows(ParseException.class, () -> t.parse(options, flags));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());
    }

    @Test
    public void parseTestNoOptionsNoFlagsEmptyLists() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        t.setStringToParse("!test");

        assertDoesNotThrow(() -> t.parse(null, null));
        assertDoesNotThrow(() -> t.parse(null, flags));
        assertDoesNotThrow(() -> t.parse(options, null));
        assertDoesNotThrow(() -> t.parse(options, flags));
    }

    @Test
    public void parseTestStringOptionNoFlags() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        StringOption testStringOption = (StringOption)getTestOption(OptionType.STRING);
        Flag testFlag = getTestFlag();

        options.add(testStringOption);
        flags.add(testFlag);
        t.setStringToParse("!test 02");

        assertThrows(ParseException.class, () -> t.parse(null, null));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertThrows(ParseException.class, () -> t.parse(null, flags));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertDoesNotThrow(() -> t.parse(options, null));
        assertTrue(testStringOption.hasValue());
        assertEquals(testStringOption.getValue(), "02");
        assertFalse(testFlag.isGiven());

        assertDoesNotThrow(() -> t.parse(options, flags));
        assertTrue(testStringOption.hasValue());
        assertEquals(testStringOption.getValue(), "02");
        assertFalse(testFlag.isGiven());
    }

    @Test
    public void parseTestIntOptionNoFlags() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        IntegerOption testIntOption = (IntegerOption)getTestOption(OptionType.INT);
        Flag testFlag = getTestFlag();

        options.add(testIntOption);
        flags.add(testFlag);
        t.setStringToParse("!test 2");

        assertThrows(ParseException.class, () -> t.parse(null, null));
        assertFalse(testIntOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertThrows(ParseException.class, () -> t.parse(null, flags));
        assertFalse(testIntOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertDoesNotThrow(() -> t.parse(options, null));
        assertTrue(testIntOption.hasValue());
        assertEquals(testIntOption.getValue(), 2);
        assertFalse(testFlag.isGiven());

        assertDoesNotThrow(() -> t.parse(options, flags));
        assertTrue(testIntOption.hasValue());
        assertEquals(testIntOption.getValue(), 2);
        assertFalse(testFlag.isGiven());
    }

    @Test
    public void parseDateTimeOptionNoFlags() {
        List<Option> options = new ArrayList<>();

        DateTimeOption testDateOption = (DateTimeOption) getTestOption(OptionType.DATETIME);

        options.add(testDateOption);

        t.setStringToParse("!test \"10/01/2020 23:59:04\"");

        LocalDateTime expected = LocalDateTime.parse("10/01/2020 23:59:04", Config.dateFormat);

        assertDoesNotThrow(() -> t.parse(options, null));
        assertTrue(testDateOption.hasValue());
        //TODO this
        assertEquals(expected, testDateOption.getValue());
    }

    @Test
    public void parseTestFlushOptionNoFlags() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        FlushOption testFlushOption = (FlushOption)getTestOption(OptionType.FLUSH);
        Flag testFlag = getTestFlag();

        options.add(testFlushOption);
        flags.add(testFlag);
        t.setStringToParse("!test Lorem Ipsum\nLorem Ipsum\n");

        assertThrows(ParseException.class, () -> t.parse(null, null));
        assertFalse(testFlushOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertThrows(ParseException.class, () -> t.parse(null, flags));
        assertFalse(testFlushOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertDoesNotThrow(() -> t.parse(options, null));
        assertTrue(testFlushOption.hasValue());
        assertEquals(testFlushOption.getValue(), "Lorem Ipsum\nLorem Ipsum\n");
        assertFalse(testFlag.isGiven());

        assertDoesNotThrow(() -> t.parse(options, flags));
        assertTrue(testFlushOption.hasValue());
        assertEquals(testFlushOption.getValue(), "Lorem Ipsum\nLorem Ipsum\n");
        assertFalse(testFlag.isGiven());
    }

    @Test
    public void parseTestNoOptionsShortFlag() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        StringOption testStringOption = (StringOption)getTestOption(OptionType.STRING);
        Flag testFlag = getTestFlag();

        options.add(testStringOption);
        flags.add(testFlag);

        t.setStringToParse("!test -t");
        assertThrows(ParseException.class, () -> t.parse(null, null));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertDoesNotThrow(() -> t.parse(null, flags));
        assertFalse(testStringOption.hasValue());
        assertTrue(testFlag.isGiven());

        testFlag.reset(); //has to be done manually as the flags list is not given and therefore not reset by the parse method

        assertThrows(ParseException.class, () -> t.parse(options, null));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertThrows(ParseException.class, () -> t.parse(options, flags));
        assertFalse(testStringOption.hasValue());
        assertTrue(testFlag.isGiven());
    }

    @Test
    public void parseTestNoOptionsLongFlag() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        StringOption testStringOption = (StringOption)getTestOption(OptionType.STRING);
        Flag testFlag = getTestFlag();

        options.add(testStringOption);
        flags.add(testFlag);

        t.setStringToParse("!test --test");
        assertThrows(ParseException.class, () -> t.parse(null, null));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertDoesNotThrow(() -> t.parse(null, flags));
        assertFalse(testStringOption.hasValue());
        assertTrue(testFlag.isGiven());

        testFlag.reset(); //has to be done manually as the flags list is not given and therefore not reset by the parse method

        assertThrows(ParseException.class, () -> t.parse(options, null));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertThrows(ParseException.class, () -> t.parse(options, flags));
        assertFalse(testStringOption.hasValue());
        assertTrue(testFlag.isGiven());
    }

    @Test
    public void parseTestNoOptionsShortFlagWithOptionGiven() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        StringOption testStringOption = (StringOption)getTestOption(OptionType.STRING);
        Flag testFlag = new Flag('t', "test", null, testStringOption);

        flags.add(testFlag);

        t.setStringToParse("!test -t testoption");
        assertThrows(ParseException.class, () -> t.parse(null, null));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertDoesNotThrow(() -> t.parse(null, flags));
        assertTrue(testStringOption.hasValue());
        assertEquals(testStringOption.getValue(), "testoption");
        assertTrue(testFlag.isGiven());

        flags.get(0).reset(); //has to be done manually as the flags list is not given and therefore not reset by the parse method

        assertThrows(ParseException.class, () -> t.parse(options, null));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertDoesNotThrow(() -> t.parse(options, flags));
        assertTrue(testStringOption.hasValue());
        assertEquals(testStringOption.getValue(), "testoption");
        assertTrue(testFlag.isGiven());
    }

    @Test
    public void parseTestNoOptionsShortFlagWithOptionNotGiven() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        StringOption testStringOption = (StringOption)getTestOption(OptionType.STRING);
        Flag testFlag = new Flag('t', "test", null, testStringOption);

        flags.add(testFlag);

        t.setStringToParse("!test -t");
        assertThrows(ParseException.class, () -> t.parse(null, null));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertThrows(ParseException.class, () -> t.parse(null, flags));
        assertFalse(testStringOption.hasValue());
        assertTrue(testFlag.isGiven());

        flags.get(0).reset(); //has to be done manually as the flags list is not given and therefore not reset by the parse method

        assertThrows(ParseException.class, () -> t.parse(options, null));
        assertFalse(testStringOption.hasValue());
        assertFalse(testFlag.isGiven());

        assertThrows(ParseException.class, () -> t.parse(options, flags));
        assertFalse(testStringOption.hasValue());
        assertTrue(testFlag.isGiven());
    }

    @Test
    public void parseTestIntQuoteOptionShortFlag() {
        List<Option> options = new ArrayList<>();
        List<Flag> flags = new ArrayList<>();

        IntegerOption testIntOption = (IntegerOption)getTestOption(OptionType.INT);
        StringOption testStringOption = (StringOption)getTestOption(OptionType.STRING);
        IntegerOption testOptionalOption = new IntegerOption(new String[] {"test", "test"}, null, false);
        StringOption testFlagParam = (StringOption)getTestOption(OptionType.STRING);
        Flag testFlag = new Flag('t', "test", null, testFlagParam);

        options.add(testIntOption);
        options.add(testStringOption);
        options.add(testOptionalOption);
        flags.add(testFlag);

        t.setStringToParse("!test 4 -t testFlagRequirement \"test\"");
        assertDoesNotThrow(() -> t.parse(options, flags));
        assertTrue(testIntOption.hasValue());
        assertTrue(testStringOption.hasValue());
        assertFalse(testOptionalOption.hasValue());
        assertTrue(testFlagParam.hasValue());
        assertEquals(4, testIntOption.getValue());
        assertEquals("test", testStringOption.getValue());
        assertTrue(testFlag.isGiven());
        assertEquals("testFlagRequirement", ((StringOption)testFlag.getParameter()).getValue());
    }

}

