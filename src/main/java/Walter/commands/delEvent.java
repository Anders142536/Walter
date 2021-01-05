package Walter.commands;

import Walter.EventScheduler;
import Walter.Parsers.StringOption;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class delEvent extends Command {
    StringOption eventname;

    public delEvent() {
        super(new String[] {
                "This command deletes the event with the name **EVENT NAME**.",
                "Dieser Befehl löscht das Event mit dem Namen **EVENTNAME**."
        });

        keywords = new String[][] {
                {"delEvent", "deleteEvent", "rmEvent"},
                {"löscheEvent", "entferneEvent"}
        };

        eventname = new StringOption(new String[] {"event name", "Eventname"},
                new String[] {"Name of the event to delete",
                        "Name des zu löschenden Events"}
        );

        options = new ArrayList<>();
        options.add(eventname);
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        try {
            EventScheduler.instance.deleteEvent(eventname.getValue());
        } catch (ReasonedException e) {
            throw new CommandExecutionException(e);
        }
    }
}
