package Walter.commands;

import Walter.enums.BlackRole;

public class test extends Command {

    public test () {
        keywords = new String[]{"test"};
        minimumRequiredBlackRole = BlackRole.MEMBER;

    }

//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event) {
//        System.out.println("main keyword german:" + mainKeywordGerman);
//    }
}
