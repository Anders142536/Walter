package Walter.Parsers;

public class FlushOption extends Option{
    String value = null;

    public FlushOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman) {
        super(OptionType.FLUSH, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman);
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
