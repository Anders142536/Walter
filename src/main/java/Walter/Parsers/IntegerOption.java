package Walter.Parsers;

public class IntegerOption extends Option {

    Integer value = null;

    public IntegerOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman) {
        super(OptionType.INT, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman);
    }

    public IntegerOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman, boolean required) {
        super(OptionType.INT, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman, required);
    }

    public void setValue (int value) { this.value = value; }

    public boolean hasValue() { return value != null; }

    public int getValue() { return value; }

    public void reset() { value = null; }
}
