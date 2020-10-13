package Walter.commands;

import Walter.Helper;
import Walter.Parsers.IntegerOption;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class roll extends Command {
    IntegerOption limit;

    public roll() {
        super(new String[] {
                "I give you a random number between 1 and **limit**. If no **limit** is given I give you a random " +
                        "number between 1 and 6",
                "Ich gebe dir eine Zufallszahl zwischen 1 und **Limit**. Wenn keine **Limit** angegeben ist, gebe ich" +
                        "dir eine Zufallszahl zwischen 1 und 6"
        });
        keywords = new String[][]{
                {"roll"},
                {"würfel", "wuerfel"}
        };
        minimumRequiredRole = BlackRole.GUEST;
        limit = new IntegerOption(new String[] {"limit", "Limit"},
                new String[] {"Integer upper limit of the roll", "Ganzzahlige obere Schranke für den Wurf"}, false);
        options = new ArrayList<>();
        options.add(limit);
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        Member author = event.getMember();
        MessageChannel channel = event.getChannel();

        int actualLimit = (limit.hasValue() ? limit.getValue() : 6);
        int randomNumber = (int)(Math.random() * actualLimit) + 1;

        Helper.instance.respond(author, channel,
                "Deine Zufallszahl lautet: " + randomNumber,
                "Your random number is: " + randomNumber);
    }
}