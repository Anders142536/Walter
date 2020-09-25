package Walter.commands;

public class file extends Command {

    public file() {
        super(new String[]{
                "I post the file with the name **FILENAME** into the chat. You must include file extentions. Futhermore " +
                        "the file must be present on the raspberry i run on in the folder walter\\files.",
                "Ich poste die Datei mit dem Namen **DATEINAME** in den Chat. Du musst die Dateierweiterung mit angeben. " +
                        "Weiters muss sich die Datei auf dem Raspberry, auf dem ich laufe, im Ordner walter\\files befinden."});
        keywords = new String[][]{
                {"file"},
                {"datei"}};
    }

//    @Override
//    public void execute(MessageReceivedEvent event) throw CommandExecutionException {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
