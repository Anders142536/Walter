package Walter.Parsers;

public class StringOption extends Option {

    String value = null;

    public StringOption(OptionType type, String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman) {
        super(type, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman);
    }

    public StringOption(OptionType type, String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman, boolean required) {
        super(type, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman, required);
    }

    public void setValue (String value) {
        this.value = value;
    }

    public boolean hasValue(){
        return value != null;
    }

    public String getValue() { return value; }

    public void reset() {
        value = null;
    }
}
