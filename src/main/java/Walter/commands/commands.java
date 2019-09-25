package Walter.commands;

import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.core.events.Event;

import java.util.List;

public class commands extends Command {

    //String[] keywords = {"command", "commands"};    //can these be moved to a constructor?

    public commands() {
        keywords = {"command", "commands"};
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                "",
                "Dieser Command listet alle dir zur Verfügung stehenden Commands auf."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                "",
                "This command lists all the commands available to you."};
    }

    // //this can probably be returned on the abstract class already, avoiding having the same code 20 times
    // @Override
    // public String[] getKeywords() {
    //     return keywords;
    // }

    @Override
    public int execute(List<String> args, Event event, Helper helper) {
        return -1;
    }
}
