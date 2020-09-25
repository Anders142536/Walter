package Walter.commands;

import Walter.entities.BlackRole;

public class help extends Command {

    public help() {
        super(new String[] {
                "This command displays the general help page with a short explanation where you can find information.",
                "Dieser Command zeigt dir die allgemeine Hilfe-Seite an mit einer kurzen Erklärung wo du Informationen findest."
        });
        keywords = new String[][]{
                {"help"},
                {"hilfe"}};
        minimumRequiredRole = BlackRole.GUEST;
    }

//    @Override
//    public String[] execute(List<String> args, MessageReceivedEvent event) {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
