package Walter.exceptions;

public class CommandExecutionException extends ReasonedException {

    public CommandExecutionException(String reason) { super(reason); }

    public CommandExecutionException(String[] reason) {
        super(reason);
    }

    public CommandExecutionException(ReasonedException e) {
        super(e.getReasons());
    }
}
