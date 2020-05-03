package Walter.commands;

import Walter.Walter;
import Walter.enums.BlackChannel;
import Walter.enums.BlackWebhook;
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
                "Dieser Command schreibt hardcodierte patch notes in <#" + BlackChannel.NEWS.ID + "> mit Hilfe des Patchnotes-Webhook."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " TEST",
                "This command writes hardcoded patch notes into <#" + BlackChannel.NEWS.ID + "> using the Patchnotes webhook."};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

    @Override
    public String[] execute(List<String> args, MessageReceivedEvent event) {
        String toSend = "__**Walter " + Walter.VERSION + "**__\n" +
//                "\n**New Features**\n" +
                // enter new features between here..

                // ..and here
                "\n**Bug Fixes**\n" +
                // enter bugfixes between here..
                item("fixed issue with walter tagging twice in the dropzone") +
                item("fixed issue with walter tagging in dropzone if a command was used") +
                // ..and here
                "\nIn case you encounter any issues or wish for new features please contact <@!151010441043116032>";

        BlackWebhook.PATCHNOTES.sendMessage(toSend);
        return null;
    }

    private String item(String text) {
        return ":small_orange_diamond: " + text + "\n";
    }

    private String note(String text) {
        return "    *" + text + "*\n";
    }
}
