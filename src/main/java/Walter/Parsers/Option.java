package Walter.Parsers;

public abstract class Option {

    private final boolean required;
    private final Option parameter;
    private final String nameEnglish;
    private final String nameGerman;
    private final String descriptionEnglish;
    private final String descriptionGerman;


    public Option(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman) {
        this(nameEnglish, nameGerman, descriptionEnglish, descriptionGerman, false, null);
    }

    public Option(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman, boolean required) {
        this(nameEnglish, nameGerman, descriptionEnglish, descriptionGerman, required, null);

    }

    public Option(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman, boolean required, Option parameter) {
        this.nameEnglish = nameEnglish;
        this.nameGerman = nameGerman;
        this.descriptionEnglish = descriptionEnglish;
        this.descriptionGerman = descriptionGerman;
        this.required = required;
        this.parameter = parameter;
    }

    public boolean isRequired() {
        return required;
    }
    public boolean hasParameter() {
        return parameter != null;
    }

    public Option getParameter() {
        return parameter;
    }
    

    public String getDescriptionGerman() {
        return String.format("%-10s%s", nameGerman, descriptionGerman);
    }

    public String getDescriptionEnglish() {
        return String.format("%-10s%s", nameEnglish, descriptionEnglish);
    }

}
