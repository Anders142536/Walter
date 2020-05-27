package Walter.exceptions;


public class UserException extends Exception {
    private String reasonGerman;
    private String reasonEnglish;

    public UserException (String reasonGerman, String reasonEnglish) {
        this.reasonGerman = reasonGerman;
        this.reasonEnglish = reasonEnglish;
    }

    public String getReasonGerman() {
        return reasonGerman;
    }

    public String getReasonEnglish() {
        return reasonEnglish;
    }
}
