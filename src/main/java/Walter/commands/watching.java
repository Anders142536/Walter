package Walter.commands;

import Walter.Parsers.Flag;
import Walter.Parsers.StringOption;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class watching extends Command {
    Flag clear;
    StringOption source;

    public watching() {
        super(new String[] {
                "This command changes what Walter is watching to **media source**",
                "Dieser Command ändert was Walter gerade schaut zu **Medienquelle**"
        });
        keywords = new String[][]{
                {"watching"},
                {"schaue"}
        };
        minimumRequiredRole = BlackRole.GUEST;
        clear = new Flag('c', "clear", new String[] {
                "Clears the current activity",
                "Löscht die aktuelle Aktivität"
        });
        source = new StringOption(new String[] {"media source", "Medienquelle"},
                new String[] {"What I should watch, given as text", "Was ich schauen soll, gegeben als Text"}, false
        );
        options = new ArrayList<>();
        flags = new ArrayList<>();

        options.add(source);
        flags.add(clear);
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        if (clear.isGiven()) {
            event.getJDA().getPresence().setActivity(null);
        } else {
            if (!source.hasValue() || source.getValue().trim().equals(""))
                throw new CommandExecutionException(new String[] {
                        "If you don't want to clear my activity a source is required",
                        "Wenn du meine Aktivität nicht löschen willst ist eine Quelle erforderlich"
                });
            event.getJDA().getPresence().setActivity(Activity.watching(source.getValue()));

        }
    }
}
