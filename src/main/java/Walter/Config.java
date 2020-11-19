package Walter;

import Walter.Settings.*;
import Walter.entities.BlackChannel;
import Walter.entities.BlackWebhook;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static Yaml config = new Yaml();

    //General
    public static IntegerSetting dropZoneLimit;

    //Hidden
    public static LongSetting configMessageID;
    public static BoolSetting isLockdown;

    public static void initialize() throws IOException, ReasonedException {
        initializeSettings();
        File yamlFile = new File(Walter.location + "/config.yaml");
        if (!yamlFile.exists())
            createTemplateSettingsYaml(yamlFile);
        else
            loadFromFile();
    }

    private static void initializeSettings() throws ReasonedException {
        dropZoneLimit = new IntegerSetting(100, 0);
        dropZoneLimit.setDefault(50);
        configMessageID = new LongSetting();
        isLockdown = new BoolSetting();
        isLockdown.setDefault(false);
    }

    private static void createTemplateSettingsYaml(File iniFile) throws IOException, ReasonedException {
        System.out.println("Config file not found in " + iniFile.getPath());
        Map<String, Object> template = new HashMap<>();
        //TODO: find a way to separate hidden and non-hidden settings in file
        template.put("dropZoneLimit", 50); //TODO: give settings a getDefault and test it
        template.put("servernews", "URL");
        template.put("patchnotes", "URL");
        template.put("configMessageID", "ID");
        template.put("isLockdown", false);

        File templateFile = new File(Walter.location + "/configtemplate.yaml");
        templateFile.createNewFile();
        try (FileWriter writer = new FileWriter(templateFile)) {
            config = new Yaml();
            config.dump(template, writer);
        } catch (YAMLException e) {
            throw new ReasonedException("There was an issue writing the template config:\n" + e.getMessage());
        }

        throw new ReasonedException("Config template created in " + templateFile.getPath());
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
