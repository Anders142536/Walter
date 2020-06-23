package Walter.Parsers;

import Walter.exceptions.ParseException;

public class IntegerOption extends Option {

    Integer value = null;

    public IntegerOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman) {
        super(OptionType.INT, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman);
    }

    public IntegerOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman, boolean required) {
        super(OptionType.INT, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman, required);
    }

    public void setValue (String argument) throws ParseException {
        if (!argument.matches("\\d*"))
            throw new ParseException("Argument " + argument + " ist keine natürliche Zahl.",
                    "Argument " + argument + " is not a natural number.");
        this.value = Integer.parseUnsignedInt(argument);
    }

    public boolean hasValue() { return value != null; }

    public int getValue() { return value; }

    public void reset() { value = null; }
}
