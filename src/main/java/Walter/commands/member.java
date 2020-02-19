package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class member extends Command {

    public member() {
        keywords = new String[]{"member", "mitglied"};
        minimumRequiredRole = Collection.MEMBER_ROLE_ID;
        mainKeywordGerman = 1;
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " NAME",
                "Dieser Command gibt dem User mit dem Namen **NAME** die *Member*-Rolle. Dies nimmt außerdem die " +
                        "*Guest*-Rolle falls der Ziel-User diese hat." +
                        Collection.CAUTION_QUOTES_FOR_WHITESPACE};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " NAME",
                "This command assigns the role *member* to the user with the name **NAME**. This also revokes the" +
                        " *guest*-role if the targeted user has it." +
                        Collection.CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

    @Override
    public void execute(List<String> args, MessageReceivedEvent event, Helper helper) {
        Member author = event.getMember();
        MessageChannel channel = event.getChannel();

        if (args.size() < 2) {
            helper.respond(author, channel,
                    "Es tut mir Leid, doch mir wurde kein Name gegeben.",
                    "I am utterly sorry, but I was not given a name.");
            return;
        }

        String memberToSearchFor = args.get(1);
        List<Member> foundMembers = helper.getMembersByName(memberToSearchFor);

        if (foundMembers.size() == 0) helper.respond(author, channel,
                "Es tut mir Leid, doch ich habe niemanden mit dem Namen \"" + memberToSearchFor + "\" gefunden.",
                "I am utterly sorry, but I did not find anyone with the name \"" + memberToSearchFor + "\".");
        else if (foundMembers.size() > 1) helper.respond(author, channel,
                "Es tut mir Leid, doch \"" + memberToSearchFor + "\" könnte auf mehrere Benutzer zutreffen.",
                "I am utterly sorry, but \"" + memberToSearchFor + "\" could mean several users.");
        else {
            Member toAssign = foundMembers.get(0);
            if (helper.hasRole(toAssign, Collection.MEMBER_ROLE_ID)) helper.respond(author, channel,
                    "Es tut mir Leid, doch der Benutzer \"" + memberToSearchFor + "\" ist bereits Member.",
                    "I am utterly sorry, but the user \"" + memberToSearchFor + "\" already is a member.");
            else {
                helper.assignRole(toAssign, Collection.MEMBER_ROLE_ID);
                if (helper.hasRole(toAssign, Collection.GUEST_ROLE_ID)) helper.removeRole(toAssign, Collection.GUEST_ROLE_ID);
            }
        }
    }
}
