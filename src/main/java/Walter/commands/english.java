package Walter.commands;

import Walter.Helper;
import Walter.RoleHandler;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class english extends Command {

    public english() {
        super(new String[] {
                "This command gives you the role *English*, switching the Info-channel and the language I speak to you" +
                        "in to english. This is the opposite of !german.",
                "Dieser Command gibt dir die Rolle *English*. Dadurch wird der Info-Channel auf Englisch angezeigt" +
                        " und ich spreche Englisch mit dir. Dies ist das Gegenstück zu !deutsch."});
        keywords = new String[][]{
                {"english"},
                {"englisch"}};
        minimumRequiredRole = BlackRole.GUEST;
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        Member author = event.getMember();
        MessageChannel channel = event.getChannel();

        if (!RoleHandler.hasRole(author, BlackRole.ENGLISH)) {
            RoleHandler.assignRole(author, BlackRole.ENGLISH);
            Helper.respond(author, channel,
                    "Ich kommuniziere nun mit dir in Englisch.",
                    "I will now communicate with you in english.");
        } else
            Helper.respond(author, channel,
                    "Ich kommuniziere bereits mit dir in Englisch.",
                    "I already communicate with you in english.");
    }
}