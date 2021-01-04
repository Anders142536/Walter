package Walter.commands;

import Walter.Config;
import Walter.EventScheduler;
import Walter.Helper;
import Walter.Parsers.Flag;
import Walter.Parsers.StringOption;
import Walter.Settings.SeasonSetting;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.ArrayList;

public class newEvent extends Command {
    StringOption eventname;

    public newEvent() {
        super(new String[] {
                "This command creates a new event with the name **EVENT NAME**. It can then be edited with " +
                        "the \"editEvent\" command",
                "Dieser Befehl erzeugt ein neues Event mit dem Namen **EVENTNAME**. Danach kann es mit dem " +
                        "\"editEvent\" Befehl editiert werden."
        });
        keywords = new String[][] {
                {"newEvent", "createEvent", "makeEvent"},
                {"neuesEvent", "kreiereEvent", "erzeugeEvent"}
        };

        eventname = new StringOption(new String[] {"event name", "Eventname"},
                new String[] {"Name of the new event",
                "Name des neuen Events"}
        );

        options = new ArrayList<>();
        options.add(eventname);
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        try {
            SeasonSetting setting = new SeasonSetting();
            setting.setName(eventname.getValue());
            EventScheduler.instance.addEvent(setting);
            Config.save();
            Helper.respond(event.getAuthor(), event.getChannel(), new String[] {
                    eventname.getValue() + " event successfully created",
                    eventname.getValue() + " Event erfolgreich erstellt"
            });
        } catch (ReasonedException e) {
            throw new CommandExecutionException(e);
        }
    }
}
