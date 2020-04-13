package Walter.commands;

import Walter.enums.BlackChannel;

public class config extends Command {

    public config() {
        keywords = new String[]{"config", "konfig"};
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " SCHLÜSSEL WERT",
                "Dieser Command ändert den Wert der Variable **SCHLÜSSEL** zum Wert **WERT**. Diese sind in <#" +
                        BlackChannel.CONFIG+ "> aufgelistet."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " KEY VALUE",
                "This command changes the variable with the name **KEY** to the value **VALUE**. Those are displayed in <#" +
                        BlackChannel.CONFIG + ">."};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event) {
//    }
}
