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
        String toSend = "__**Walter " + Walter.VERSION + "**__\n\n" +
                "**New Features**\n" +
                // enter new features between here..
                item("implemented !patch command") +
                item("implemented !roll command") +
                item("implemented !playing command") +
                item("implemented !watching command") +
                item("implemented !listening command") +
                item("messages sent into the news channel are now again being reposted by walter, nicely formatted") +
                item("newly joined users will now automatically get the guest role assigned") +
                item("in case a user does a mistake on using a command walter now automatically shows the help page of the command") +
                note("the help pages are designed to explain **how** to use the command and the error messages should tell " +
                        "you **what** you did wrong. until now the error messages told you both, which meant having almost " +
                        "exactly the same text in two different places of the code. So this should make everything more " +
                        "user friendly **and** easier to maintain. win-win!") +
                item("guests are now announced differently") +
                // ..and here
                "\n**Bug Fixes**\n" +
                // enter bugfixes between here..
                item("fixed issue with help texts of patch command displaying the news channel name incorrectly") +
                item("fixed issue with walter displaying english help text to german users and vice versa") +
                item("clarified some unclear error messages") +
                item("improved formatting of exception error messages") +
                item("slight code refactor") +
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
