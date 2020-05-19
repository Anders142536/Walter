package Walter.Parsers;

import Walter.exceptions.ParseException;

public abstract class Parser {
    String stringToParse;

    public void setStringToParse(String stringToParse) {
        this.stringToParse = stringToParse;
    }

    public abstract void parse() throws ParseException;
}
