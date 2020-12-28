package Walter.commands;

import Walter.Helper;
import Walter.Walter;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class version extends Command {

    public version () {
        super(new String[] {
                "Tells you the current version of me.",
                "Sagt dir die aktuelle Version von mir."
        });
        keywords = new String[][]{
                {"version", "v"}
        };
        minimumRequiredRole = BlackRole.GUEST;
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        MessageChannel channel = event.getChannel();
        Helper.respond(channel, Walter.VERSION);
    }
}
