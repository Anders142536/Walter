package Walter.commands;

import Walter.EventScheduler;
import Walter.Helper;
import Walter.Parsers.Flag;
import Walter.Parsers.StringOption;
import Walter.Settings.EventSetting;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class events extends Command {

    StringOption eventname;
    Flag verbose;

    public events() {
        super(new String[] {
                "This command gives you a list of all currently defined events, as well as information about them.",
                "Dieser Befehl gibt dir eine Liste von allen derzeit definierten Events " +
                        "und detaillierte Informationen über sie."
        });
        keywords = new String[][] {
                {"events", "event"}
        };
        eventname = new StringOption(new String[] {"event name", "Eventname"},
                new String[] {"Name of event you want details of",
                        "Name des Events zu dem du Details willst"}
        , false);
        options = new ArrayList<>();
        options.add(eventname);

        verbose = new Flag('v', "verbose", new String[] {
                "Makes the list verbose",
                "Macht die Liste ausführlicher"
        });

        flags = new ArrayList<>();
        flags.add(verbose);
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        MessageChannel channel = event.getChannel();

        if (eventname.hasValue()) {
            try {
                EventSetting toPrint = EventScheduler.instance.getEvent(eventname.getValue());
                Helper.respond(channel, String.format("```%s```", toPrint.toString()));
            } catch (ReasonedException e) {
                throw new CommandExecutionException(e);
            }
        } else {
            if (verbose.isGiven()) {
                Helper.respond(channel, EventScheduler.instance.getFormattedListOfEvents());
            } else {
                Helper.respond(channel, EventScheduler.instance.getShortFormattedListOfEvents());
            }
        }
    }
}
