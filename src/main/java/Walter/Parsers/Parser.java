package Walter.Parsers;

import org.jetbrains.annotations.NotNull;

public abstract class Parser {
    String stringToParse = "";

    /** Sets the String that shall be parsed and resets the state of the Parser.
     *
     * @param stringToParse
     */
    public void setStringToParse(@NotNull String stringToParse) {
        this.stringToParse = stringToParse;
        reset();
    }

    /** Resets the state of the Parser
     *
     * */
    abstract void reset();

//    public abstract void parse() throws ParseException;
}
