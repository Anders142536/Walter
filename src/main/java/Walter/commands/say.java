package Walter.commands;

public class say extends Command {

    public say() {
        super(new String[] {
                "I post the message **TEXT** into the chat.",
                "Ich poste die Nachricht **TEXT** in den Chat."
        });
        keywords = new String[][]{
                {"say", "echo"}
        };
    }

//    @Override
//    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
