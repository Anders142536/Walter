package Walter;


import net.dv8tion.jda.core.events.Event;

import java.util.List;

//this class represents a command, including help texts
public abstract class Command {

    String[] keywords = {"(╯°Д°）╯︵ ┻━┻"};

    //returns the help string
    public String[] getHelp() {
        return new String[]{
                "",
                "Dieser Command hat keinen eigenen Hilfe-Text."};
    }

    //returns the english help string
    public String[] getHelpEnglish() {
        return new String[]{
                "",
                "This command does not have a help text."};
    }

    //returns a string array of keywords
    public String[] getKeywords() {
        return keywords;
    }

    //executes the command with the given parameters
    //returns an integer number indicating how the command executed
    public abstract int execute(List<String> args, Event event, Helper helper);
}
