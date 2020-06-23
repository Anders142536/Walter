package Walter.Parsers;

public class FlushOption extends Option{
    String value = null;

    public FlushOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman) {
        super(OptionType.FLUSH, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman);
    }

    @Override
    public void setValue (String argument) {
        this.value = argument;
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
