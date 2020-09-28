package Walter.commands;

import Walter.Collection;
import Walter.Parsers.Flag;
import Walter.Parsers.StringOption;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class playing extends Command {
    Flag clear;
    StringOption game;

    public playing() {
        super(new String[] {
                "This command changes the game Walter is playing to **TEXT**"
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH,
                "Dieser Command ändert das Spiel, das Walter gerade spielt, zu **TEXT**"
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE
        });
        keywords = new String[][]{
                {"playing"},
                {"spiele"}
        };
        minimumRequiredRole = BlackRole.GUEST;
        clear = new Flag('c', "clear", new String[] {
                "Clears the current activity",
                "Löscht die aktuelle Aktivität"
        });
        game = new StringOption(new String[] {"soundsource", "tonquelle"},
                new String[] {"What I should listen to, given as text", "Was ich hören soll, gegeben als Text"}, false
        );
        options = new ArrayList<>();
        flags = new ArrayList<>();

        options.add(game);
        flags.add(clear);
    }

    @Override
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        if (clear.isGiven()) {
            event.getJDA().getPresence().setActivity(null);
        } else {
            if (!game.hasValue() || game.getValue().trim().equals(""))
                throw new CommandExecutionException(new String[] {
                        "If you don't want to clear my activity a game is required",
                        "Wenn du meine Aktivität nicht löschen willst ist ein Spiel erforderlich"
                });
            event.getJDA().getPresence().setActivity(Activity.playing(game.getValue()));

        }
    }
}
