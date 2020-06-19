package Walter.commands;

import Walter.Walter;
import Walter.entities.BlackChannel;
import Walter.entities.BlackWebhook;
import Walter.exceptions.CommandExecutionException;

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
    public void execute() throws CommandExecutionException {
        String toSend = "__**Walter " + Walter.VERSION + " Parser Update**__\n" +
                "\n**New Features**\n" +
                // enter new features between here..
                item("From now on Updates will have titles/themes") +
                note("Until now a version increment (2.4.* -> 2.5.0) was done whenever a somewhat" +
                        " big-ish feature was added. Now every update will have a theme or concept that it will" +
                        " focus on. This helps me writing this shit and hopefully you understanding this shit.") +
                item("Complete rewrite of how Walter understands commands") +
                note("Until now walters approach to parsing commands was... primitive, to say the least. The" +
                        " new approach now orients itself way closer to a few professional command line parsers that are " +
                        "open source, mainly **Apache CLI**.\n" +
                        "Before Walter identified a command by checking the first sign in a message for being ! or ?." +
                        " The rest of the message was then split at whitespace and arguments bracketed in quotes. " +
                        "This behaviour caused a lot of checks and half of the code of most commands was catching user " +
                        "mistakes. This is now way more streamlined.\n") +
                item("Commands are now more standardized to how commands are usually designed and standardized.") +
                note("The changes mostly affect admin-exclusive commands. The commands that members or guests may use" +
                        " whose behaviour changed are listed below:") +
                item("On detected user error admins are now tagged so they can more effectively help you.") +
                // ..and here
                "\n**Bug Fixes & Improvements**\n" +
                // enter bugfixes between here..
                item("User errors are now caught differently") +
                note("Mostly done to avoid having \"I am utterly sorry, but...\" written in the code **EVERYWHERE**") +
                item("Changed various wordings.") +
                // ..and here
                "\nIn case you encounter any issues, have any questions or wish for new features please contact <@!151010441043116032>";

        BlackWebhook.PATCHNOTES.sendMessage(toSend);
    }

    private String item(String text) {
        return ":small_orange_diamond: " + text + "\n";
    }

    private String note(String text) {
        return "    *" + text + "*\n";
    }
}
