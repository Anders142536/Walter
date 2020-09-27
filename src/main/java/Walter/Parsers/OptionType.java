package Walter.Parsers;

import Walter.Language;

public enum OptionType {
    STRING  (new String[] {"Text", "Text"}),
    INT     (new String[] {"Integer", "Ganze Zahl"}),
    FLUSH   (new String[] {"Rest", "Rest"}),
    FLOAT   (new String[] {"Decimal", "Dezimalzahl"});

    String[] name;

    OptionType(String[] name) {
        this.name = name;
    }

    public String getName(Language lang) {
        return name[lang.index];
    }
}
