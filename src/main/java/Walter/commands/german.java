package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class german extends Command {

    public german() {
        keywords = new String[]{"deutsch", "german"};
        minimumRequiredRole = Collection.GUEST_ROLE_ID;
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
    public String[] getKeywords() {
        return keywords;
    }
//
//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event, Helper helper) {
//    }
}
