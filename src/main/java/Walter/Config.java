package Walter;

import Walter.entities.BlackChannel;
import Walter.entities.BlackWebhook;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Config {
    private static Ini config = new Ini();

    //General
    private static int dropZoneLimit;

    //Hidden
    private static long configMessageID;

    public static void startUp() throws IOException, ReasonedException {
        File iniFile = new File(Walter.location + "/config.ini");
        if (!iniFile.exists()) {
            System.out.println("Config file not found in " + iniFile.getPath());
            File templateFile = new File(Walter.location + "/configtemplate.ini");
            templateFile.createNewFile();
            config = new Ini(templateFile);
            config.put("General", "dropZoneLimit", 50);
            config.put("Hidden", "servernews", "URL");
            config.put("Hidden", "patchnotes", "URL");
            config.put("Hidden", "configMessageID", "ID");
            config.store();

            System.out.println("Config template created in " + templateFile.getPath());
            throw new ReasonedException();
        }
        config = new Ini(iniFile);

        loadFromFile();
    }

    public static void loadFromFile() {
        //General
        dropZoneLimit = Integer.parseInt(config.get("General", "dropZoneLimit"));

        //Hidden
        configMessageID = Long.parseLong(config.get("Hidden", "configMessageID"));
        BlackWebhook.SERVERNEWS = new BlackWebhook(config.get("Hidden", "servernews"));
        BlackWebhook.PATCHNOTES = new BlackWebhook(config.get("Hidden", "patchnotes"));

        writeToConfigChannel();
    }

    private static void writeToConfigChannel() {
        StringBuilder toWrite = new StringBuilder("```ini\n");
        for (Map.Entry<String, String> entry: config.get("General").entrySet()) {
            toWrite.append(String.format("%-20s = %s\n", entry.getKey(), entry.getValue()));
        }
        toWrite.append("```");
        TextChannel configChannel = Helper.instance.getTextChannel(BlackChannel.CONFIG);
        Message configMessage = configChannel.retrieveMessageById(configMessageID).complete();
        if (configMessage != null) configMessage.editMessage(toWrite.toString()).queue();
        else configChannel.sendMessage(toWrite.toString()).queue();
    }

    public static int getDropZoneLimit() { return dropZoneLimit; }

    public static void setDropZoneLimit(int value) throws ReasonedException, IOException{
        if (value >= 30) {
            dropZoneLimit = value;
            config.store();
            writeToConfigChannel();
        }
        else throw new ReasonedException(new String[] {
                "dropZoneLimit must not be lower than 30",
                "dropZoneLimit darf nicht niedriger als 30 sein"
        });
    }

    public static Map<String, String> getWebhooks() {
        return config.get("Webhooks");
    }

}
