package Walter.commands;

import Walter.Parsers.StringOption;
import Walter.Walter;
import Walter.entities.Language;
import Walter.Helper;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class help extends Command {
    StringOption chosenPage;

    HashMap<String, HelpPage> pageMap;
    List<HelpPage> pageList;
    String[] pageListHeaders;

    public help() throws ReasonedException {
        super(new String[] {
                "This command displays a specified help page or a list of available pages if no page is specified.",
                "Dieser Command zeigt dir eine gegebene Hilfe-Seite an oder eine Liste an verfügbaren Seiten falls keine seite gegeben ist."
        });
        keywords = new String[][]{
                {"help"},
                {"hilfe"}};
        minimumRequiredRole = BlackRole.GUEST;

        chosenPage = new StringOption(new String[] {"topic", "thema"},
                new String[] {
                        "Which page is shown",
                        "Welche Seite gezeigt wird"
                }, false);

        options = new ArrayList<>();
        options.add(chosenPage);

        pageListHeaders = new String[] {
            "List of Help pages", "Liste der Hilfeseiten"
        };
        if (pageListHeaders.length < Language.values().length)
            Helper.instance.logError("Walter.commands.help::help()\n" +
                    "headers list is shorter than number of languages");

        loadPages();
    }

    private void loadPages() throws ReasonedException {
        String foldername = Walter.location + "/helppages";
        pageMap = new HashMap<>();
        pageList = new ArrayList<>();

        File folder = new File(foldername);
        Map<String, Object> pageYamlDefinition;

        if (!folder.exists()) folder.mkdir();

        for (File f: folder.listFiles((file, filename) -> filename.endsWith(".yaml"))) {
            pageYamlDefinition = loadYamlFromFile(f);
            LinkedHashMap<String, Object> page;
            HelpPage helpPage = new HelpPage(f);
            pageList.add(helpPage);

            for (Language lang: Language.values()) {
                if (pageYamlDefinition.containsKey(lang.name())) {
                    page = (LinkedHashMap)pageYamlDefinition.get(lang.name());
                    //unchecked access. Only done on startup, so it is allowed to fail, as I can immediatly take care of it then
                    String name = (String)page.get("name");
                    helpPage.names.add(name);
                    pageMap.put(name, helpPage);
                } else {
                    //if the language is not specified in the helppage file the name of the file is taken instead
                    helpPage.names.add(f.getName().replace(".yaml", ""));
                }
            }
        }
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

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        Language desiredLanguage = Language.getLanguage(event.getAuthor());

        try {
            if (chosenPage.hasValue()) {
                if (!pageMap.containsKey(chosenPage.getValue()))
                    throw new CommandExecutionException(new String[]{
                            "No helppage called " + chosenPage.getValue() + " found",
                            "Keine Hilfeseite namens " + chosenPage.getValue() + "gefunden"
                    });

                //TODO find out why exception above is not thrown
                HelpPage helpPage = pageMap.get(chosenPage.getValue());
                event.getChannel().sendMessage(helpPage.getPageEmbed(desiredLanguage)).queue();
            } else {
                event.getChannel().sendMessage(getPageListEmbed(desiredLanguage)).queue();
            }
        } catch (ReasonedException e) {
            throw new CommandExecutionException(e);
        }
    }


    private MessageEmbed getPageListEmbed(Language lang) {
        StringBuilder list = new StringBuilder();
        for (HelpPage p: pageList) {
            list.append(p.names.get(lang.index)).append("\n");
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(7854123);
        builder.setTitle(getPageListHeader(lang));
        builder.setDescription(list.toString());
        builder.setFooter("Walter v" + Walter.VERSION);

        return builder.build();
    }

    private String getPageListHeader(Language lang) {
        if (lang.index < pageListHeaders.length) return pageListHeaders[lang.index];
        return pageListHeaders[0];
    }

    private class HelpPage {
        File file;
        List<String> names;

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
