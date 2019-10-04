package Walter.commands;

import Walter.Collection;
import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.api.events.Event;

import java.util.List;

public class member extends Command {

    String[] keywords = {"member", "mitglied"};

    @Override
    public String[] getHelp() {
        return new String[]{
                " NAME",
                "Dieser Command gibt dem User mit dem Namen **NAME** die *Member*-Rolle. Dies nimmt außerdem die " +
                        "*Guest*-Rolle falls der Ziel-User diese hat." +
                        Collection.CAUTION_QUOTES_FOR_WHITESPACE};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " NAME",
                "This command assigns the role *member* to the user with the name **NAME**. This also revokes the" +
                        " *guest*-role if the targeted user has it." +
                        Collection.CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

    @Override
    public int execute(List<String> args, Event event, Helper helper) {
        return -1;
    }
}
