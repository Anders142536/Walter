package Walter.Parsers;

import Walter.Language;

public abstract class Argument {
    protected final String[] description;
    protected final int argMaxLength = 15;

    public Argument(String[] description) {
        this.description = description;
    }

    public String getDescription(Language lang) {
        if (description == null) return "No description";
        if (description.length >= lang.index) return description[0];
        return description[lang.index];
    }

    public abstract void reset();

    String formatArgumentDescription(String argument, String description) {
        return String.format("%-" + argMaxLength + "s%s", argument, description);
    }
}
