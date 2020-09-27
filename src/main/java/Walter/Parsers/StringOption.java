package Walter.Parsers;

import javax.annotation.Nonnull;

public class StringOption extends Option {

    String value = null;

    public StringOption(@Nonnull String[] name, String[] description) {
        super(OptionType.STRING, name, description);
    }

    public StringOption(@Nonnull String[] name, String[] description, boolean required) {
        super(OptionType.STRING, name, description, required);
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
