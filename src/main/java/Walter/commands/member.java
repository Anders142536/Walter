package Walter.commands;

import Walter.Helper;
import Walter.Parsers.StringOption;
import Walter.RoleHandler;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class member extends Command {
    StringOption nick;

    public member() {
        super(new String[] {
                "This command assigns the role *member* to the user with the name **NAME**. This also revokes the" +
                        " *guest*-role if the targeted user has it",
                "Dieser Befehl gibt dem User mit dem Namen **NAME** die *Member*-Rolle. Dies nimmt außerdem die " +
                        "*Guest*-Rolle falls der Ziel-User diese hat"
        });
        keywords = new String[][]{
                {"member"},
                {"mitglied"}
        };
        minimumRequiredRole = BlackRole.MEMBER;
        nick = new StringOption(new String[] {"nickname", "Nickname"},
                new String[] {"Visible nickname of user that should become a member",
                        "Angezeigter Nickname des Benutzers der ein Mitglied werden soll"}
        );
        options = new ArrayList<>();
        options.add(nick);
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        String memberToSearchFor = nick.getValue();
        List<Member> foundMembers = Helper.getMembersByName(memberToSearchFor);

        if (foundMembers.size() == 0)
            throw new CommandExecutionException(new String[] {
                    "I did not find anyone with the name \"" + memberToSearchFor + "\"",
                    "Ich habe niemanden mit dem Namen \"" + memberToSearchFor + "\" gefunden"});
        if (foundMembers.size() > 1)
            throw new CommandExecutionException(new String[] {
                    "\"" + memberToSearchFor + "\" matches several users",
                    "\"" + memberToSearchFor + "\" könnte auf mehrere Benutzer zutreffen"});

        Member memberToAssignTo = foundMembers.get(0);
        if (RoleHandler.hasRole(memberToAssignTo, BlackRole.MEMBER))
            throw new CommandExecutionException(new String[] {
                    "The user \"" + memberToSearchFor + "\" already is a member",
                    "Der Benutzer \"" + memberToSearchFor + "\" ist bereits Member"});

        RoleHandler.assignRole(memberToAssignTo, BlackRole.MEMBER);
        if (RoleHandler.hasRole(memberToAssignTo, BlackRole.GUEST))
            RoleHandler.removeRole(memberToAssignTo, BlackRole.GUEST);
    }
}
