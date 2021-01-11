package Walter.commands;

import Walter.Config;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class save extends Command {
    public save() {
        super(new String[] {"Saves to the config file and prints again to the config channel.",
                "Speichert in die Config-Datei und wirft die Config message im config channel erneut aus."});
        keywords = new String[][] {
                {"save"}
        };
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        try {
            Config.save();
        } catch (ReasonedException e) {
            throw new CommandExecutionException(e);
        }
    }
}
