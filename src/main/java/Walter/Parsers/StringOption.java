package Walter.Parsers;

public class StringOption extends Option {

    String value = null;

    public StringOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman) {
        super(OptionType.STRING, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman);
    }

    public StringOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman, boolean required) {
        super(OptionType.STRING, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman, required);
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
