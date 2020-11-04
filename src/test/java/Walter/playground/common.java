package Walter.playground;

import Walter.Language;
import Walter.commands.patch;
import Walter.exceptions.CommandExecutionException;
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

    @Test
    public void commonTest() {
        ArrayList<String> list = patch.splitMessageOnLinebreak("Lorem" + System.lineSeparator() +
                "ipsum" + System.lineSeparator() +
                "dolor\n" +
                "sit\n" +
                "amet,\n" +
                "consetetur\n" +
                "sadipscing\n" +
                "elitr,\n" +
                "sed\n" +
                "diam\n" +
                "nonumy\n" +
                "eirmod\n" +
                "tempor\n" +
                "invidunt\n" +
                "ut\n" +
                "labore\n" +
                "et\n" +
                "dolore\n" +
                "magna\n" +
                "aliquyam\n" +
                "erat,\n" +
                "sed\n" +
                "diam\n" +
                "voluptua.\n" +
                "At\n" +
                "vero\n" +
                "eos\n" +
                "et\n" +
                "accusam\n" +
                "et\n" +
                "justo\n" +
                "duo\n" +
                "dolores\n" +
                "et\n" +
                "ea\n" +
                "rebum.\n" +
                "Stet\n" +
                "clita\n" +
                "kasd\n" +
                "gubergren,\n" +
                "no\n" +
                "sea\n" +
                "takimata\n" +
                "sanctus\n" +
                "est\n" +
                "Lorem\n" +
                "ipsum\n" +
                "dolor\n" +
                "sit\n" +
                "amet.\n" +
                "Lorem\n" +
                "ipsum\n" +
                "dolor\n" +
                "sit\n" +
                "amet,\n" +
                "consetetur\n" +
                "sadipscing\n" +
                "elitr,\n" +
                "sed\n" +
                "diam\n" +
                "nonumy\n" +
                "eirmod\n" +
                "tempor\n" +
                "invidunt\n" +
                "ut\n" +
                "labore\n" +
                "et\n" +
                "dolore\n" +
                "magna\n" +
                "aliquyam\n" +
                "erat,\n" +
                "sed\n" +
                "diam\n" +
                "voluptua.\n" +
                "At\n" +
                "vero\n" +
                "eos\n" +
                "et\n" +
                "accusam\n" +
                "et\n" +
                "justo\n" +
                "duo\n" +
                "dolores\n" +
                "et\n" +
                "ea\n" +
                "rebum.\n" +
                "Stet\n" +
                "clita\n" +
                "kasd\n" +
                "gubergren,\n" +
                "no\n" +
                "sea\n" +
                "takimatasanctusest\n" +
                "Lorem\n" +
                "ipsum\n" +
                "dolor\n" +
                "sit\n" +
                "amet.", 10);


        System.out.println("stopper");
    }
}
