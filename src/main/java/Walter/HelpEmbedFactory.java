package Walter;

import Walter.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import sun.security.x509.OtherName;

public class HelpEmbedFactory {
    final String[] titles;
    final String[] syntaxFieldnames;
    final String[] argumentsFieldNames;
    final String[] flagFieldNames;
    final String[] requiredFieldNames;
    final String[] descriptionFieldNames;
    final String[] synonymFieldNames;
    final String[] requiredRoleFieldNames;

    Command comm;
    Language lang;

    public HelpEmbedFactory() {
        titles = new String[] {
                " help page",
                " Hilfeseite"
        };
        syntaxFieldnames = new String[] {
                "Syntax",
                "Syntax"
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
        this.comm = comm;
        this.lang = lang;

        int i = lang.index;
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(7854123);
        builder.setTitle(usedArgument + titles[i]);
        builder.setDescription(comm.getDescription(lang));
        builder.addField(syntaxFieldnames[i], getSyntaxString(), false);

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
        if (synonymString != null) {
            builder.addField(synonymFieldNames[i], synonymString, false);
        }

        builder.addField(requiredRoleFieldNames[i], comm.getMinimumRequiredRole().getName(), false);
        builder.setFooter("Walter " + Walter.VERSION);
        return builder.build();
    }

    private String getSyntaxString() {
        return "";
    }

    private String[] getOptionsStrings() {
        return null;
    }

    private String[] getFlagStrings() {
        return null;
    }

    private String getSynonymsString() {
        return "";
    }
}
