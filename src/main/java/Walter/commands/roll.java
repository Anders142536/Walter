package Walter.commands;

import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.api.events.Event;

import java.util.List;

public class roll extends Command {

    String[] keywords = {"roll", "würfel"};

    @Override
    public String[] getHelp() {
        return new String[]{
                " NUMMER",
                "Ich gebe dir eine Zufallszahl zwischen 1 und **NUMMER**. Wenn keine **NUMMER** angegeben ist, gebe ich" +
                        "dir eine Zufallszahl zwischen 1 und 6."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " NUMBER",
                "I give you a random number between 1 and **NUMBER**. If no **NUMBER** is given I give you a random " +
                        "number between 1 and 6."};
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
