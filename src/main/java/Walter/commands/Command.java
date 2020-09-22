package Walter.commands;

import Walter.Parsers.Flag;
import Walter.Parsers.Option;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

//this class represents a command, including help texts
public abstract class Command {

    String[][] keywords;
    List<Option> options;
    List<Flag> flags;
    BlackRole minimumRequiredRole = BlackRole.ADMIN;
    int mainKeywordGerman;
    int mainKeywordEnglish;

    //returns the help string
    public String[] getDescription() {
        return new String[]{"Dieser Command hat keinen eigenen Hilfe-Text.",
                "This command does not have a help text."};
    }

    public String[][] getKeywords() {
        return keywords;
    }

    public BlackRole getMinimumRequiredRole() {
        return minimumRequiredRole;
    }

    public List<Option> getOptions() { return options; }

    public List<Flag> getFlags() { return flags; }

    //executes the command with the given parameters
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        throw new CommandExecutionException("This command is not yet implemented. Please report to <@!151010441043116032> so he can implement this.",
                "Es tut mir Leid, doch dies ist noch nicht implementiert. Bitte melde dies <@!151010441043116032> damit er es implementieren kann.");
    }
}
