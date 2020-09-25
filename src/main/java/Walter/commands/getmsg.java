package Walter.commands;

public class getmsg extends Command {

    public getmsg() {
        super(new String[] {
                "I print the message with the ID **ID** both into the chat and the console. To access the IDs of " +
                    "messages you need to enable the developer settings under User Settings > Appearance > " +
                    "Developer Mode. Then you can right click any message to access its ID. The message to be printed " +
                    "needs not to be posted by me.",
                "Ich werfe die Nachricht mit der ID **ID** in den Chat und die Konsole aus. Um die ID von Nachrichten " +
                        "auslesen zu können musst du die Developer Settings aktivieren unter User Settings > Appearance " +
                        "> Developer Mode. Dann kannst du mit einem Rechtsklick die ID von Nachrichten auslesen. Die " +
                        "auszuwerfende Nachricht muss nicht von mir verfasst worden sein."
        });
        keywords = new String[][]{
                {"getmsg"}
        };
    }

//    @Override
//    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
