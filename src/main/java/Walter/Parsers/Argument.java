package Walter.Parsers;

public abstract class Argument {
    final String descriptionEnglish;
    final String descriptionGerman;

    public Argument(String descriptionEnglish, String descriptionGerman) {
        this.descriptionEnglish = descriptionEnglish;
        this.descriptionGerman = descriptionGerman;
    }

    public String getDescriptionEnglish;

    public String getDescriptionGerman;
}
