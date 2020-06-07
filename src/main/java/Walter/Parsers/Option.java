package Walter.Parsers;

/** Abstract class for Options. For every different Option Type there will be a derived class
 * that will define the type by implementing the method isCorrectType()
 *
 */

public abstract class Option extends Argument {

    private final boolean required;
    private final String nameEnglish;
    private final String nameGerman;

    /** When not giving a boolean wether or not this option is required it defaults to being required
     *
     * @param nameEnglish
     * @param nameGerman
     * @param descriptionEnglish
     * @param descriptionGerman
     */
    public Option(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman) {
        this(nameEnglish, nameGerman, descriptionEnglish, descriptionGerman, true);
    }

    /**
     *
     * @param nameEnglish
     * @param nameGerman
     * @param descriptionEnglish
     * @param descriptionGerman
     * @param required
     */
    public Option(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman, boolean required) {
        super(descriptionEnglish, descriptionGerman);
        this.nameEnglish = nameEnglish;
        this.nameGerman = nameGerman;
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    /**
     * @param argument Given argument that should be checked for type
     * @return wether or not the given argument is of the correct type
     */
    public abstract boolean isCorrectType(String argument);

    /**
     * @return Formatted german description of option
     */
    public String getDescriptionGerman() {
        return String.format("%-10s%s", nameGerman, descriptionGerman);
    }

    /**
     * @return Formatted english description of option
     */
    public String getDescriptionEnglish() {
        return String.format("%-10s%s", nameEnglish, descriptionEnglish);
    }

}
