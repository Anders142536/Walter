package Walter.commands;

import Walter.Helper;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class hello extends Command {

    public hello () {
        super(new String[] {
                "You greet me politely, with me greeting you back.",
                "Hiermit grüßt du mich höflich und ich grüße zurück."
        });
        keywords = new String[][]{
                {"hi", "hallo", "hello", "hey", "hola",
                "moin", "servus", "griasdi", "grüzi", "tag"}};
        minimumRequiredRole = BlackRole.GUEST;
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        String memberName = Helper.instance.getMember(event.getAuthor()).getEffectiveName();
        event.getChannel().sendMessage(capitalize(usedKeyword) + ", Master " + memberName + "!").queue();
    }

    //it is guaranteed that the used keyword is at least 1 letter long, therefore no checks
    private String capitalize(String usedKeyword) {
        return usedKeyword.substring(0,1).toUpperCase() + usedKeyword.substring(1).toLowerCase();
    }
}
