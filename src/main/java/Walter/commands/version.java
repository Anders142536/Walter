package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import Walter.Walter;
import Walter.RoleID;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class version extends Command {

    public version () {
        keywords = new String[]{"version"};
        minimumRequiredRole = RoleID.MEMBER;

    }

    @Override
    public void execute(List<String> args, MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        Helper.instance.respond(channel, Walter.VERSION);
    }
}