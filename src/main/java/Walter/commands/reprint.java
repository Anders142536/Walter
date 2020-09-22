package Walter.commands;

public class reprint extends Command {

    public reprint() {
        keywords = new String[][]{new String[]{"reprint"}};
    }

    @Override
    public String getDescription() {
        return "Leert und füllt den Kanal mit der ID **ID** wenn es eine Datei namens **ID**.channel auf dem Raspberry, " +
                        "auf dem ich laufe, im Ordner walter gibt. Um die ID von Kanälen auslesen zu können musst du " +
                        "die Developer Settings aktiveren unter User Settings > Appearance > Developer Mode. Dann " +
                        "kannst du mit einem Rechtsklick die ID von Kanälen auslesen.";
    }

    @Override
    public String getDescriptionEnglish() {
        return "Clears and reprints the channel with the ID **ID** if there is a file called **ID**.channel on the " +
                        "raspberry I run on in the walter folder. To access the IDs of channels you need to enable " +
                        "the developer settings under USer Settings > Appearance > Developer Mode. Then you can " +
                        "right click any channel to access its ID.";
    }

//    @Override
//    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
