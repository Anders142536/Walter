package Walter.exceptions;

public class ParseException extends Exception {
    String reason;

    public ParseException(String reason) { this.reason = reason; }

    public String toString() { return reason; }
}
