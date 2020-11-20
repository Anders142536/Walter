package Walter;

import Walter.Settings.EventSetting;
import Walter.Settings.SeasonSetting;
import Walter.entities.BlackWebhook;
import Walter.exceptions.ReasonedException;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTests {

    @Test
    public void writeToFileTest() {
        Map<String, Object> template = new HashMap<>();
        List<EventSetting> events = new ArrayList<>();
        EventSetting test = new EventSetting();
        EventSetting test1 = new SeasonSetting();
        events.add(test);
        events.add(test1);

        //TODO: find a way to separate hidden and non-hidden settings in file
        template.put("dropZoneLimit", 50);
        template.put("servernews", "url");
        template.put("patchnotes", "url");
        template.put("configMessageID", 4);
        template.put("isLockdown", false);
        template.put("Events", events);

        File templateFile = new File("/home/anders/git/walter/misc/test.yaml");
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

    @Test
    public void loadFromFile() throws ReasonedException {
        Map<String, Object> map;
        try (FileInputStream reader = new FileInputStream(new File("/home/anders/git/walter/misc/test.yaml"))){
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            map = yaml.load(reader);
        } catch (FileNotFoundException e) {
            throw new ReasonedException("The config file was not found");
        } catch (IOException e) {
            throw new ReasonedException("Something went wrong on reading the config file");
        }

        System.out.println("test");
    }

}
