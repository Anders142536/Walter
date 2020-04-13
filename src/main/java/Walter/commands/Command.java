package Walter.commands;

import Walter.Helper;
import Walter.enums.BlackRole;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

//this class represents a command, including help texts
public abstract class Command {

    protected String[] keywords;
    BlackRole minimumRequiredBlackRole = BlackRole.ADMIN;
    int mainKeywordGerman;
    int mainKeywordEnglish;

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

    public BlackRole getMinimumRequiredBlackRole() {
        return minimumRequiredBlackRole;
    }

    //executes the command with the given parameters
    //returns an integer number indicating how the command executed
    public void execute(List<String> args, MessageReceivedEvent event) {
        Member author = Helper.instance.getMember(event.getAuthor());
        Helper.instance.respond(author, event.getChannel(),
                "Es tut mir Leid, doch dies ist noch nicht implementiert. Bitte melde dies <@!151010441043116032> damit er es implementieren kann.",
                "I am utterly sorry, but this is not yet implemented. Please report to <@!151010441043116032> so he can implement this.");
    }
}
