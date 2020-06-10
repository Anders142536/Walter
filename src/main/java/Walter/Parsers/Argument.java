package Walter.Parsers;

public abstract class Argument {
    final String descriptionEnglish;
    final String descriptionGerman;
    final int argMaxLength = 15;

    public Argument(String descriptionEnglish, String descriptionGerman) {
        this.descriptionEnglish = descriptionEnglish;
        this.descriptionGerman = descriptionGerman;
    }

    public String getDescriptionEnglish;

    public String getDescriptionGerman;

    String formatArgumentDescription(String argument, String description) {
        return String.format("%-" + argMaxLength + "s%s", argument, description);
    }
}
