package Walter.commands;

import Walter.entities.BlackRole;

public class help extends Command {

    public help() {
        keywords = new String[][]{new String[]{"help", "hilfe"}};
        minimumRequiredRole = BlackRole.GUEST;
        mainKeywordGerman = 1;
    }

    @Override
    public String getDescription() {
        return "Dieser Command zeigt dir die allgemeine Hilfe-Seite an mit einer kurzen Erklärung wo du Informationen findest.";
    }

    @Override
    public String getDescriptionEnglish() {
        return "This command displays the general help page with a short explanation where you can find information.";
    }

//    @Override
//    public String[] execute(List<String> args, MessageReceivedEvent event) {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
