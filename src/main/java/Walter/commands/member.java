package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import Walter.RoleHandler;
import Walter.enums.BlackRole;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class member extends Command {

    public member() {
        keywords = new String[]{"member", "mitglied"};
        minimumRequiredRole = BlackRole.MEMBER;
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
    public String[] execute(List<String> args, MessageReceivedEvent event) {
        Member author = event.getMember();
        MessageChannel channel = event.getChannel();

        if (args.size() < 2) return new String[]{"Mir wurde kein Name gegeben.", "I was not given a name."};

        String memberToSearchFor = args.get(1);
        List<Member> foundMembers = Helper.instance.getMembersByName(memberToSearchFor);

        if (foundMembers.size() == 0) return new String[]{"Ich habe niemanden mit dem Namen \"" + memberToSearchFor + "\" gefunden.",
                "I did not find anyone with the name \"" + memberToSearchFor + "\"."};
        else if (foundMembers.size() > 1) return new String[] {memberToSearchFor + "\" könnte auf mehrere Benutzer zutreffen.",
                memberToSearchFor + "\" matches several users."};
        else {
            Member memberToAssignTo = foundMembers.get(0);
            if (RoleHandler.instance.hasRole(memberToAssignTo, BlackRole.MEMBER)) return new String[]{"Der Benutzer \"" + memberToSearchFor + "\" ist bereits Member.",
                    "The user \"" + memberToSearchFor + "\" already is a member."};
            else {
                RoleHandler.instance.assignRole(memberToAssignTo, BlackRole.MEMBER);
                if (RoleHandler.instance.hasRole(memberToAssignTo, BlackRole.GUEST)) RoleHandler.instance.removeRole(memberToAssignTo, BlackRole.GUEST);
            }
        }
        return null;
    }
}
