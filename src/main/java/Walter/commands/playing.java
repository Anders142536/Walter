package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class playing extends Command {

    public playing() {
        keywords = new String[]{"playing"};
        minimumRequiredRole = Collection.GUEST_ROLE_ID;
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
    public String[] getKeywords() {
        return keywords;
    }

//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event) {
//    }
}
