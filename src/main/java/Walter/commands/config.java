package Walter.commands;

import Walter.Config;
import Walter.Parsers.StringOption;
import Walter.entities.BlackChannel;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class config extends Command {
    StringOption key;
    StringOption value;

    public config() {
        super(new String[] {
            "This command changes the setting with the name **KEY** to the value **VALUE**. Those are displayed in <#" +
                BlackChannel.CONFIG.ID + ">.",
            "Dieser Command ändert den Wert des Settings **SCHLÜSSEL** zum Wert **WERT**. Diese sind in <#" +
                BlackChannel.CONFIG.ID + "> aufgelistet."});
                keywords = new String[][]{
                {"config"},
                {"konfig"}};

        key = new StringOption(new String[] {"key", "Schlüssel"},
                new String[] {
                        "Name of setting to be changed",
                        "Name des zu verändernden Settings"
                });
        value = new StringOption(new String[] {"value", "Wert"},
                new String[] {
                        "New value of the setting",
                        "Neuer Wert des Settings"
                });

        options = new ArrayList<>();
        options.add(key);
        options.add(value);
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        try {
            switch (key.getValue().toLowerCase().trim()) {
                case "dropzonelimit":
                        Config.setDropZoneLimit(Integer.parseInt(value.getValue()));
                default:
                    throw new CommandExecutionException(new String[] {
                            "There is no setting called " + key.getValue(),
                            "Es gibt kein Setting namens " + key.getValue()
                    });
            }
        } catch (NumberFormatException e) {
            throw new CommandExecutionException(new String[] {
                    value.getValue() + " is no number", value.getValue() + " ist keine Zahl"
            });
        } catch (ReasonedException e) {
            throw new CommandExecutionException(e);
        }
    }
}
