package Walter;

import Walter.exceptions.ReasonedException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigIni {
    private File configFile;
    private HashMap<String, String> config;

    final String iniLineRegex;
    final Pattern iniLinePattern;

    public ConfigIni(String pathToConfigFile) throws IOException {
        iniLineRegex = "^\\s*([^=\\s]+)\\s*=\\s*(.*)";
        iniLinePattern = Pattern.compile(iniLineRegex);

        configFile = new File(pathToConfigFile);

        if (!configFile.exists()) configFile.createNewFile();
        else readFileToHashmap();

    }

    private void readFileToHashmap() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            Matcher lineMatcher;
            while ((line = reader.readLine()) != null) {
                lineMatcher = iniLinePattern.matcher(line);
                if (!lineMatcher.find()) throw new IOException("Line " + line + " did not match the required format");
                config.put(lineMatcher.group(1), lineMatcher.group(2));
            }
        }
    }

    private void writeHashmapToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
            for(Map.Entry<String, String> entry: config.entrySet()) {
                writer.write(entry.getKey() + " = " + entry.getValue());
            }
        }
    }

    public String getValue(String key) {
        return config.get(key);
    }

    public void setValue(String key, String value) {
        config.put(key, value);
    }
}
