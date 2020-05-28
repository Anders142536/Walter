package Walter.Parsers;

import Walter.exceptions.ParseException;

import java.util.ArrayList;
import java.util.List;

public class CommandParser extends Parser {

    final char quote = 34; // "
    private String firstArgument;
    private final List<String> parseResult = new ArrayList<>();

    void reset() {
        firstArgument = "";
        parseResult.clear();
    }

    /** @return first argument of given stringToParse. "" if not yet parsed or parsing was not successful.
     */
    public String getFirstArgument() {
        return firstArgument;
    }

    /** Parses the first argument from the given stringToParse.
     *
     * @throws ParseException if parsing is not successful
     */
    public void parseFirstArgument() throws ParseException {
        if (stringToParse == null || stringToParse.length() < 2) throw new ParseException(
                "Mir wurde kein Text zu parsen gegeben.",
                "I was not given text to parse.");
        String[] splitString = stringToParse.substring(1).split(" ");   //substring truncates the ! or ?
        if (splitString.length == 0) throw new ParseException(
                "Es ist kein Befehl aus \"" + stringToParse + "\" identifizierbar.",
                "There is no command identifyable in \"" + stringToParse + "\".");
        firstArgument = splitString[0];
    }

    /** Parses given stringToParse against the given list of options. The first argument is ignored
     * as this is the found command.
     *
     * @param Options List of options to parse against
     * @throws ParseException if parsing is not successful or a required option is not given.
     */
    public void parse(List<Option> Options) throws ParseException {
        parseResult.clear();
        List<String> arguments = splitStringToParseIntoArguments();
        for (Option option : Options) {
            // do stuff
        }


    }

    private List<String> splitStringToParseIntoArguments() {
        List<String> arguments = new ArrayList<>();
        String[] split = stringToParse.substring(1).split("" + quote);

        for (int i = 0; i < split.length; i++) {
            //as arguments that are bracketed with " are always bracketed using exactly two "s it is certain
            //that every even index in the resulted array needs to be split at white spaces
            if (i % 2 == 0) {
                for (String temp : split[i].split(" ")) {
                    if (!temp.equals("")) arguments.add(temp);
                }
            } else arguments.add(split[i]);
        }
        return arguments;
    }

    /** @return parse result. Empty list if the parsing was not yet done.
     */
    public List<String> getParseResult() {
        return parseResult;
    }
}
