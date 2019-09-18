package Walter.commands;

import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.core.events.Event;

import java.util.List;

public class shutdown extends Command {

    String[] keywords = {"shutdown"};

    @Override
    public String[] getHelp() {
        return new String[]{
                "",
                "Ich fahre herunter. Ich kann danach nur noch über eine SSH Verbindung erneut gestartet werden."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                "",
                "I shut down. I can only be started again via an SSH connection."};
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
