package Walter.Parsers;

import javax.annotation.Nonnull;

public class FlushOption extends Option{
    String value = null;

    public FlushOption(@Nonnull String[] name, String[] description) {
        super(OptionType.FLUSH, name, description);
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
