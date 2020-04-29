package Walter.commands;

import Walter.enums.BlackRole;

public class test extends Command {

    public test () {
        keywords = new String[]{"test"};
        minimumRequiredRole = BlackRole.MEMBER;

    }

//    @Override
//    public String[] execute(List<String> args, MessageReceivedEvent event) {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
