package Walter.commands;

import Walter.Collection;
import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class test extends Command {

    public test () {
        keywords = new String[]{"test"};
        minimumRequiredRole = Collection.MEMBER_ROLE_ID;

    }

//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event, Helper helper) {
//        System.out.println("main keyword german:" + mainKeywordGerman);
//    }
}
