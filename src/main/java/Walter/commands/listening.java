package Walter.commands;

import Walter.Collection;
import Walter.entities.BlackRole;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class listening extends Command {

    public listening() {
        keywords = new String[]{"listening"};
        minimumRequiredRole = BlackRole.GUEST;
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " TEXT",
                "Dieser Command ändert was Walter gerade hört zu **TEXT**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " TEXT",
                "This command changes what Walter is listening to to **TEXT**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH};
    }

    @Override
    public String[] execute(List<String> args, MessageReceivedEvent event) {
        if (args.size() > 1 && !args.get(1).trim().equals("")) event.getJDA().getPresence().setActivity(Activity.listening(args.get(1)));
        else return new String[]{"Mir wurde nichts zu hören gegeben.", "I was not given anything to listen to."};
        return null;
    }
}
