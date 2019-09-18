package Walter;


import net.dv8tion.jda.core.events.Event;

import java.util.List;

//this class represents a command, including help texts
public abstract class Command {

    //returns the help string
    public abstract String[] getHelp();

    //returns the english help string
    public abstract String[] getHelpEnglish();

    //returns a string array of keywords
    public abstract String[] getKeywords();

    //executes the command with the given parameters
    //returns an integer number indicating how the command executed
    public abstract int execute(List<String> args, Event event, Helper helper);
}
