package Walter.commands;

import Walter.Collection;
import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.api.events.Event;

import java.util.List;

public class config extends Command {

    String[] keywords = {"config", "konfig"};

    @Override
    public String[] getHelp() {
        return new String[]{
                " SCHLÜSSEL WERT",
                "Dieser Command ändert den Wert der Variable **SCHLÜSSEL** zum Wert **WERT**. Diese sind in <#" +
                        Collection.CONFIG_CHANNEL_ID + "> aufgelistet." +
                        Collection.CAUTION_QUOTES_FOR_WHITESPACE};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " KEY VALUE",
                "This command changes the variable with the name **KEY** to the value **VALUE**. Those are displayed in <#" +
                        Collection.CONFIG_CHANNEL_ID + ">." +
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
