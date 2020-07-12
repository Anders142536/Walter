package Walter.Parsers;

public class Flag extends Argument {
    private final String longName;
    private final char shortName;
    private final Option parameter;
    private boolean isGiven = false;

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

    public void given() { isGiven = true; }

    public boolean isGiven() { return isGiven; }

    public void reset() {
        isGiven = false;
        if (parameter != null) parameter.reset();
    }

    public boolean hasParameter() {
        return parameter != null;
    }

    public Option getParameter() {
        return parameter;
    }

    public char getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public String getDescriptionEnglish() {
        return formatArgumentDescription("-" + shortName + ", --" + longName, descriptionEnglish);
    }

    public String getDescriptionGerman() {
        return formatArgumentDescription("-" + shortName + ", --" + longName, descriptionGerman);
    }
}
