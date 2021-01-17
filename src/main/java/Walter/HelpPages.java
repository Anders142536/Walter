package Walter;

import Walter.entities.Language;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class HelpPages {
    public static HelpPages instance = new HelpPages();

    private final HashMap<String, HelpPage> pageMap = new HashMap<>();
    private final List<HelpPage> pageList = new ArrayList<>();
    String[] pageListHeaders = new String[]{
            "List of Help pages", "Liste der Hilfeseiten"
    };
    String[] helpQuestionmarkHint = new String[] {
            "To look at the help page of a command just replace the `!` with a `?` like `?help`\n\n",
            "Um eine Hilfeseite zu einem Befehl zu erhalten ersetze einfach das `!` mit einem `?` z.B.: `?hilfe`\n\n"
    };

    public HelpPages() {
        if (pageListHeaders.length < Language.values().length)
            Helper.logError("Walter.commands.help::help()\n" +
                    "headers list is shorter than number of languages");
    }

    public void loadPages() throws ReasonedException {
        pageMap.clear();
        pageList.clear();
        Map<String, Object> pageYamlDefinition;

        String foldername = Walter.location + "/helppages";
        File folder = new File(foldername);
        if (!folder.exists()) folder.mkdir();

        for (File f: folder.listFiles((file, filename) -> filename.endsWith(".yaml"))) {
            pageYamlDefinition = loadYamlFromFile(f);
            LinkedHashMap<String, Object> page;
            HelpPage helpPage = new HelpPage(f);

            String name;
            for (Language lang: Language.values()) {
                if (pageYamlDefinition.containsKey(lang.name())) {
                    page = (LinkedHashMap)pageYamlDefinition.get(lang.name());
                    //unchecked access, as this is supposed to always be in there
                    name = (String)page.get("name");
                } else
                    //if the language is not specified in the helppage file the name of the file is taken instead
                    name = f.getName().replace(".yaml", "");

                System.out.println("Adding page with name " + name + " and language " + lang.name());
                helpPage.names.add(name);
                pageMap.put(prepHelppageName(name), helpPage);
            }
        }

        pageMap.keySet().forEach((x) -> System.out.println("key in map: " + x));
    }

    private Map<String, Object> loadYamlFromFile(File f) throws ReasonedException {
        try {
            Yaml pageYaml = new Yaml();
            FileInputStream reader = new FileInputStream(f);
            byte[] fileData = new byte[(int) f.length()];
            reader.read(fileData);

            return pageYaml.load(new String(fileData, "UTF-8"));
        } catch (IOException e) {
            throw new ReasonedException(new String[] {
                    "Something went wrong on reading help page file " + f.getPath()
            });
        }
    }

    public MessageEmbed getPageEmbed(String name, Language lang) throws ReasonedException {
        String preparedName = prepHelppageName(name);
        if (!pageMap.containsKey(preparedName))
            throw new ReasonedException(new String[]{
                    "No helppage called " + name + " found",
                    "Keine Hilfeseite namens " + name + " gefunden"
            });
        return pageMap.get(preparedName).getPageEmbed(lang);
    }

    public MessageEmbed getPageListEmbed(Language lang) {
        StringBuilder list = new StringBuilder();
        pageMap.values()
                .stream()
                .distinct()
                .sorted((x, y) ->
                        x.names.get(lang.index).compareToIgnoreCase(y.names.get(lang.index)))
                .forEachOrdered((p) ->
            list.append(p.names.get(lang.index)).append("\n"));

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(7854123);
        builder.setTitle(getPageListHeader(lang));
        builder.setDescription(getHelpQuestionmarkHint(lang) + list.toString());
        builder.setFooter("Walter v" + Walter.VERSION);

        return builder.build();
    }

    private String getPageListHeader(Language lang) {
        if (lang.index < pageListHeaders.length) return pageListHeaders[lang.index];
        return pageListHeaders[0];
    }

    private String getHelpQuestionmarkHint(Language lang) {
        if (lang.index < helpQuestionmarkHint.length) return helpQuestionmarkHint[lang.index];
        return helpQuestionmarkHint[0];
    }

    private String prepHelppageName(String name) {
        return name.replaceAll("\\s", "").toLowerCase();
    }

    private class HelpPage {
        private File file;
        private List<String> names;

        public HelpPage(@Nonnull File file){
            this.file = file;
            names = new ArrayList<>();
        }

        MessageEmbed getPageEmbed(Language lang) throws ReasonedException {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(7854123);

            Map<String, Object> pageYaml = loadYamlFromFile(file);

            if (!pageYaml.containsKey(lang.name())) lang = Language.ENGLISH;
            LinkedHashMap<String, Object> languageEntry = (LinkedHashMap)pageYaml.get(lang.name());
            builder.setTitle((String)languageEntry.get("title"));
            builder.setDescription((String)languageEntry.get("description"));

            if (languageEntry.containsKey("fields")) {
                for (LinkedHashMap<String, String> field : (ArrayList<LinkedHashMap>) languageEntry.get("fields")) {
                    builder.addField(field.get("title"), field.get("description"), false);
                }
            }

            builder.setFooter("Walter v" + Walter.VERSION);
            return builder.build();
        }
    }
}
