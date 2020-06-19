package Walter.Parsers;

public abstract class Argument {
    protected final String descriptionEnglish;
    protected final String descriptionGerman;
    protected final int argMaxLength = 15;

    public Argument(String descriptionEnglish, String descriptionGerman) {
        this.descriptionEnglish = descriptionEnglish;
        this.descriptionGerman = descriptionGerman;
    }

    public String getDescriptionEnglish;

    public String getDescriptionGerman;

    public abstract void reset();

    String formatArgumentDescription(String argument, String description) {
        return String.format("%-" + argMaxLength + "s%s", argument, description);
    }
}
