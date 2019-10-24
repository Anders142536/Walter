package Walter.commands;

import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class help extends Command {

    String[] keywords = {"help", "hilfe"};

    @Override
    public String[] getHelp() {
        return new String[]{
                "",
                "Dieser Command zeigt dir die allgemeine Hilfe-Seite an mit einer kurzen Erklärung wo du Informationen findest."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                "",
                "This command displays the general help page with a short explanation where you can find information."};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

    @Override
    public void execute(List<String> args, MessageReceivedEvent event, Helper helper) {
    }
}
