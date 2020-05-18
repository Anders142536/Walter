package Walter.exceptions;

public class WalterFileParseException extends Exception {
    private String reason;

    public WalterFileParseException(String reason) {
        this.reason = reason;
    }

    public String toString() {
        return reason;
    }
}
