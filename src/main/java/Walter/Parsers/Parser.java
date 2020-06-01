package Walter.Parsers;

import Walter.exceptions.ParseException;

public abstract class Parser {
    String stringToParse;

    /** Sets the String that shall be parsed and resets the state of the Parser.
     *
     * @param stringToParse
     */
    public void setStringToParse(String stringToParse) {
        this.stringToParse = stringToParse;
        reset(Integer);
    }

    /** Resets the state of the Parser
     *
     * */
    abstract void reset();

//    public abstract void parse() throws ParseException;
}
