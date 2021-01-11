package Walter;

import Walter.Settings.EventSetting;
import Walter.Settings.SeasonSetting;
import Walter.exceptions.ReasonedException;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTests extends WalterTest{

    //@Test
    public void initialize() {
        //requires deletion of Config.yaml file if it exists
        assertThrows(ReasonedException.class, () -> Config.initialize());
    }

    //@Test
    public void writeToFileTestVerbose() {
        Map<String, Object> template = new HashMap<>();
        List<EventSetting> events = new ArrayList<>();
        EventSetting test1 = new SeasonSetting();
        events.add(test1);

        //TODO: find a way to separate hidden and non-hidden settings in file
        template.put("dropZoneLimit", 50);
        template.put("servernews", "url");
        template.put("patchnotes", "url");
        template.put("configMessageID", 4);
        template.put("isLockdown", false);
        template.put("events", events);

        File templateFile = new File(Walter.location + "/config.yaml");
        try (FileWriter writer = new FileWriter(templateFile)) {
            templateFile.createNewFile();
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml config = new Yaml(options);

            config.dump(template, writer);
        } catch (YAMLException | IOException e) {
            System.out.println("There was an issue writing the template config:\n" + e.getMessage());
        }
    }

    //@Test
    public void writeToFileTest() {
        assertDoesNotThrow(Config::initialize);

        //assertDoesNotThrow(() -> Config.writeToFile(Walter.location + "/configtemplate.yaml"));

    }

    //@Test
    public void loadFromFileVerbose() throws ReasonedException {
        Map<String, Object> map;
        try (FileInputStream reader = new FileInputStream(new File(Walter.location + "/configtemplate.yaml"))){
            Yaml yaml = new Yaml();
            map = yaml.load(reader);
        } catch (FileNotFoundException e) {
            throw new ReasonedException("The config file was not found");
        } catch (IOException e) {
            throw new ReasonedException("Something went wrong on reading the config file");
        }

        System.out.println("debug stop");
    }

    //@Test
    public void loadFromFile() {
        assertDoesNotThrow(Config::initialize);


    }

}
