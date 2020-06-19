package Walter.exceptions;

public class CommandExecutionException extends UserException{

    public CommandExecutionException(String reasonGerman, String reasonEnglish) {
        super(reasonGerman, reasonEnglish);
    }
}
