package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import Walter.Parsers.StringOption;
import Walter.RoleHandler;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class member extends Command {

    public member() {
        super(new String[] {
                "This command assigns the role *member* to the user with the name **NAME**. This also revokes the" +
                        " *guest*-role if the targeted user has it." +
                        Collection.CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH,
                "Dieser Command gibt dem User mit dem Namen **NAME** die *Member*-Rolle. Dies nimmt außerdem die " +
                        "*Guest*-Rolle falls der Ziel-User diese hat." +
                        Collection.CAUTION_QUOTES_FOR_WHITESPACE
        });
        keywords = new String[][]{
                {"member"},
                {"mitglied"}
        };
        minimumRequiredRole = BlackRole.MEMBER;
        options = new ArrayList<>();
        options.add(new StringOption("nickname", "nickname",
                "Visible nickname of user that should become member.",
                "Sichtbarer nickname des Users, der member werden soll."));
    }

    @Override
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        Member author = event.getMember();
        MessageChannel channel = event.getChannel();

        StringOption nick = (StringOption)options.get(0);

        String memberToSearchFor = nick.getValue();
        List<Member> foundMembers = Helper.instance.getMembersByName(memberToSearchFor);

        if (foundMembers.size() == 0)
            throw new CommandExecutionException("Ich habe niemanden mit dem Namen \"" + memberToSearchFor + "\" gefunden.",
                "I did not find anyone with the name \"" + memberToSearchFor + "\".");
        if (foundMembers.size() > 1)
            throw new CommandExecutionException("\"" + memberToSearchFor + "\" könnte auf mehrere Benutzer zutreffen.",
                    "\"" + memberToSearchFor + "\" matches several users.");

        Member memberToAssignTo = foundMembers.get(0);
        if (RoleHandler.instance.hasRole(memberToAssignTo, BlackRole.MEMBER))
            throw new CommandExecutionException("Der Benutzer \"" + memberToSearchFor + "\" ist bereits Member.",
                "The user \"" + memberToSearchFor + "\" already is a member.");

        RoleHandler.instance.assignRole(memberToAssignTo, BlackRole.MEMBER);
        if (RoleHandler.instance.hasRole(memberToAssignTo, BlackRole.GUEST))
            RoleHandler.instance.removeRole(memberToAssignTo, BlackRole.GUEST);
    }
}
