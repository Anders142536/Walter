package Walter.commands;

import Walter.Helper;
import Walter.Parsers.Flag;
import Walter.Parsers.StringOption;
import Walter.entities.BlackRole;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class test extends Command {
    Flag test;
    Flag anothertest;
    StringOption text;


    public test () {
        keywords = new String[][]{
                {"test"}
        };
        minimumRequiredRole = BlackRole.GUEST;
        test = new Flag('t', "test", new String[] {
                "testflag for demonstration purposes, with awfully long description",
                "testschalter aus demo zwecken"
        });
        anothertest = new Flag('a', "all", null);
        text = new StringOption(new String[] {"text", "Text"}, null, false);
        options = new ArrayList<>();
        flags = new ArrayList<>();

        options.add(text);
        flags.add(test);
        flags.add(anothertest);
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() == 151010441043116032L) { // if Anders

        } else {
            Helper.instance.respond(event.getMember(), event.getChannel(),
                    "Es tut mir Leid, doch derzeit gibt es keine Tests.",
                    "I am utterly sorry, but there are no tests currently.");
        }
    }
}
