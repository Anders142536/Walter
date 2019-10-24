package Walter.commands;

import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class file extends Command {

    String[] keywords = {"file", "datei"};

    @Override
    public String[] getHelp() {
        return new String[]{
                " DATEINAME",
                "Ich poste die Datei mit dem Namen **DATEINAME** in den Chat. Du musst die Dateierweiterung mit angeben. " +
                        "Weiters muss sich die Datei auf dem Raspberry, auf dem ich laufe, im Ordner walter befinden."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " FILENAME",
                "I post the file with the name **FILENAME** into the chat. You must include file extentions. Futhermore " +
                        "the file must be present on the raspberry i run on in the folder walter."};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

    @Override
    public void execute(List<String> args, MessageReceivedEvent event, Helper helper) {
    }
}
