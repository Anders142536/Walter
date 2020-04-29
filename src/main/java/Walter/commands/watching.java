package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import Walter.enums.BlackRole;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class watching extends Command {

    public watching() {
        keywords = new String[]{"watching"};
        minimumRequiredRole = BlackRole.GUEST;
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " TEXT",
                "Dieser Command ändert was Walter gerade schaut zu **TEXT**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " TEXT",
                "This command changes what Walter is watching to **TEXT**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE};
    }

    @Override
    public String[] execute(List<String> args, MessageReceivedEvent event) {
        if (args.size() > 1 && !args.get(1).trim().equals("")) event.getJDA().getPresence().setActivity(Activity.watching(args.get(1)));
        else return new String[]{"Mir wurde nichts zu schauen gegeben.", "I was not given something to watch."};
        return null;
    }
}
