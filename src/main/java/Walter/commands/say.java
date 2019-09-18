package Walter.commands;

import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.core.events.Event;

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
    public int execute(List<String> args, Event event, Helper helper) {
        return -1;
    }
}
