package Walter.commands;

import Walter.Collection;
import Walter.Parsers.Flag;
import Walter.Parsers.StringOption;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class listening extends Command {
    Flag clear;
    StringOption source;

    public listening() {
        super(new String[] {
                "This command changes what Walter is listening to to **TEXT**"
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH,
                "Dieser Command ändert was Walter gerade hört zu **TEXT**"
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE
        });
        keywords = new String[][]{
                {"listening"},
                {"höre", "hoere"}
        };
        minimumRequiredRole = BlackRole.GUEST;
        clear = new Flag('c', "clear", new String[] {
                "Clears the current activity",
                "Löscht die aktuelle Aktivität"
        });
        source = new StringOption(new String[] {"soundsource", "tonquelle"},
                new String[] {"What I should listen to, given as text", "Was ich hören soll, gegeben als Text"}, false
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
            event.getJDA().getPresence().setActivity(Activity.listening(source.getValue()));

        }
    }
}
