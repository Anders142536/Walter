package Walter.Parsers;

public class StringOption extends Option {

    public StringOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman) {
        super(nameEnglish, nameGerman, descriptionEnglish, descriptionGerman);
    }

    public StringOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman, boolean required) {
        super(nameEnglish, nameGerman, descriptionEnglish, descriptionGerman, required);
    }

    public boolean isCorrectType(String argument) {
        return true;
    }
}
