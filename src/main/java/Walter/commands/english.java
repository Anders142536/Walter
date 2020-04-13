package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import Walter.RoleHandler;
import Walter.RoleID;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class english extends Command {

    public english() {
        keywords = new String[]{"english", "englisch"};
        minimumRequiredRole = RoleID.GUEST;
        mainKeywordGerman = 1;
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                "",
                "Dieser Command gibt dir die Rolle *English*. Dadurch wird der Info-Channel auf Englisch angezeigt" +
                        " und ich spreche Englisch mit dir. Dies ist das Gegenstück zu !deutsch."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                "",
                "This command gives you the role *English*, switching the Info-channel and the language I speak to you" +
                        "in to english. This is the opposite of !german."};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

    @Override
    public void execute(List<String> args, MessageReceivedEvent event) {
        Member author = event.getMember();
        MessageChannel channel = event.getChannel();

        if (!RoleHandler.instance.hasRole(author, RoleID.ENGLISH)) {
            RoleHandler.instance.assignRole(author, RoleID.ENGLISH);
            Helper.instance.respond(author, channel,
                    "Ich kommuniziere nun mit dir in Englisch.",
                    "I will now communicate with you in english.");
        } else
            Helper.instance.respond(author, channel,
                    "Ich kommuniziere bereits mit dir in Englisch.",
                    "I already communicate with you in english.");
    }
}