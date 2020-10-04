package Walter.commands;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class patchTests {
    String path = "/home/anders/git/walter/testnotes";

    @Test
    public void yamltests() {
        Yaml notes = new Yaml();
        Map<String, Object> list = notes.load(getNotesExample());

        String name = list.get("name").toString();

        for (LinkedHashMap<String, String> entry: (ArrayList<LinkedHashMap>)list.get("New Features")) {
            System.out.println("entry found");
        }

        System.out.println("just for debugger stopping");

    }

    private String getNotesExample() {
        try {
            File file = new File(path);
            FileInputStream reader = new FileInputStream(file);
            byte[] filedata = new byte[(int) file.length()];
            reader.read(filedata);
            reader.close();

            return new String(filedata, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
