package Walter.playground;

import Walter.Language;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class common {

    @Test
    public void yamlParseTests() {
        Map<String, Object> map;
        try {
            File f = new File("D:\\git\\Walter\\testhelppage.yaml");
            Yaml pageYaml = new Yaml();
            FileInputStream reader = new FileInputStream(f);
            byte[] fileData = new byte[(int) f.length()];
            reader.read(fileData);

            map = pageYaml.load(new String(fileData, "UTF-8"));

            for (Language lang: Language.values()) {
                if (!map.containsKey(lang.name())) continue;
                LinkedHashMap<String, Object> languageEntry = (LinkedHashMap)map.get(lang.name());
                String name = (String)languageEntry.get("name");
                String title = (String)languageEntry.get("title");
                String description = (String)languageEntry.get("description");

                if (!languageEntry.containsKey("fields")) continue;
                for (LinkedHashMap<String, String> field: (ArrayList<LinkedHashMap>) languageEntry.get("fields")) {
                    String fieldTitle = field.get("title");
                    String fieldDescription = field.get("description");
                    System.out.println("dbeug stop");
                }
            }
        } catch (IOException e) {
            System.out.println("fail");
        }

    }

    @Test
    public void ExceptionTests() {
        CommandExecutionException e = new CommandExecutionException(new String[] { "a", "b"});

        CommandExecutionException r = new CommandExecutionException(e);

        System.out.println("debug stopper");
    }
}
