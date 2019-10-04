package Walter.commands;

import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.api.events.Event;

import java.util.List;

public class german extends Command {

    String[] keywords = {"deutsch", "german"};

    @Override
    public String[] getHelp() {
        return new String[]{
                "",
                "Dieser Command nimmt dir die Rolle *English*. Dadurch wird der Info-Channel auf Deutsch angezeigt und " +
                        "ich spreche Deutsch mit dir. Dies ist das Gegenstück zu !englisch."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                "",
                "This command revokes your role *English*, switching the Info-channel and the language I speak to you" +
                        "in to german. This is the opposite of !english."};
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
