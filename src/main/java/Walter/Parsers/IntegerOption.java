package Walter.Parsers;

public class IntegerOption extends Option {

    public IntegerOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman) {
        super(nameEnglish, nameGerman, descriptionEnglish, descriptionGerman);
    }

    public IntegerOption(String nameEnglish, String nameGerman, String descriptionEnglish, String descriptionGerman, boolean required) {
        super(nameEnglish, nameGerman, descriptionEnglish, descriptionGerman, required);
    }

    @Override
    public boolean isCorrectType(String argument) {
        //there are smarter ways to do this, but this is simple and short ¯\_(ツ)_/¯
        try {
            Integer.parseInt(argument);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
