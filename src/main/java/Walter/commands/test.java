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
        keywords = new String[][]{
                {"test"}
        };
        minimumRequiredRole = BlackRole.GUEST;
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
