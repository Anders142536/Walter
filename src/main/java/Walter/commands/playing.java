package Walter.commands;

import Walter.Collection;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class playing extends Command {

    public playing() {
        keywords = new String[]{"playing"};
        minimumRequiredRole = BlackRole.GUEST;
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " TEXT",
                "Dieser Command ändert das Spiel, das Walter gerade spielt, zu **TEXT**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " TEXT",
                "This command changes the game Walter is playing to **TEXT**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH};
    }

    @Override
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        if (parseResult.size() > 1 && !parseResult.get(1).trim().equals("")) event.getJDA().getPresence().setActivity(Activity.playing(parseResult.get(1)));
        else return new String[]{"Mir wurde nichts zu spielen gegeben.", "I was not given something to watch."};
        return null;
    }
}
