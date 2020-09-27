package Walter.Parsers;

import Walter.exceptions.ParseException;

public class Flag extends Argument {
    private final String longName;
    private final char shortName;
    private final Option parameter;
    private boolean isGiven = false;

    public Flag (char shortName, String longName, String[] description) {
        this(shortName, longName, description, null);
    }

    public Flag (char shortName, String longName, String[] description, Option parameter) {
        super(description);

        this.shortName = shortName;
        this.longName = longName;
        this.parameter = parameter;
    }

    public void given() throws ParseException {
        if (isGiven) throw new ParseException(new String[] {
                "Flag " + longName + " may not be set several times.",
                "Flag " + longName + " kann nicht mehrmals gesetzt werden."});
        isGiven = true;
    }

    public boolean isGiven() { return isGiven; }

    public void reset() {
        isGiven = false;
        if (parameter != null) parameter.reset();
    }

    public boolean hasParameter() {
        return parameter != null;
    }

    public Option getParameter() {
        return parameter;
    }

    public char getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }
}
