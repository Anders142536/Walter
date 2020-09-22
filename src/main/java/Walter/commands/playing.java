package Walter.commands;

import Walter.Collection;
import Walter.Parsers.StringOption;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class playing extends Command {

    public playing() {
        keywords = new String[][]{new String[]{"playing"}};
        minimumRequiredRole = BlackRole.GUEST;
        options = new ArrayList<>();
        options.add(new StringOption("game", "spiel",
                "Game I should play.", "Spiel, das ich spielen soll."));
    }

    @Override
    public String getDescription() {
        return "Dieser Command ändert das Spiel, das Walter gerade spielt, zu **TEXT**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE;
    }

    @Override
    public String getDescriptionEnglish() {
        return "This command changes the game Walter is playing to **TEXT**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH;
    }

    @Override
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        StringOption game = (StringOption)options.get(0);
        event.getJDA().getPresence().setActivity(Activity.playing(game.getValue()));
    }
}
