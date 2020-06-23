package Walter.commands;

import Walter.Helper;
import Walter.Walter;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class version extends Command {

    public version () {
        keywords = new String[]{"version"};
        minimumRequiredRole = BlackRole.MEMBER;
    }

    @Override
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        MessageChannel channel = event.getChannel();
        Helper.instance.respond(channel, Walter.VERSION);
    }
}
