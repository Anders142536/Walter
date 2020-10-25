package Walter.commands;

import Walter.Helper;
import Walter.Language;
import Walter.Parsers.IntegerOption;
import Walter.Walter;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class roll extends Command {
    IntegerOption limit;
    IntegerOption times;

    String[] headers;

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
        times = new IntegerOption(new String[] {"throws", "würfe"},
                new String[] {"Amount of throws", "Anzahl der Würfe"}, false);

        options = new ArrayList<>();
        options.add(limit);
        options.add(times);

        headers = new String[]{
                " random number(s) between 1 and ",
                " Zufallszahl(en) zwischen 1 und "
        };
        if (headers.length < Language.values().length)
            Helper.instance.logError("Walter.commands.roll::roll\n" +
                    "headers list is shorter than number of languages");
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        Member author = event.getMember();
        MessageChannel channel = event.getChannel();

        int throwLimit = (limit.hasValue() ? limit.getValue() : 6);
        int throwTimes = (times.hasValue() ? times.getValue() : 1);

        StringBuilder results = new StringBuilder("" + getRandomNumber(throwLimit));
        for (int i = 1; i < throwTimes; i++) //only start doing commas after the first number
            results.append(", ").append(getRandomNumber(throwLimit));

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(7854123);
        embed.setTitle(throwTimes + getHeader(Language.getLanguage(author)) + throwLimit);
        embed.setDescription(results.toString());
        embed.setFooter("Walter v" + Walter.VERSION);
        channel.sendMessage(embed.build()).queue();
    }

    private int getRandomNumber(int actualLimit) {
        return (int)(Math.random() * actualLimit) + 1;
    }

    private String getHeader(Language lang) {
        if (lang.index < headers.length) return headers[lang.index];
        return headers[0];
    }
}