package Walter.commands;

public class shutdown extends Command {

    public shutdown() {
        super(new String[] {
                "I shut down. I can only be started again via an SSH connection.",
                "Ich fahre herunter. Ich kann danach nur noch über eine SSH Verbindung erneut gestartet werden."
        });
        keywords = new String[][]{
                {"shutdown"}
        };
    }

//    @Override
//    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
