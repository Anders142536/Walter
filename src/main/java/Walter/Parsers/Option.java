package Walter.Parsers;

import Walter.exceptions.ParseException;

/** Class representing Options.
 *
 */

public abstract class Option extends Argument {

    private final boolean required;
    private final String nameEnglish;
    private final String nameGerman;
    private final OptionType type;

    /** When not giving a boolean wether or not this option is required it defaults to being required
     *
     */
    public Option(OptionType type, String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman) {
        this(type, nameEnglish, nameGerman, descriptionEnglish, descriptionGerman, true);
    }

    public Option(OptionType type, String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman, boolean required) {
        super(descriptionEnglish, descriptionGerman);

        assert nameEnglish.length() <= argMaxLength : "argument name " + nameEnglish + " is longer than the hardcoded limit of " + argMaxLength;
        assert nameGerman.length() <= argMaxLength : "argument name " + nameGerman + "is longer than the hardcoded limit of " + argMaxLength;

        this.type = type;
        this.nameEnglish = nameEnglish;
        this.nameGerman = nameGerman;
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public OptionType getType() { return type; }

    public abstract boolean hasValue();

    public abstract void setValue(String argument) throws ParseException;

    public String getNameGerman() { return nameGerman; }

    public String getNameEnglish() { return nameEnglish; }

    /**
     * @return Formatted german description of option
     */
    public String getDescriptionGerman() {
        return formatArgumentDescription(nameGerman, descriptionGerman);

    }

    /**
     * @return Formatted english description of option
     */
    public String getDescriptionEnglish() {
        return formatArgumentDescription(nameEnglish, descriptionEnglish);
    }

}
