package Walter.Parsers;

import Walter.exceptions.ParseException;
import Walter.exceptions.ReasonedException;

import javax.annotation.Nonnull;

public class IntegerOption extends Option {

    private Integer value = null;
    private int upperLimit = Integer.MAX_VALUE;
    private int lowerLimit = Integer.MIN_VALUE;

    public IntegerOption(@Nonnull String[] name, String[] description) {
        super(OptionType.INT, name, description);
    }

    public IntegerOption(@Nonnull String[] name, String[] description, boolean required) {
        super(OptionType.INT, name, description, required);
    }

    /***
     * Sets the upper limit. Values set by setValue() can be this value or lower
     * @param upperLimit Value to set
     */
    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    /***
     * Sets the lower limit. Values set by setValue() can be this value or higher
     * @param lowerLimit Value to set
     */
    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    /***
     * Checks if the given String is actually a number and within the limits. If the given String
     * is a valid value, it is set.
     * @param argument argument to check
     * @throws ParseException Information about what was the issue.
     */
    public void setValue (String argument) throws ParseException {
        if (!argument.matches("^-?\\d*"))
            throw new ParseException(new String[] {
                    "Argument " + argument + " is not a natural number.",
                    "Argument " + argument + " ist keine natürliche Zahl."});
        try {
            int temp = Integer.parseInt(argument);
            if (temp < lowerLimit)
                throw new ParseException(new String[]{
                        "Integer " + temp + " is too small! The limit is " + lowerLimit,
                        "Natürliche Zahl " + temp + " ist zu klein! Das limit ist " + lowerLimit
                });
            if (temp > upperLimit)
                throw new ParseException(new String[]{
                        "Integer " + temp + " is too big! The limit is " + upperLimit,
                        "Natürliche Zahl " + temp + " ist zu groß! Das limit ist " + upperLimit
                });
            this.value = temp;
        } catch (NumberFormatException e) {
            throw new ParseException(new String[] {
                    "The value has to be within the limits of " + lowerLimit + " and " + upperLimit,
                    "Der Wert muss zwischen " + lowerLimit + " und " + upperLimit + " liegen"
            });
        }
    }

    public boolean hasValue() { return value != null; }

    public Integer getValue() { return value; }

    public void reset() { value = null; }
}
