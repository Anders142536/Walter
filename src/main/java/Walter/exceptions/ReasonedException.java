package Walter.exceptions;


import Walter.entities.Language;

import javax.annotation.Nonnull;

public class ReasonedException extends Exception {
    private final String[] reasons;

    public ReasonedException() {
        this(new String[] {
                "No reason given",
                "Kein Grund gegeben"
        });
    }

    public ReasonedException(@Nonnull String[] reasons) {
        this.reasons = reasons;
    }

    public ReasonedException(@Nonnull String reason) {
        this.reasons = new String[] { reason };
    }

    public String getReason(Language lang) {
        if (reasons.length <=lang.index) return reasons[0];
        return reasons[lang.index];
    }

    public String getReason() {
        return getReason(Language.ENGLISH);
    }

    public String[] getReasons() {
        return reasons;
    }

}
