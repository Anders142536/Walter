package Walter.commands;

import Walter.Config;
import Walter.HelpPages;
import Walter.Helper;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;

public class load extends Command {
    public load() {
        super(new String[] {
                "Loads the config file and help pages again. This is necessary if changes were made on the file manually.",
                "Lädt die Config-Datei und die Hilfe-Seiten erneut. Dies ist notwendig wenn die Datei manuell bearbeitet wurde."
        });

        keywords = new String[][] {
                {"load"},
                {"laden"}
        };
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        try {
            Config.load();
            HelpPages.instance.loadPages();

            Helper.respond(event.getAuthor(), event.getChannel(), new String[] {
                    "Loaded", "Geladen"
            });
            System.out.println("Loaded");
        } catch (ReasonedException e) {
            throw new CommandExecutionException(e);
        } catch (IOException e) {
            Helper.logException(e.toString());
            throw new CommandExecutionException(e.getMessage());
        }
    }
}
