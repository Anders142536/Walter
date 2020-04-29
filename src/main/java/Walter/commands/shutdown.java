package Walter.commands;

import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class shutdown extends Command {

    public shutdown() {
        keywords = new String[]{"shutdown"};
    }

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

//    @Override
//    public String[] execute(List<String> args, MessageReceivedEvent event) {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
