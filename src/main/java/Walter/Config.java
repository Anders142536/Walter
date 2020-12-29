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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Config {
    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/y HH:mm:ss");

    private static Yaml config = new Yaml();

    //Every Setting defined here should be initialized in initializeGeneralSettings()
    private static List<Setting> general;
    public static FileSetting defaultServerLogoFile;
    public static FileSetting defaultWalterLogoFile;
    public static ColorSetting defaultMemberColor;
    public static IntegerSetting dropZoneLimit;

    public static List<EventSetting> eventSettingList;

    //Every Setting defined here should be initialized in initializeHiddenSettings()
    private static List<Setting> hidden;
    public static LongSetting configMessageID;
    public static BoolSetting isLockdown;
    private static StringSetting servernews;
    private static StringSetting patchnotes;

    public static void initialize() throws IOException, ReasonedException {
        EventScheduler.instance = new EventScheduler();
        initializeSettings();
        File yamlFile = new File(Walter.location + "/config.yaml");
        if (!yamlFile.exists())
            createTemplateSettingsYaml();
        else
            loadFromFile(yamlFile);
    }

    private static void initializeSettings() throws ReasonedException {
        initializeGeneralSettings();
        initializeHiddenSettings();
    }

    private static void initializeGeneralSettings() throws ReasonedException {
        general = new ArrayList<>();

        defaultServerLogoFile = new FileSetting("/defaults/");
        defaultServerLogoFile.setName("defaultServerLogo");
        general.add(defaultServerLogoFile);

        defaultWalterLogoFile = new FileSetting("/defaults/");
        defaultWalterLogoFile.setName("defaultWalterLogo");
        general.add(defaultWalterLogoFile);

        defaultMemberColor = new ColorSetting();
        defaultMemberColor.setName("defaultMemberColor");
        general.add(defaultMemberColor);

        dropZoneLimit = new IntegerSetting(100, 30);
        dropZoneLimit.setName("dropZoneLimit");
        dropZoneLimit.setDefault(50);
        general.add(dropZoneLimit);
    }

    private static void initializeHiddenSettings() {
        hidden = new ArrayList<>();

        configMessageID = new LongSetting();
        configMessageID.setName("configMessageID");
        hidden.add(configMessageID);

        isLockdown = new BoolSetting();
        isLockdown.setName("isLockdown");
        hidden.add(isLockdown);

        servernews = new StringSetting();
        servernews.setName("servernews");
        hidden.add(servernews);

        patchnotes = new StringSetting();
        patchnotes.setName("patchnotes");
        hidden.add(patchnotes);
    }

    private static void createTemplateSettingsYaml() throws IOException, ReasonedException {
        System.out.println("Config.yaml file not found in folder");

        //if the list of events is empty we add one that can be printed to make it easier to add one in the file
        if (eventSettingList == null) eventSettingList = new ArrayList<>();
        if (eventSettingList.isEmpty()) {
            SeasonSetting template = new SeasonSetting();
            template.setName("TemplateSetting");
            template.setStartDate(LocalDateTime.now());
            eventSettingList.add(template);
        }

        writeToFile(Walter.location + "/configtemplate.yaml");

        //this cancels the startup process
        throw new ReasonedException("Config template created in folder called configtemplate.yaml");
    }

    public static void writeToFile(String filePath) throws IOException, ReasonedException {
        Map<String, Object> template = new HashMap<>();

        //TODO: find a way to separate hidden and non-hidden settings in file
        general.forEach((x) -> template.put(x.getName(), x.getValueString()));
        hidden.forEach((x) -> template.put(x.getName(), x.getValueString()));

        template.put("lastEventExecution", EventScheduler.instance.getLastEventExecution().format(dateFormat));

        template.put("EVENTS", eventSettingList);

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
        for (Setting setting: general)
            setting.setValue(String.valueOf(yaml.get(setting.getName())));

        EventScheduler.instance.setLastEventExecution(LocalDateTime.parse(String.valueOf(yaml.get("lastEventExecution"))));

        eventSettingList = (List<EventSetting>)yaml.get("EVENTS");
        EventScheduler.instance.resetAndScheduleEvents(eventSettingList);

        //Hidden
        for (Setting setting: hidden)
            setting.setValue(String.valueOf(yaml.get(setting.getName())));

        BlackWebhook.SERVERNEWS = new BlackWebhook(servernews.getValue());
        BlackWebhook.PATCHNOTES = new BlackWebhook(patchnotes.getValue());

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
        if (!configMessageID.hasValue()) {
            Helper.logException("There is no config message ID defined!");
            return;
        }
        TextChannel configChannel = BlackChannel.CONFIG.getInstance();
        Message configMessage = configChannel.retrieveMessageById(configMessageID.getValue()).complete();
        String configText = buildConfigText();
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
        int maxSettingNameLength = getSettingNameLength();

        general.forEach((x) -> toWrite.append(String.format("%-" + maxSettingNameLength + "s = %s\n", x.getName(), x.getValueString())));
        toWrite.append("\nEvents:\n");
        for (EventSetting sett: eventSettingList) {
            toWrite.append(String.format("%s\n", sett.toString()));
        }
        toWrite.append("```");
        return toWrite.toString();
    }

    private static int getSettingNameLength() {
        int maxLength = 10;
        int tempLength;
        for (Setting x: general) {
            tempLength = x.getName().length();
            if (tempLength > maxLength) maxLength = tempLength;
        }
        return maxLength + 1;
    }
}
