package Walter.commands;

import Walter.entities.BlackRole;

public class hello extends Command {

    public hello () {
        super(new String[] {
                "You greet me politely, with me greeting you back.",
                "Hiermit grüßt du mich höflich und ich grüße zurück."
        });
        keywords = new String[][]{
                {"hi", "hallo", "hello", "hey", "hola",
                "moin", "servus", "griaßdi", "grüzi", "tag"}};
        minimumRequiredRole = BlackRole.GUEST;
    }

//    @Override
//    public execute(MessageReceivedEvent event) throws CommandExecutionException {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
