package Walter.commands;

import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class say extends Command {

    public say() {
        keywords = new String[]{"say", "echo"};
    }

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

//    @Override
//    public String[] execute(List<String> args, MessageReceivedEvent event) {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
