package Walter.commands;

import Walter.Walter;
import Walter.Helper;
import Walter.Parsers.StringOption;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class reprint extends Command {
    StringOption channelID;

    final String channelFileFolder;
    final String fileNameRegex;
    final Pattern fileNameParser;

    public reprint() {
        super(new String[] {
                "Clears and reprints the channel with the ID **ID** if there is a file called **ID**.channel on the " +
                        "raspberry I run on in the walter folder. To access the IDs of channels you need to enable " +
                        "the developer settings under USer Settings > Appearance > Developer Mode. Then you can " +
                        "right click any channel to access its ID. You cannot protect Messages from deletion by pinning them. " +
                        "If there is no **ID** given the channel this command was sent in is taken instead.",
                "Leert und füllt den Kanal mit der ID **ID** wenn es eine Datei namens **ID**.channel auf dem Raspberry, " +
                        "auf dem ich laufe, im Ordner walter gibt. Um die ID von Kanälen auslesen zu können musst du " +
                        "die Developer Settings aktiveren unter User Settings > Appearance > Developer Mode. Dann " +
                        "kannst du mit einem Rechtsklick die ID von Kanälen auslesen. Nachrichten können nicht vor dem " +
                        "löschen bewahrt werden indem sie gepinnt sind. Wenn keine **ID** gegeben wird, wird der Kanal " +
                        "genommen, in dem der Befehl gesendet wurde."
        });
        keywords = new String[][]{
                {"reprint"}
        };
        channelID = new StringOption(new String[] {"channelID"}, new String[] {
                "Channel to reprint", "Kanal, der neu geprintet werden soll"
        }, false);
        options = new ArrayList<>();
        options.add(channelID);

        channelFileFolder = Walter.location + "/reprintfiles/";
        fileNameRegex = "^%FILE:(.+\\..+)%$";
        fileNameParser = Pattern.compile(fileNameRegex);

    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        MessageChannel channel = (channelID.hasValue() ? checkChannelValidity() : event.getChannel());
        File file = getChannelFile(channel);

        //only delete channel if there actually is a channel file
        Helper.clearChannelOfMessages(channel);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println("line:\n>" + line + "<");
                if (line.matches(fileNameRegex)) {
                    System.out.println("matches regex");
                    sendAndClearBuilderBuffer(channel, builder);

                    //maybe just skip missing files?
                    channel.sendFile(getDefinedFile(line)).complete();
                } else
                    builder.append(line).append("\n");
            }

            sendAndClearBuilderBuffer(channel, builder);
        } catch (IOException e) {
            throw new CommandExecutionException(new String[] {
                    "Something went wrong on trying to execute the channel file: " + e.getMessage(),
                    "Etwas ist schiefgelaufen beim Versuch die channel Datei auszuführen: " + e.getMessage()
            });
        }

    }

    /**Checks the validity of the given channel ID argument
     * @return the channel represented by the ID
     * @throws CommandExecutionException if file not found
     */
    private MessageChannel checkChannelValidity() throws CommandExecutionException{
        long id;
        try {
            id = Long.parseLong(channelID.getValue());
        } catch (NumberFormatException e) {
            throw new CommandExecutionException(new String[] {
                    channelID.getValue() + " is not a valid channel ID",
                    channelID.getValue() + " ist keine valide Kanal ID"
            });
        }
        MessageChannel channel = Helper.getTextChannel(id);
        if (channel == null) throw new CommandExecutionException(new String[]{
                "No channel found with given id " + id,
                "Kein Kanal gefunden mit der gegebenen ID " + id
        });
        return channel;
    }

    @NotNull
    private File getChannelFile(MessageChannel channel) throws CommandExecutionException {
        String filePath = channelFileFolder + channel.getId() + ".channel";
        File file = new File(filePath);

        if (!file.exists())
            throw new CommandExecutionException(new String[]{
                    "Couldn't find the file: " + filePath,
                    "Konnte die Datei nicht finden: " + filePath
            });
        return file;
    }

    private void sendAndClearBuilderBuffer(MessageChannel channel, StringBuilder builder) {
        if (builder.length() == 0) return; //nothing to send

        Helper.splitMessageOnLinebreak(builder.toString(), 1900).forEach(
                (msg) -> channel.sendMessage(msg).complete()); //complete, as the order is important
        builder.setLength(0); //clearing the builder

    }

    private File getDefinedFile(String line) throws CommandExecutionException {
        Matcher fileNameMatcher = fileNameParser.matcher(line);

        if (!fileNameMatcher.find()) throw new CommandExecutionException(new String[] {
                "Couldn't parse a filename from: " + line,
                "Konnte keinen Dateinamen lesen aus: " + line
        });

        String filePath = channelFileFolder + fileNameMatcher.group(1);
        File definedFile = new File(filePath);

        if (!definedFile.exists()) throw new CommandExecutionException(new String[] {
                "Couldn't find the file: " + filePath,
                "Konnte die Datei nicht finden: " + filePath
        });

        return definedFile;
    }
}
