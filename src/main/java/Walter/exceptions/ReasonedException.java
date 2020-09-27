package Walter.exceptions;


import Walter.Language;

public class ReasonedException extends Exception {
    private final String[] reason;

    public ReasonedException(String[] reason) {
        if (reason == null) reason = new String[] {
                "No reason given",
                "Kein Grund gegeben"
        };
        this.reason = reason;
    }

    public String getReason(Language lang) {
        if (reason.length >=lang.index) return reason[0];
        return reason[lang.index];
    }

}
