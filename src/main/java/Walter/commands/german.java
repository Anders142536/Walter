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
        keywords = new String[]{"deutsch", "german"};
        minimumRequiredRole = BlackRole.GUEST;
        mainKeywordEnglish = 1;
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                "",
                "Dieser Command nimmt dir die Rolle *English*. Dadurch wird der Info-Channel auf Deutsch angezeigt und " +
                        "ich spreche Deutsch mit dir. Dies ist das Gegenstück zu !englisch."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                "",
                "This command revokes your role *English*, switching the Info-channel and the language I speak to you" +
                        "in to german. This is the opposite of !english."};
    }

    @Override
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        Member author = event.getMember();
        MessageChannel channel = event.getChannel();

        if (RoleHandler.instance.hasRole(author, BlackRole.ENGLISH)) {
            RoleHandler.instance.removeRole(author, BlackRole.ENGLISH);
            Helper.instance.respond(author, channel,
                    "Ich kommuniziere nun mit dir in Deutsch.",
                    "I will now communicate with you in german.");
        } else
            Helper.instance.respond(author, channel,
                    "Ich kommuniziere bereits mit dir in Deutsch.",
                    "I already communicate with you in german.");
        return null;
    }
}