package Walter.commands;

import Walter.Helper;
import Walter.RoleHandler;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class german extends Command {

    public german() {
        super(new String[] {
                "This command revokes your role *English*, switching the Info-channel and the language I speak to you" +
                        "in to german. This is the opposite of !english.",
                "Dieser Command nimmt dir die Rolle *English*. Dadurch wird der Info-Channel auf Deutsch angezeigt und " +
                        "ich spreche Deutsch mit dir. Dies ist das Gegenstück zu !englisch."
        });
        keywords = new String[][]{
                {"german"},
                {"deutsch"}
        };
        minimumRequiredRole = BlackRole.GUEST;
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        Member author = event.getMember();
        MessageChannel channel = event.getChannel();

        if (RoleHandler.hasRole(author, BlackRole.ENGLISH)) {
            RoleHandler.removeRole(author, BlackRole.ENGLISH);
            Helper.respond(author, channel,
                    "Ich kommuniziere nun mit dir in Deutsch.",
                    "I will now communicate with you in german.");
        } else
            Helper.respond(author, channel,
                    "Ich kommuniziere bereits mit dir in Deutsch.",
                    "I already communicate with you in german.");
    }
}