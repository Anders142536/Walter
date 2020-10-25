package Walter.commands;

import Walter.Parsers.StringOption;
import Walter.Walter;
import Walter.Language;
import Walter.Helper;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class help extends Command {
    StringOption page;

    HashMap<String, HelpPage> pages;
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
        page = new StringOption(new String[] {"topic", "thema"},
                new String[] {
                        "Which page is shown",
                        "Welche Seite gezeigt wird"
                }, false);

        options = new ArrayList<>();
        options.add(page);

        pageListHeaders = new String[] {
            "List of Help pages", "Liste der Hilfeseiten"
        };
        if (pageListHeaders.length < Language.values().length)
            Helper.instance.logError("Walter.commands.help::help\n" +
                    "headers list is shorter than number of languages");

        loadPages();
    }

    private void loadPages() throws ReasonedException {
        String foldername = Walter.location + "/helppages";
        pages = new HashMap<>();
        pageList = new ArrayList<>();

        File folder = new File(foldername);
        Map<String, Object> yaml;

        for (File f: folder.listFiles((file, filename) -> filename.endsWith(".yaml"))) {
            yaml = loadYamlFromFile(f);
            String pageName;
            HelpPage helpPage = new HelpPage(f);
            pageList.add(helpPage);

            for (Language lang: Language.values()) {
                if (yaml.containsKey("name" + lang.name())) {
                    pageName = (String)yaml.get("name" + lang.name());
                    helpPage.names.add(pageName);
                    pages.put(pageName, helpPage);
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
        //TODO: add in CommandHandler.createListOfCommands()
        Language desiredLanguage = Language.getLanguage(event.getAuthor());

        try {
            if (page.hasValue()) {
                if (!pages.containsKey(page.getValue())) throw new CommandExecutionException(new String[]{
                        "No helppage called " + page.getValue() + " found",
                        "Keine Hilfeseite namens " + page.getValue() + "gefunden"
                });
                HelpPage helpPage = pages.get(page.getValue());
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
        }

        MessageEmbed getPageEmbed(Language lang) throws ReasonedException {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(7854123);
            builder.setTitle(names.get(lang.index));

            Map<String, Object> pageYaml = loadYamlFromFile(file);

            //TODO this


            builder.setFooter("Walter v" + Walter.VERSION);

            return builder.build();
        }
    }
}
