package Walter.Parsers;

public class StringOption extends Option {

    String value = null;

    public StringOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman) {
        super(OptionType.STRING, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman);
    }

    public StringOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman, boolean required) {
        super(OptionType.STRING, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman, required);
    }

    @Override
    public void setValue (String argument) {
        if (argument.matches("\"[^\"]*\"")) {
            this.value = argument.substring(1, argument.length() - 1);
        } else this.value = argument;
    }

    @Override
    public boolean hasValue(){
        return value != null;
    }

    public String getValue() { return value; }

    @Override
    public void reset() {
        value = null;
    }
}
