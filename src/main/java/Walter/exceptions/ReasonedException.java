package Walter.exceptions;


import Walter.entities.Language;

public class ReasonedException extends Exception {
    private final String[] reasons;

    public ReasonedException() {
        this(null);
    }

    public ReasonedException(String[] reasons) {
        if (reasons == null) reasons = new String[] {
                "No reason given",
                "Kein Grund gegeben"
        };
        this.reasons = reasons;
    }

    public String getReason(Language lang) {
        if (reasons.length >=lang.index) return reasons[0];
        return reasons[lang.index];
    }

    public String[] getReasons() {
        return reasons;
    }

}
