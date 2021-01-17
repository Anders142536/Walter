package Walter.commands;

import Walter.Helper;
import Walter.Parsers.StringOption;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class editmsg extends Command {
    StringOption messageID;
    StringOption newContent;

    public editmsg() {
        super(new String[] {
            "This command changes the content of the message with the ID **ID** to **TEXT**. To access the IDs " +
                "of messages you need to enable the developer settings under User Settings > Appearance > " +
                "Developer Mode. Then you can right click any message to access its ID. With this command you " +
                "can only edit messages that were sent by me.",
            "Dieser Command ändert den Inhalt der Nachricht mit der ID **ID** zu **TEXT**. Um die ID von Nachrichten " +
                "auszulesen zu können musst du die Developer-Settings aktivieren unter User Settings > Appearance " +
                "> Developer Mode. Dann kannst du mit einem Rechtsklick die ID von Nachrichten auslesen. Du " +
                "kannst hiermit nur Nachrichten editieren, die von mir verfasst worden sind."});
                keywords = new String[][]{
                {"editmsg"}};
        messageID = new StringOption(new String[] {"message ID", "Nachrichten ID"}, new String[] {
                "ID of the message to edit",
                "ID der zu editierenden Nachricht"
        });
        newContent = new StringOption(new String[] {"Content", "Inhalt"}, new String[] {
                "New content for the message",
                "Neuer Inhalt für die Nachricht"
        });
        options = new ArrayList<>();
        options.add(messageID);
        options.add(newContent);
    }

    @Override
    public void execute(String commandName, MessageReceivedEvent event) throws CommandExecutionException {
        try {
            event.getChannel().retrieveMessageById(messageID.getValue()).queue(
                    success -> {
                        success.editMessage(newContent.getValue()).queue(
                                editSuccess -> {
                                    event.getMessage().delete().queue();
                                },
                                editError -> {
                                    Helper.respondException(event, new CommandExecutionException(new String[]{
                                            "Something went wrong on editing the message:\n" + editError.getMessage()
                                    }));
                                }
                        );
                    },
                    error -> {
                        Helper.respondException(event, new CommandExecutionException(new String[]{
                                "Couldn't get the given message:\n" + error.getMessage()
                        }));
                    }
            );
        } catch (IllegalArgumentException e) {
            throw new CommandExecutionException(new String[] {
                    String.format("\"%s\" is not a message ID", messageID),
                    String.format("\"%s\" ist keine message ID", messageID)
            });
        }
    }
}
