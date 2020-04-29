package Walter.commands;

import Walter.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class editmsg extends Command {

    public editmsg() {
        keywords = new String[]{"editmsg"};
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " ID TEXT",
                "Dieser Command ändert den Inhalt der Nachricht mit der ID **ID** zu **TEXT**. Um die ID von Nachrichten " +
                        "auszulesen zu können musst du die Developer-Settings aktivieren unter User Settings > Appearance " +
                        "> Developer Mode. Dann kannst du mit einem Rechtsklick die ID von Nachrichten auslesen. Du " +
                        "kannst hiermit nur Nachrichten editieren, die von mir verfasst worden sind."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " ID TEXT",
                "This command changes the content of the messag ewith the ID **ID** to **TEXT**. To access the IDs " +
                        "of messages you need to enable the developer settings under User Settings > Appearance > " +
                        "Developer Mode. Then you can right click any message to access its ID. With this command you " +
                        "can only edit messages that were sent by me."};
    }

//    @Override
//    public String[] execute(List<String> args, MessageReceivedEvent event) {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
