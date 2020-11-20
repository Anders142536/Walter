package Walter;

import Walter.Settings.*;
import Walter.entities.BlackChannel;
import Walter.entities.BlackWebhook;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    private static File yamlFile;
    private static Yaml config = new Yaml();

    //General
    public static IntegerSetting dropZoneLimit;
    public static List<EventSetting> eventSettingList;

    //Hidden
    public static LongSetting configMessageID;
    public static BoolSetting isLockdown;
    private static StringSetting servernews;
    private static StringSetting patchnotes;

    public static void initialize() throws IOException, ReasonedException {
        initializeSettings();
        yamlFile = new File(Walter.location + "/config.yaml");
        if (!yamlFile.exists())
            createTemplateSettingsYaml();
        else
            loadFromFile();
    }

    private static void initializeSettings() throws ReasonedException {
        dropZoneLimit = new IntegerSetting(100, 30);
        dropZoneLimit.setDefault(50);
        eventSettingList = new ArrayList<>();

        configMessageID = new LongSetting();
        isLockdown = new BoolSetting(); //Default is false
        servernews = new StringSetting();
        patchnotes = new StringSetting();
    }

    private static void createTemplateSettingsYaml() throws IOException, ReasonedException {
        System.out.println("Config.yaml file not found in folder");
        writeToFile(Walter.location + "/configtemplate.yaml");
        throw new ReasonedException("Config template created in folder called configtemplate.yaml");
    }

    public static void writeToFile(String filePath) throws IOException, ReasonedException {
        Map<String, Object> template = new HashMap<>();

        //TODO: find a way to separate hidden and non-hidden settings in file
        template.put("dropZoneLimit", dropZoneLimit.getValue());
        template.put("events", eventSettingList);

        template.put("servernews", servernews.getValue());
        template.put("patchnotes", patchnotes.getValue());
        template.put("configMessageID", configMessageID.getValue());
        template.put("isLockdown", isLockdown.getValue());

        File templateFile = new File(filePath);
        templateFile.createNewFile();
        try (FileWriter writer = new FileWriter(templateFile)) {
            config = new Yaml();
            config.dump(template, writer);
        } catch (YAMLException e) {
            throw new ReasonedException("There was an issue writing the template config:\n" + e.getMessage());
        }
    }

    public static void loadFromFile() throws ReasonedException {
        Map<String, Object> yaml = loadYamlFileToMap();

        //General
        dropZoneLimit.setValue(String.valueOf(yaml.get("dropZoneLimit")));
        //TODO: load events

        //Hidden
        configMessageID.setValue(String.valueOf(yaml.get("configMessageID")));
        isLockdown.setValue(String.valueOf(yaml.get("isLockdown")));


        BlackWebhook.SERVERNEWS = new BlackWebhook(String.valueOf(yaml.get("servernotes")));
        BlackWebhook.PATCHNOTES = new BlackWebhook(String.valueOf(yaml.get("patchnotes")));

        writeToConfigChannel();
    }

    private static Map<String, Object> loadYamlFileToMap() throws ReasonedException {
        try (FileInputStream reader = new FileInputStream(yamlFile)){
            return config.load(reader);
        } catch (FileNotFoundException e) {
            throw new ReasonedException("The config file was not found");
        } catch (IOException e) {
            throw new ReasonedException("Something went wrong on reading the config file");
        }
    }

    private static void writeToConfigChannel() {
        StringBuilder toWrite = new StringBuilder("```ini\n");

        //list all non-hidden settings like this
        toWrite.append(String.format("%-20s = %s\n", "dropzoneLimit", dropZoneLimit.getValue()));

        toWrite.append("```");
        TextChannel configChannel = BlackChannel.CONFIG.getInstance();
        Message configMessage = configChannel.retrieveMessageById(configMessageID.getValue()).complete();
        if (configMessage != null) configMessage.editMessage(toWrite.toString()).queue();
        else configChannel.sendMessage(toWrite.toString()).queue(
                newMsg -> {
                    try {
                        configMessageID.setValue(newMsg.getId());
                        writeToFile(Walter.location + "/config.yaml");
                    } catch (ReasonedException e) {
                        Helper.instance.logException("Something went wrong when trying to refresh the configMessageID\n" + e.getReason());
                    } catch (IOException e) {
                        Helper.instance.logException("Something went wrong when trying to write config to file");
                    }
                }
        );
    }
}
