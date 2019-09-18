package Walter.commands;

import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.core.events.Event;

import java.util.List;

public class hello extends Command {

    String[] keywords = {"hallo", "hello", "hi", "hey", "hola",
            "moin", "servus", "griaßdi", "grüzi", "tag"};

    @Override
    public String[] getHelp() {
        return new String[]{
                " Walter",
                "Hiermit grüßt du mich höflich und ich grüße zurück."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " Walter",
                "You greet me politely, with me greeting you back."};
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
