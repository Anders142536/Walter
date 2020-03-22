package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class hello extends Command {

    public hello () {
        keywords = new String[]{"hallo", "hello", "hi", "hey", "hola",
                "moin", "servus", "griaßdi", "grüzi", "tag"};
        minimumRequiredRole = Collection.GUEST_ROLE_ID;
        mainKeywordEnglish = 1;
    }
    @Override
    public String[] getHelp() {
        return new String[]{
                " Walter",
                "Hiermit grüßt du mich höflich und ich grüße zurück."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " Walter",
                "You greet me politely, with me greeting you back."};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event) {
//    }
}
