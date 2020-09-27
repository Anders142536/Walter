package Walter;

import Walter.Parsers.Flag;
import Walter.Parsers.Option;
import Walter.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class HelpEmbedFactory {
    final String[] titles;
    final String[] exampleFieldnames;
    final String[] argumentsFieldNames;
    final String[] flagFieldNames;
    final String[] requiredFieldNames;
    final String[][] requiredValues;
    final String[] descriptionFieldNames;
    final String[] synonymFieldNames;
    final String[] requiredRoleFieldNames;

    String usedArgument;
    Command comm;
    Language lang;

    public HelpEmbedFactory() {
        titles = new String[] {
                " help page",
                " Hilfeseite"
        };
        exampleFieldnames = new String[] {
                "Example",
                "Beispiel"
        };
        argumentsFieldNames = new String[] {
                "Arguments",
                "Argumente"
        };
        flagFieldNames = new String[] {
                "Flags",
                "Schalter"
        };
        requiredFieldNames = new String[] {
                "Required",
                "Benötigt"
        };
        requiredValues = new String[][]{
                {"Yes", "No"},
                {"Ja", "Nein"}
        };
        descriptionFieldNames = new String[] {
                "Descriptions",
                "Beschreibungen"
        };
        synonymFieldNames = new String[] {
                "Synonyms",
                "Synonyme"
        };
        requiredRoleFieldNames = new String[] {
                "Minimum required role",
                "Minimale benötigte Rolle"
        };
    }

    public MessageEmbed create(String usedArgument, Command comm, Language lang) {
        this.usedArgument = usedArgument;
        this.comm = comm;
        this.lang = lang;

        int i = lang.index;
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(7854123);
        builder.setTitle(usedArgument + titles[i]);
        builder.setDescription(comm.getDescription(lang));
        builder.addField(exampleFieldnames[i], getExampleString(), false);

        if (comm.hasOptions()) {
            String[] optionsStrings = getOptionsStrings();
            builder.addField(argumentsFieldNames[i], optionsStrings[0], true);
            builder.addField(requiredFieldNames[i], optionsStrings[1], true);
            builder.addField(descriptionFieldNames[i], optionsStrings[2], true);
        }

        if (comm.hasFlags()) {
            String[] flagStrings = getFlagStrings();
            builder.addField(flagFieldNames[i], flagStrings[0], true);
            builder.addField(descriptionFieldNames[i], flagStrings[1], true);
        }

        String synonymString = getSynonymsString();
        if (!synonymString.equals("")) {
            builder.addField(synonymFieldNames[i], synonymString, false);
        }

        builder.addField(requiredRoleFieldNames[i], comm.getMinimumRequiredRole().getName(), false);
        builder.setFooter("Walter v" + Walter.VERSION);
        return builder.build();
    }

    private String getExampleString() {
        StringBuilder example = new StringBuilder("```yaml\n!" + usedArgument);

        for (Option option: comm.getOptions()) {
            example.append(" " + option.getName(lang));
        }

        for (Flag flag: comm.getFlags()) {
            example.append(" --" + flag.getLongName());
        }
        return example.append("```").toString();
    }

    private String[] getOptionsStrings() {
        String[] options = new String[3];

        for (Option option: comm.getOptions()) {
            options[0] += option.getName(lang) + "\n";
            options[1] += (option.isRequired() ? requiredValues[lang.index][0] : requiredValues[lang.index][1]) + "\n";
            options[2] += option.getDescription(lang) + "\n";
        }
        return options;
    }

    private String[] getFlagStrings() {
        String[] flags = new String[2];

        for (Flag flag: comm.getFlags()) {
            flags[0] += "-" + flag.getShortName() + ", --" + flag.getLongName() + "\n";
            flags[1] += flag.getDescription(lang) + "\n";
        }
        return flags;
    }

    private String getSynonymsString() {
        StringBuilder synonyms = new StringBuilder();
        for (String[] keywords: comm.getKeywords()) {
            for (String keyword: keywords) {
                if (!usedArgument.equals(keyword)) synonyms.append("!" + keyword + " ");
            }
        }
        return synonyms.toString();
    }
}
