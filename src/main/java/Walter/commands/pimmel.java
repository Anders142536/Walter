package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class pimmel extends Command {

    public pimmel() {
        keywords = new String[]{"pimmel", "pimmelberger", "dick", "dickhead"};
        mainKeywordEnglish = 2;
        minimumRequiredRole = Collection.GUEST_ROLE_ID;
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " NAME",
                "Dieser Command gibt dem User mit dem Namen **NAME** die *Pimmel*-Rolle. Wenn der Ziel-User die " +
                        "*Pimmel*-Rolle bereits hat wird sie ihm genommen." +
                        Collection.CAUTION_QUOTES_FOR_WHITESPACE};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " NAME",
                "This command assigns the role *Pimmel* to the user with the name **NAME**. If the targeted user " +
                        "already has the *Pimmel* role it will be revoked." +
                        Collection.CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event) {
//    }
}
