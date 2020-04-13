package Walter.commands;

import Walter.Helper;
import Walter.enums.BlackRole;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class roll extends Command {

    public roll() {
        keywords = new String[]{"roll", "würfel"};
        minimumRequiredBlackRole = BlackRole.GUEST;
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " NUMMER",
                "Ich gebe dir eine Zufallszahl zwischen 1 und **NUMMER**. Wenn keine **NUMMER** angegeben ist, gebe ich" +
                        "dir eine Zufallszahl zwischen 1 und 6."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " NUMBER",
                "I give you a random number between 1 and **NUMBER**. If no **NUMBER** is given I give you a random " +
                        "number between 1 and 6."};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

    @Override
    public void execute(List<String> args, MessageReceivedEvent event) {
        Member author = event.getMember();
        MessageChannel channel = event.getChannel();
        int limit;

        if (args.size() > 1) {
            limit = Integer.parseInt(args.get(1));
        } else {
            limit = 6;
        }

        int randomNumber = (int)(Math.random() * limit) + 1;

        Helper.instance.respond(author, channel,
                "Deine Zufallszahl lautet: " + randomNumber,
                "Your random number is: " + randomNumber);
    }
}