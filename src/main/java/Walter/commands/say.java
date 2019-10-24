package Walter.commands;

import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class say extends Command {

    String[] keywords = {"say", "echo"};

    @Override
    public String[] getHelp() {
        return new String[]{
                " TEXT",
                "Ich poste die Nachricht **TEXT** in den Chat."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " TEXT",
                "I post the message **TEXT** into the chat."};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

    @Override
    public void execute(List<String> args, MessageReceivedEvent event, Helper helper) {
    }
}
