package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class watching extends Command {

    public watching() {
        keywords = new String[]{"watching"};
        minimumRequiredRole = Collection.GUEST_ROLE_ID;
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
    public String[] getKeywords() {
        return keywords;
    }
//
//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event) {
//    }
}
