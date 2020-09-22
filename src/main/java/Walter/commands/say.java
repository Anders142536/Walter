package Walter.commands;

public class say extends Command {

    public say() {
        keywords = new String[][]{new String[]{"say", "echo"}};
    }

    @Override
    public String getDescription() {
        return "Ich poste die Nachricht **TEXT** in den Chat.";
    }

    @Override
    public String getDescriptionEnglish() {
        return "I post the message **TEXT** into the chat.";
    }

//    @Override
//    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
