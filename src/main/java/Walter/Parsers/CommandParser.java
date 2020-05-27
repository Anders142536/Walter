package Walter.Parsers;

import Walter.exceptions.ParseException;

import java.util.ArrayList;
import java.util.List;

public class CommandParser extends Parser {

    final char quote = 34; // "
    private String firstArgument;
    List<String> arguments = new ArrayList<>();

    public String getFirstArgument() {
        return firstArgument;
    }

    public void parseFirstArgument() throws ParseException {
        if (stringToParse == null) throw new ParseException(
                "Mir wurde kein Text zu parsen gegeben.",
                "I was not given text to parse.");
        String[] splitString = stringToParse.split(" ");
        if (splitString.length == 0) throw new ParseException(
                "Es ist kein Befehl aus \"" + stringToParse + "\" identifizierbar.",
                "There is no command identifyable in \"" + stringToParse + "\".");
        firstArgument = splitString[0];
    }

    public void parse() throws ParseException {
        arguments.clear();
        String[] split = stringToParse.substring(1).split("" + quote);

        for (int i = 0; i < split.length; i++) {
            //as arguments that are bracketed with " are always bracketed using exactly two "s it is certain
            //that every even index in the resulted array needs to be split at white spaces
            if (i % 2 == 0) {
                for (String temp : split[i].split(" ")) {
                    //sorting out empty entries, in case someone forgot to put a whitespace between two bracketed arguments
                    if (!temp.equals("")) arguments.add(temp);
                }
            } else arguments.add(split[i]);
        }
    }

    public List<String> getArguments() {
        return arguments;
    }
}
