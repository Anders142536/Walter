package Walter.commands;

import Walter.Config;
import Walter.Helper;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;

public class load extends Command {
    public load() {
        super(new String[] {
                "Loads the config file again. This is necessary if changes were made on the file manually.",
                "Lädt die Config-Datei erneut. Dies ist notwendig wenn die Datei manuell bearbeitet wurde."
        });

        keywords = new String[][] {
                {"load"}
        };
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        try {
            Config.load();
        } catch (ReasonedException e) {
            throw new CommandExecutionException(e);
        } catch (IOException e) {
            Helper.logException(e.toString());
            throw new CommandExecutionException(e.getMessage());
        }
    }
}
