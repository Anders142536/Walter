package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import Walter.RoleID;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class test extends Command {

    public test () {
        keywords = new String[]{"test"};
        minimumRequiredRole = RoleID.MEMBER;

    }

//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event) {
//        System.out.println("main keyword german:" + mainKeywordGerman);
//    }
}
