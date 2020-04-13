package Walter.commands;

import Walter.ChannelID;
import Walter.Collection;
import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class config extends Command {

    public config() {
        keywords = new String[]{"config", "konfig"};
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " SCHLÜSSEL WERT",
                "Dieser Command ändert den Wert der Variable **SCHLÜSSEL** zum Wert **WERT**. Diese sind in <#" +
                        ChannelID.CONFIG+ "> aufgelistet."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " KEY VALUE",
                "This command changes the variable with the name **KEY** to the value **VALUE**. Those are displayed in <#" +
                        ChannelID.CONFIG + ">."};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event) {
//    }
}
