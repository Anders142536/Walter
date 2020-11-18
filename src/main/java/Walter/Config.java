package Walter;

import Walter.Settings.IntegerSetting;
import Walter.entities.BlackChannel;
import Walter.entities.BlackWebhook;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.ini4j.Ini;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Config {
    private static Yaml config = new Yaml();

    //General
    private static IntegerSetting dropZoneLimit;

    //Hidden
    private static long configMessageID;
    private static boolean isLockdown;

    public static void initialize() throws IOException, ReasonedException {
        initializeSettings();
        File yamlFile = new File(Walter.location + "/config.yaml");
        if (!yamlFile.exists())
            createTemplateYaml(yamlFile);

        config = new Yaml(yamlFile);

        loadFromFile();
    }

    private static void initializeSettings() {

    }

    private static void createTemplateYaml(File iniFile) throws IOException, ReasonedException {
        System.out.println("Config file not found in " + iniFile.getPath());
        File templateFile = new File(Walter.location + "/configtemplate.yaml");
        templateFile.createNewFile();
        config = new Ini(templateFile);
        config.put("General", "dropZoneLimit", 50);
        config.put("Hidden", "servernews", "URL");
        config.put("Hidden", "patchnotes", "URL");
        config.put("Hidden", "configMessageID", "ID");
        config.put("Hidden", "isLockdown", "false");
        config.store();

        System.out.println("Config template created in " + templateFile.getPath());
        throw new ReasonedException();
    }

    public static void loadFromFile() {
        //General
        dropZoneLimit = Integer.parseInt(config.get("General", "dropZoneLimit"));

        //Hidden
        configMessageID = Long.parseLong(config.get("Hidden", "configMessageID"));
        isLockdown = Boolean.parseBoolean(config.get("Hidden", "isLockdown"));
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

    public static void setDropZoneLimit(int value) throws ReasonedException {
        try {
            if (value >= 30) {
                dropZoneLimit = value;
                config.put("General", "dropZoneLimit", value);
                config.store();
                writeToConfigChannel();
            } else throw new ReasonedException(new String[]{
                    "dropZoneLimit must not be lower than 30",
                    "dropZoneLimit darf nicht niedriger als 30 sein"
            });
        } catch (IOException e) {
            throw new ReasonedException(new String[] {
                    "Something went wrong on storing the dropZoneLimit:\n" + e.getMessage(),
                    "Etwas ist beim speichern des dropZoneLimits schief gelaufen:\n" + e.getMessage()
            });
        }
    }

    public static boolean getIsLockdown() { return isLockdown; }

    public static void setIsLockdown(boolean value) throws ReasonedException {
        isLockdown = isLockdown;
        try {
            isLockdown = value;
            config.put("General", "isLockdown", value);
            config.store();
            writeToConfigChannel();
        } catch (IOException e) {
            throw new ReasonedException(new String[] {
                    "Something went wrong on storing the dropZoneLimit:\n" + e.getMessage(),
                    "Etwas ist beim speichern des dropZoneLimits schief gelaufen:\n" + e.getMessage()
            });
        }
    }
}
