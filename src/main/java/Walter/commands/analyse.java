package Walter.commands;

import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class analyse extends Command {

    public analyse() {
        keywords = new String[]{"analyse", "analyze"};
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                "",
                "Ich analysiere die von mir geschriebenen log-Datein und schreibe das Ergebnis sowohl in eine " +
                        "Ergebnis-Datei als auch in den Chat."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                "",
                "I analyse the log files I have written and write the result both into a respective result file and the chat."};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event, Helper helper) {
//    }
}
