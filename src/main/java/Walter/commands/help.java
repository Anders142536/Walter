package Walter.commands;

import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.core.events.Event;

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
    public int execute(List<String> args, Event event, Helper helper) {
        return -1;
    }
}
