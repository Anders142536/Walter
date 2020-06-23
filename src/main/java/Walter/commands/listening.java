package Walter.commands;

import Walter.Collection;
import Walter.Parsers.StringOption;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class listening extends Command {

    public listening() {
        keywords = new String[]{"listening"};
        minimumRequiredRole = BlackRole.GUEST;
        options = new ArrayList<>();
        options.add(new StringOption("soundsource", "tonquelle",
                "What I should listen to, given as text.",
                "Was ich hören soll, gegeben in Text-Form."));
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " TEXT",
                "Dieser Command ändert was Walter gerade hört zu **TEXT**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " TEXT",
                "This command changes what Walter is listening to to **TEXT**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH};
    }

    @Override
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        StringOption source = (StringOption)options.get(0);
        event.getJDA().getPresence().setActivity(Activity.listening(source.getValue()));

    }
}
