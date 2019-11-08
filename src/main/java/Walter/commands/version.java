package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class version extends Command {

    public version () {
        keywords = new String[]{"version"};
        minimumRequiredRole = Collection.MEMBER_ROLE_ID;

    }

    @Override
    public void execute(List<String> args, MessageReceivedEvent event, Helper helper) {
        MessageChannel channel = event.getChannel();
        helper.respond(channel, Collection.VERSION);
    }
}
