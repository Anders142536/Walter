package Walter.Parsers;

public class Flag extends Argument {
    private final String longName;
    private final char shortName;
    private final Option parameter;

    public Flag (char shortName, String longName, String descriptionEnglish, String descriptionGerman) {
        this(shortName, longName, descriptionEnglish, descriptionGerman, null);
    }

    public Flag (char shortName, String longName, String descriptionEnglish, String descriptionGerman, Option parameter) {
        super(descriptionEnglish, descriptionGerman);

        assert longName.length() <= argMaxLength - 6 : "flag long name " + longName + " is longer than the hardcoded limit of " + (argMaxLength - 6);

        this.shortName = shortName;
        this.longName = longName;
        this.parameter = parameter;
    }

    public boolean hasParameter() {
        return parameter != null;
    }

    public char getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public Option getParameter() {
        return parameter;
    }

    public String getDescriptionEnglish() {
        return formatArgumentDescription("-" + shortName + ", --" + longName, descriptionEnglish);
    }

    public String getDescriptionGerman() {
        return formatArgumentDescription("-" + shortName + ", --" + longName, descriptionGerman);
    }
}
