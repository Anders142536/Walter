package Walter.commands;

public class file extends Command {

    public file() {
        keywords = new String[][]{new String[]{"file", "datei"}};
        mainKeywordGerman = 1;
    }

    @Override
    public String getDescription() {
        return "Ich poste die Datei mit dem Namen **DATEINAME** in den Chat. Du musst die Dateierweiterung mit angeben. " +
                        "Weiters muss sich die Datei auf dem Raspberry, auf dem ich laufe, im Ordner walter\\files befinden.";
    }

    @Override
    public String getDescriptionEnglish() {
        return "I post the file with the name **FILENAME** into the chat. You must include file extentions. Futhermore " +
                        "the file must be present on the raspberry i run on in the folder walter\\files.";
    }

//    @Override
//    public void execute(MessageReceivedEvent event) throw CommandExecutionException {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
