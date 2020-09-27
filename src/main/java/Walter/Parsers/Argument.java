package Walter.Parsers;

import Walter.Language;

public abstract class Argument {
    protected final String[] description;

    public Argument(String[] description) {
        if (description == null) description = new String[] {
                "No description",
                "Keine Beschreibung"
        };
        this.description = description;
    }

    public String getDescription(Language lang) {
        if (description.length <= lang.index) return description[0];
        return description[lang.index];
    }

    public abstract void reset();
}
