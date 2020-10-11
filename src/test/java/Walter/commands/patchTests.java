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
//        Map<String, Object> list = notes.load(getNotesExample());
        Map<String, Object> list = notes.load(getNotesExample());

        if (!list.containsKey("name")) return; //throw exception

        StringBuilder patchmsg = new StringBuilder("__**" + list.get("name") + "**__\n");
        for (Map.Entry<String, Object> entry: list.entrySet()) {
            if (entry.getKey().equals("name")) continue;

            patchmsg.append("\n**").append(entry.getKey()).append("**\n");
            for (LinkedHashMap<String, String> item : (ArrayList<LinkedHashMap>) entry.getValue()) {
                if (!item.containsKey("title")) return; //throw exception
                patchmsg.append(":small_orange_diamond: ").append(item.get("title")).append("\n");
                if (item.containsKey("description"))
                    patchmsg.append("    *").append(item.get("description")).append("*\n");
            }
        }


        String result = patchmsg.toString();
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
