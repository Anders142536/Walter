package Walter.commands;

public class shutdown extends Command {

    public shutdown() {
        keywords = new String[][]{new String[]{"shutdown"}};
    }

    @Override
    public String getDescription() {
        return "Ich fahre herunter. Ich kann danach nur noch über eine SSH Verbindung erneut gestartet werden.";
    }

    @Override
    public String getDescriptionEnglish() {
        return "I shut down. I can only be started again via an SSH connection.";
    }

//    @Override
//    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
