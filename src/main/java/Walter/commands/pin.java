package Walter.commands;

import Walter.Helper;
import Walter.Parsers.StringOption;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class pin extends Command {

    StringOption messageID;

    public pin() {
        super(new String[] {
                "Pins the message with the ID **ID**. If it is already pinned it is unpinned.",
                "Pinnt die Nachricht mit der ID **ID** an. Wenn sie bereits " +
                        "angepinnt ist wird sie von den gepinnten Nachrichten entfernt."
        });
        minimumRequiredRole = BlackRole.MEMBER;
        keywords = new String[][] {
                {"pin"},
                {"pinne"}
        };
        messageID = new StringOption(new String[] {"message ID", "Nachrichten ID"}, new String[] {
                "ID of the message to pin",
                "ID der anzupinnenden Nachricht"
        });
        options = new ArrayList<>();
        options.add(messageID);
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        event.getChannel().retrieveMessageById(messageID.getValue()).queue(
                success -> {
                    //no response to user, wont be used that much and if it fails, we let it crash
                    if (success.isPinned()) success.unpin().queue();
                    else success.pin().queue();
                },
                error -> {
                    Helper.instance.respondError(event, new CommandExecutionException(new String[]{
                            "Couldn't get the given message. It has to be in the same " +
                                    "channel as your " + usedKeyword + " command!",
                            "Konnte die gegebene Nachricht nicht finden. Sie muss im selben Kanal sein wie " +
                                    "dein " + usedKeyword + " Befehl!"
                    }));}
        );
    }
}
