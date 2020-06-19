package Walter.commands;

import Walter.Collection;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Activity;

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
    public void execute() throws CommandExecutionException {
        if (parseResult.size() > 1 && !parseResult.get(1).trim().equals("")) event.getJDA().getPresence().setActivity(Activity.listening(parseResult.get(1)));
        else return new String[]{"Mir wurde nichts zu hören gegeben.", "I was not given anything to listen to."};
        return null;
    }
}
