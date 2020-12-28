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

public class guest extends Command {
    StringOption nick;

    public guest() {
        super(new String[] {
                "This command assigns the role *guest* to the user with the name **NAME**.",
                "Dieser Command gibt dem User mit dem Namen **NAME** die *Guest*-Rolle."
        });
        keywords = new String[][]{
                {"guest"},
                {"gast"}
        };
        minimumRequiredRole = BlackRole.GUEST;
        nick = new StringOption(new String[] {"nickname", "Nickname"},
                new String[] {"Visible nickname of user that should become a guest",
                        "Angezeigter Nickname des Benutzers der ein Gast werden soll"}
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
                    "The user \"" + memberToSearchFor + "\" cannot be degraded from member to guest",
                    "Der Benutzer \"" + memberToSearchFor + "\" kann nicht von Mitglied zu Gast degradiert werden"});

        if (RoleHandler.hasRole(memberToAssignTo, BlackRole.GUEST))
            throw new CommandExecutionException(new String[] {
                    "The user \"" + memberToSearchFor + "\" already is a guest",
                    "Der Benutzer \"" + memberToSearchFor + "\" ist bereits Gast"});

        RoleHandler.assignRole(memberToAssignTo, BlackRole.GUEST);
    }
}
