package Walter;

import Walter.Settings.*;
import Walter.entities.BlackChannel;
import Walter.entities.BlackWebhook;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
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
        File yamlFile = new File(Walter.location + "/config.yaml");
        if (!yamlFile.exists())
            createTemplateSettingsYaml();
        else
            loadFromFile(yamlFile);
    }

    private static void initializeSettings() throws ReasonedException {
        dropZoneLimit = new IntegerSetting(100, 30);
        dropZoneLimit.setName("dropZoneLimit");
        dropZoneLimit.setDefault(50);

        //events are dynamically loaded in .loadFromFile()
        eventSettingList = new ArrayList<>();

        configMessageID = new LongSetting();
        configMessageID.setName("configMessageID");

        isLockdown = new BoolSetting();
        isLockdown.setName("isLockdown");

        servernews = new StringSetting();
        servernews.setName("servernews");

        patchnotes = new StringSetting();
        patchnotes.setName("patchnotes");
    }

    private static void createTemplateSettingsYaml() throws IOException, ReasonedException {
        System.out.println("Config.yaml file not found in folder");

        //if the list of events is empty we add one that can be printed to make it easier to add one in the file
        if (eventSettingList.isEmpty()) eventSettingList.add(new SeasonSetting());

        writeToFile(Walter.location + "/configtemplate.yaml");

        //this cancels the startup process
        throw new ReasonedException("Config template created in folder called configtemplate.yaml");
    }

    public static void writeToFile(String filePath) throws IOException, ReasonedException {
        Map<String, Object> template = new HashMap<>();

        //TODO: find a way to separate hidden and non-hidden settings in file
        template.put(dropZoneLimit.getName(), dropZoneLimit.getValueString());
        template.put("EVENTS", eventSettingList);

        template.put(servernews.getName(), servernews.getValueString());
        template.put(patchnotes.getName(), patchnotes.getValueString());
        template.put(configMessageID.getName(), configMessageID.getValueString());
        template.put(isLockdown.getName(), isLockdown.getValueString());

        File templateFile = new File(filePath);
        templateFile.createNewFile();
        try (FileWriter writer = new FileWriter(templateFile)) {
            DumperOptions dumpOptions = new DumperOptions();
            dumpOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            config = new Yaml(dumpOptions);
            config.dump(template, writer);
        } catch (YAMLException e) {
            throw new ReasonedException("There was an issue writing the template config:\n" + e.getMessage());
        }
    }

    public static void loadFromFile(File yamlFile) throws ReasonedException {
        Map<String, Object> yaml = loadYamlFileToMap(yamlFile);

        //General
        dropZoneLimit.setValue(String.valueOf(yaml.getOrDefault(dropZoneLimit.getName(), 50)));
        //TODO: load events

        //TODO: expand tests to also test for "Undefined" tags
        //Hidden
        configMessageID.setValue(String.valueOf(yaml.getOrDefault(configMessageID.getName(), "Undefined")));
        isLockdown.setValue(String.valueOf(yaml.getOrDefault(isLockdown.getName(), false)));

        BlackWebhook.SERVERNEWS = new BlackWebhook(String.valueOf(yaml.getOrDefault(servernews.getName(), "Undefined")));
        BlackWebhook.PATCHNOTES = new BlackWebhook(String.valueOf(yaml.getOrDefault(patchnotes.getName(), "Undefined")));

        writeToConfigChannel();
    }

    private static Map<String, Object> loadYamlFileToMap(File yamlFile) throws ReasonedException {
        try (FileInputStream reader = new FileInputStream(yamlFile)){
            return config.load(reader);
        } catch (FileNotFoundException e) {
            throw new ReasonedException("The config file was not found");
        } catch (IOException e) {
            throw new ReasonedException("Something went wrong on reading the config file");
        }
    }

    private static void writeToConfigChannel() {
        String configText = buildConfigText();
        TextChannel configChannel = BlackChannel.CONFIG.getInstance();
        Message configMessage = configChannel.retrieveMessageById(configMessageID.getValue()).complete();
        if (configMessage != null) configMessage.editMessage(configText).queue();
        else configChannel.sendMessage(configText).queue(
                //if the config message was not found we send a new one, saving the newly sent config message in the setting
                newMsg -> {
                    try {
                        configMessageID.setValue(newMsg.getId());
                        writeToFile(Walter.location + "/config.yaml");
                    } catch (ReasonedException e) {
                        Helper.logException("Something went wrong when trying to refresh the configMessageID\n" + e.getReason());
                    } catch (IOException e) {
                        Helper.logException("Something went wrong when trying to write config to file");
                    }
                }
        );
    }

    @NotNull
    private static String buildConfigText() {
        StringBuilder toWrite = new StringBuilder("```ini\n");

        //list all non-hidden settings like this
        toWrite.append(String.format("%-20s = %s\n", dropZoneLimit.getName(), dropZoneLimit.getValueString()));
        toWrite.append("\nEvents:\n");
        for (EventSetting sett: eventSettingList) {
            toWrite.append(String.format("%s\n", sett.toString()));
        }
        toWrite.append("```");
        return toWrite.toString();
    }
}
