package Walter;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

//this class represents a command, including help texts
public abstract class Command {

    protected String[] keywords = {"(╯°Д°）╯︵ ┻━┻"};
    protected long minimumRequiredRole = Collection.ADMIN_ROLE_ID;

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
    public void execute(List<String> args, MessageReceivedEvent event, Helper helper) {
        System.out.println("default execution of command");
        Member author = helper.getMember(event.getAuthor());
        helper.respond(author, event.getChannel(), Collection.NOT_YET_IMPLEMENTED, Collection.NOT_YET_IMPLEMENTED_ENGLISH);
    }
}
