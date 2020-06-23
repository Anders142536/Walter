package Walter.commands;

import Walter.entities.BlackRole;

public class hello extends Command {

    public hello () {
        keywords = new String[]{"hallo", "hello", "hi", "hey", "hola",
                "moin", "servus", "griaßdi", "grüzi", "tag"};
        minimumRequiredRole = BlackRole.GUEST;
        mainKeywordEnglish = 1;
    }
    @Override
    public String[] getHelp() {
        return new String[]{
                " Walter",
                "Hiermit grüßt du mich höflich und ich grüße zurück."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " Walter",
                "You greet me politely, with me greeting you back."};
    }

//    @Override
//    public execute(MessageReceivedEvent event) throws CommandExecutionException {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
