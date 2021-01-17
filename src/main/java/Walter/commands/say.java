package Walter.commands;

import Walter.Parsers.FlushOption;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class say extends Command {

    FlushOption text;

    public say() {
        super(new String[] {
                "I post the message **TEXT** into the chat.",
                "Ich poste die Nachricht **TEXT** in den Chat."
        });
        keywords = new String[][]{
                {"say", "echo"}
        };
        minimumRequiredRole = BlackRole.MEMBER;
        text = new FlushOption(new String[] {"text"}, new String[] {
                "Text I should say",
                "Text, den ich sagen soll"
        });
        options = new ArrayList<>();
        options.add(text);
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) {
        event.getChannel().sendMessage(text.getValue()).queue();
        if (event.getChannelType().isGuild())
            event.getMessage().delete().queue();
    }
}
