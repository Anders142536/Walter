package Walter.commands;

import Walter.Walter;
import Walter.entities.BlackChannel;
import Walter.entities.BlackWebhook;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class patch extends Command {

    public patch() {
        super(new String[] {
                "This command writes hardcoded patch notes into <#" + BlackChannel.NEWS.ID + "> using the Patchnotes webhook.",
                "Dieser Command schreibt hardcodierte patch notes in <#" + BlackChannel.NEWS.ID + "> mit Hilfe des Patchnotes-Webhook."
        });
        keywords = new String[][]{new String[]{"patch"}};
    }

    @Override
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        String toSend = getPatchMessage();

        BlackWebhook.PATCHNOTES.sendMessage(toSend);
    }

    public String getPatchMessage() {
        return "__**Walter " + Walter.VERSION + " Parser Update**__\n" +
                "\n**New Features**\n" +
                // enter new features between here..
                // ..and here
                "\n**Bug Fixes & Improvements**\n" +
                // enter bugfixes between here..
                item("Fixed issue with walter throwing an error on sending a message to the dropzone") +
                note("As a bot it is only possible to fetch a maximum of 100 messages at a time from a channel. The amount fetched is " +
                        "defined in a setting on walter, and this was more than 100. Now the amount caps at 100.") +
                item("All commands can now again be used in direct messages with Walter, instead of just the server channels.") +
                item("The config message visible only to admins is now displayed correctly.") +
                // ..and here
                "\nIn case you encounter any issues, have any questions or wish for new features please contact <@!151010441043116032>";
    }

    private String item(String text) {
        return ":small_orange_diamond: " + text + "\n";
    }

    private String note(String text) {
        return "    *" + text + "*\n";
    }
}
