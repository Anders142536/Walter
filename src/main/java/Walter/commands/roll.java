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

    public roll() {
        super(new String[] {
                "I give you a random number between 1 and **NUMBER**. If no **NUMBER** is given I give you a random " +
                        "number between 1 and 6.",
                "Ich gebe dir eine Zufallszahl zwischen 1 und **NUMMER**. Wenn keine **NUMMER** angegeben ist, gebe ich" +
                        "dir eine Zufallszahl zwischen 1 und 6."
        });
        keywords = new String[][]{
                {"roll"},
                {"würfel", "wuerfel"}
        };
        minimumRequiredRole = BlackRole.GUEST;
        options = new ArrayList<>();
        options.add(new IntegerOption("limit", "limit",
                "Upper limit of the roll.", "Obere Schranke für den Wurf.", false));
    }

    @Override
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        Member author = event.getMember();
        MessageChannel channel = event.getChannel();

        IntegerOption limitOption = (IntegerOption) options.get(0);

        int limit = (limitOption.hasValue() ? limitOption.getValue() : 6);
        int randomNumber = (int)(Math.random() * limit) + 1;

        Helper.instance.respond(author, channel,
                "Deine Zufallszahl lautet: " + randomNumber,
                "Your random number is: " + randomNumber);
    }
}