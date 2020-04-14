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
    public void execute(List<String> args, MessageReceivedEvent event) {
        String itemizer = ":small_orange_diamond: ";    //keep white space at the end to avoid formatting issues
        String toSend = "__**Patchnotes Walter " + Walter.VERSION + "**__\n\n" +
                // enter patch notes between here..
                itemizer + "implemented !patch command\n" +
                itemizer + "implemented !roll command\n" +
                itemizer + "fixed issue with help texts of patch command displaying the news channel name incorrectly" +
                itemizer + "slight code refactor\n" +
                // ..and here
                "\nIn case you encounter any issues or wish for new features please contact <@!151010441043116032>";

        BlackWebhook.PATCHNOTES.sendMessage(toSend);
    }
}
