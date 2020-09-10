package Walter.Parsers;

import Walter.Helper;
import Walter.exceptions.ParseException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser extends Parser {
    static Pattern command;

    private List<Option> options = null;
    private List<Flag> flags = null;
    private List<String> arguments = null;

    public CommandParser() {
        command = Pattern.compile("[!?]([a-zA-Z]+)( +([^-\"][^\\s\"]*|-?\\d*[.,]?\\d+|-?[a-zA-Z]+|\"[^\"]*\"))*");
        /*                              *snorts cocaine* fuck yes
            A ! or ?, followed by the letters a-z case insensitive, followed by an arbitrary number of
            whitespace and either one of the following:
            * any word without whitespace, " signs or - signs
            * a integer or decimal number that may be negative. leading 0 can be left out for decimals
            * a flag. for example -U, with flag concatenation being allowed
            * any number of signs or linebreaks between two " signs
         */
    }

    public static boolean isCommand(String message) {
        Matcher commandChecker = command.matcher(message);
        return commandChecker.matches();
    }

    public void reset() {
        options = null;
        flags = null;
        arguments = null;
    }

    public String parseFirstArgument() throws ParseException {
        if (stringToParse == null || stringToParse.length() < 2)
            throw new ParseException("Mir wurde kein Text zu parsen gegeben.",
                "I was not given text to parse.");

        //deleting everything after first whitespace, if there is one
        String firstArgument = stringToParse.replaceFirst(" .*", "")
                                            .toLowerCase();

        //regex: ! or ?, followed by at least one letter a-z
        if (!firstArgument.matches("[!?][a-z]+"))
            throw new ParseException("Es ist kein Befehl aus \"" + firstArgument + "\" identifizierbar.",
                "There is no command identifyable in \"" + firstArgument + "\".");
        return firstArgument.substring(1);
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

        int optionsCounter = 0;
        Flag requiresParameter = null;
        for (int i = 1; i < arguments.size(); i++) {
            String argument = arguments.get(i);

            //regex: one '-' followed by at least one arbitrary char
            if (argument.matches("-[A-Za-z]+")) { //is flag
                if (requiresParameter != null)
                    throw new ParseException("Flag " + requiresParameter.getShortName() + " erwartet einen Parameter.",
                            "Flag " + requiresParameter.getShortName() + " expects a parameter");

                Flag flag = identifyFlag(argument);
                if (flag.isGiven())
                    throw new ParseException("Flag " + argument + " kann nicht mehrmals gesetzt werden.",
                            "Flag " + argument + " may not be set several times.");
                flag.given();
                if (flag.hasParameter()) requiresParameter = flag;
            } else {    //is option
                Option option;
                if (requiresParameter != null) {
                    option = requiresParameter.getParameter();
                    requiresParameter = null;
                } else {
                    if (optionsCounter >= (options == null ? 0 : options.size()))
                        throw new ParseException("Argument " + argument + " wurde nicht erwartet.",
                                "Argument " + argument + " was not expected.");
                    option = options.get(optionsCounter++);
                }
                if (option.getType() == OptionType.FLUSH)
                    option.setValue(stringToParse.substring(arguments.get(0).length() + 1));
                else
                    option.setValue(argument);
            }
        }
        checkRequiredOptionsForValues();
    }

    private List<String> splitStringToParseIntoArguments() {
        List<String> arguments = new ArrayList<>();

        String[] split = stringToParse.split("\"");  //TODO test this
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
        if (options != null) {
            for (Option option : options) {
                option.reset();
            }
        }
        if (flags != null) {
            for (Flag flag : flags) {
                flag.reset();
            }
        }
    }

    @NotNull
    private Flag identifyFlag(String argument) throws ParseException {
        if (flags == null)
            throw new ParseException("Flag " + argument + " wurde gegeben, aber keine Flags sind erwartet.",
                    "Flag " + argument + " was given but no flags are expected");
        Flag flag;
        if (argument.matches("--[A-Za-z]+")) //regex: -- followed by at least one letter a - z, both cases
            flag = searchListForFlagLongName(argument.substring(2));
        else if (argument.matches("-[A-Za-z]")) //regex: - followed by one letter a-z, both case
            flag = searchListForFlagShortname(argument.charAt(1));
        else
            throw new ParseException("Ungültiges Argument: " + argument,
                    "Invalid argument: " + argument);

        if (flag == null)
            throw new ParseException("Keine Flag zu " + argument + " gefunden",
                    "No flag found for " + argument);
        return flag;
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


    private void checkRequiredOptionsForValues() throws ParseException {
        if (options == null) return;
        for (Option option : options) {
            if (option.isRequired() && !option.hasValue())
                throw new ParseException(option.getNameGerman() + " wurde nicht gegeben, obwohl erfordert.",
                        option.getNameEnglish() + " was not given, although required.");
        }
    }
}
