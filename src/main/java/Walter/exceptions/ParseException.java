package Walter.exceptions;

public class ParseException extends UserException {

    public ParseException(String reasonGerman, String reasonEnglish) {
        super(reasonGerman, reasonEnglish);
    }
}
