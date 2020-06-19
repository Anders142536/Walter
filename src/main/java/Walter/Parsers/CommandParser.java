package Walter.Parsers;

import Walter.Helper;
import Walter.exceptions.ParseException;

import java.util.ArrayList;
import java.util.List;

public class CommandParser extends Parser {

    final char quote = 34; // "
    private List<Option> options = null;
    private List<Flag> flags = null;
    private List<String> arguments = null;

    public void reset() {
        options = null;
        flags = null;
        arguments = null;
    }

    public String parseFirstArgument() throws ParseException {
        if (stringToParse == null || stringToParse.length() < 2) throw new ParseException(
                "Mir wurde kein Text zu parsen gegeben.",
                "I was not given text to parse.");
        String[] splitString = stringToParse.substring(1).split(" ");   //substring truncates the ! or ?
        if (splitString.length == 0) throw new ParseException(
                "Es ist kein Befehl aus \"" + stringToParse + "\" identifizierbar.",
                "There is no command identifyable in \"" + stringToParse + "\".");
        return splitString[0];
    }

    /** Parses given stringToParse against the given list of options and flags. The first argument is ignored
     * as this is the found command.
     *
     * @throws ParseException if parsing is not successful or a required option is not given.
     */
    public void parse(List<Option> options, List<Flag> flags) throws ParseException {
        this.options = options;
        this.flags = flags;
        this.arguments = splitStringToParseIntoArguments();

        resetLists();

        checkOptions();
        checkFlags();

    }

    private List<String> splitStringToParseIntoArguments() {
        List<String> arguments = new ArrayList<>();
        String[] split = stringToParse.substring(1).split("\"");  //TODO test this

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

    private void resetLists() {
        for (Option option: options) {
            option.reset();
        }
        for (Flag flag: flags) {
            flag.reset();
        }
    }

    //side effect: found options are removed from the list
    private void checkOptions() throws ParseException {
        //checking options
        int optionsCounter = 0;
        Option tempOption;
        for (int i = 0; i < arguments.size() && optionsCounter < options.size(); i++) {
            if (argument.length() < 1) Helper.instance.logError("encountered empty option at position " + (optionsCounter + 1));
            if (argument.matches("-.?")) {

            } else {
                tempOption = options.s
            }
        }
    }

    //as everything but flags is already removed from checkOptions() we can savely assume that all entries are flags
    private void checkFlags() throws ParseException {
        Flag temp = null;
        for (String argument : arguments){
            if (argument.matches("--.?"))
                temp = searchListForFlagLongName(argument.substring(2));
            else
                temp = searchListForFlagShortname(argument.charAt(1));
            if (temp == null)
                throw new ParseException("Flag " + argument + " konnte nicht identifiziert werden.",
                        "Flag " + argument + " could not be identified");
        }
    }

    private Flag searchListForFlagLongName(String longname) {
        for (Flag flag : flags) {
            if (flag.getLongName().equals(longname)) return flag;
        }
        return null;
    }

    private Flag searchListForFlagShortname(char shortname) {
        for (Flag flag : flags) {
            if (flag.getShortName() == shortname) return flag;
        }
        return null;
    }
}
