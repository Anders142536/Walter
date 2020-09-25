package Walter.commands;

import Walter.Collection;
import Walter.Parsers.StringOption;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class watching extends Command {

    public watching() {
        super(new String[] {
                "This command changes what Walter is watching to **media source**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE,
                "Dieser Command ändert was Walter gerade schaut zu **Medienquelle**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE
        });
        keywords = new String[][]{
                {"watching"},
                {"schaue"}
        };
        minimumRequiredRole = BlackRole.GUEST;
        options = new ArrayList<>();
        options.add(new StringOption("media source", "medienquelle",
                "What I should watch.", "Was ich schauen soll"));
    }

    @Override
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        StringOption media = (StringOption)options.get(0);
        event.getJDA().getPresence().setActivity(Activity.watching(media.getValue()));
    }
}
