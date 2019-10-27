package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class patch extends Command {

    public patch() {
        keywords = new String[]{"patch"};
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " TEXT",
                "Dieser Command schreibt den Text **TEXT** in <#" + Collection.NEWS_CHANNEL_ID + "> mit Hilfe des Patchnotes-Webhook."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " TEST",
                "This command writes the text **TEXT** into <#" + Collection.NEWS_CHANNEL_ID + "> using the Patchnotes webhook."};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }
//
//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event, Helper helper) {
//    }
}
