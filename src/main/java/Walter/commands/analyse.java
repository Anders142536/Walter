package Walter.commands;

public class analyse extends Command {

    public analyse() {
        super(new String[] {
            "I analyse the log files I have written and write the result both into a respective result file and the chat.",
            "Ich analysiere die von mir geschriebenen log-Datein und schreibe das Ergebnis sowohl in eine " +
                "Ergebnis-Datei als auch in den Chat."});
        keywords = new String[][]{
                {"analyse", "analyze"},
                {"analysiere"}};
    }

//    @Override
//    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
//            TODO: add in CommandHandler.createListOfCommands()
//    }
}
