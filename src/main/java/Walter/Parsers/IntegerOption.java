package Walter.Parsers;

import Walter.exceptions.ParseException;

import javax.annotation.Nonnull;

public class IntegerOption extends Option {

    Integer value = null;

    public IntegerOption(@Nonnull String[] name, String[] description) {
        super(OptionType.INT, name, description);
    }

    public IntegerOption(@Nonnull String[] name, String[] description, boolean required) {
        super(OptionType.INT, name, description, required);
    }

    public void setValue (String argument) throws ParseException {
        if (!argument.matches("\\d*"))
            throw new ParseException(new String[] {
                    "Argument " + argument + " is not a natural number.",
                    "Argument " + argument + " ist keine natürliche Zahl."});
        this.value = Integer.parseUnsignedInt(argument);
    }

    public boolean hasValue() { return value != null; }

    public Integer getValue() { return value; }

    public void reset() { value = null; }
}
