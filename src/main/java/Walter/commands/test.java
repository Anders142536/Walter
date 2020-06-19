package Walter.commands;

import Walter.Helper;
import Walter.Parsers.CommandParser;
import Walter.Parsers.Flag;
import Walter.Parsers.Option;
import Walter.Parsers.StringOption;
import Walter.entities.BlackRole;
import Walter.exceptions.ParseException;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class test extends Command {

    private MessageChannel channel;

    public test () {
        keywords = new String[]{"test"};
        minimumRequiredRole = BlackRole.GUEST;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() == 151010441043116032L) { // if Anders
            channel = event.getChannel();
            try {
                doCommandParserTests();
            } catch (Exception e) {
                Helper.instance.respondException(channel, null, e);
            }
        } else {
            Helper.instance.respond(event.getMember(), event.getChannel(),
                    "Es tut mir Leid, doch derzeit gibt es keine Tests.",
                    "I am utterly sorry, but there are no tests currently.");
        }
    }

    private void fail(String testnumber) {
        Helper.instance.respond(channel, "failed test: " + testnumber );
    }

    private void doCommandParserTests() throws Exception {
        Helper.instance.respond(channel, "Starting command parser tests");
        CommandParser p = new CommandParser();

        //test 0, no options or flags to parse
        //expected to simply not crash
        p.setStringToParse("");
        p.parse(null, null);

        p.setStringToParse("!test");
        p.parse(null, null);

        p.setStringToParse("!test test");
        p.parse(null, null);

        p.setStringToParse("!test --test");
        p.parse(null, null);

        //test 1, only one option to parse
        List<Option> options = optionList1();
        p.setStringToParse("!test testStringOption");
        p.parse(options, null);
        StringOption option = (StringOption)options.get(0);
        if (!option.hasValue()) fail("1.0");
        if (!option.getValue().equals("testStringOption")) fail("1.1");

        p.setStringToParse("!test");
        p.parse(options, null);
        if (option.hasValue()) fail("1.2");

        p.setStringToParse("!test --test");
        p.parse(options, null);
        if (option.hasValue()) fail("1.3");


        //test 2, only one flag to parse
        List<Flag> flags = flagList2();
        p.setStringToParse("!test -t");
        p.parse(null, flags);
        Flag flag = flags.get(0);
        if (!flag.isGiven()) fail("2.0");

        p.setStringToParse("!test");
        p.parse(null, flags);
        if (flag.isGiven()) fail("2.1");

        p.setStringToParse("!test --test");
        p.parse(null, flags);
        if (!flag.isGiven()) fail("2.2");

        p.setStringToParse("!test test");
        p.parse(null, flags);
        if (flag.isGiven()) fail("2.3");



        Helper.instance.respond(channel, "Finished command parser tests");
    }

    private List<Option> optionList1() {
        List<Option> list = new ArrayList<>();
        list.add(new StringOption("testString",
                "testString d",
                "stringoption for test",
                "stringoption for test d",
                false));
        return list;
    }

    private List<Flag> flagList2() {
        List<Flag> list = new ArrayList<>();
        list.add(new Flag('t',
                "test",
                "des",
                "des d"));
        return list;
    }

}
