package Walter.commands;

import Walter.entities.BlackChannel;

public class config extends Command {

    public config() {
        keywords = new String[]{"config", "konfig"};
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " SCHLÜSSEL WERT",
                "Dieser Command ändert den Wert der Variable **SCHLÜSSEL** zum Wert **WERT**. Diese sind in <#" +
                        BlackChannel.CONFIG.ID + "> aufgelistet."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " KEY VALUE",
                "This command changes the variable with the name **KEY** to the value **VALUE**. Those are displayed in <#" +
                        BlackChannel.CONFIG.ID + ">."};
    }
//    @Override
//    public void execute(MessageReceivedEvent event) throws CommandExecutionException{
        //TODO: add in CommandHandler.createListOfCommands()
//    }
}
