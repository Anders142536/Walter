package Walter.Parsers;

import Walter.exceptions.ParseException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser extends Parser {
    static Pattern command;
    static Matcher commandMatcher;

    private List<Option> options = null;
    private List<Flag> flags = null;
    private final List<String> arguments = new ArrayList<>();
    private String commandName = null;

    public CommandParser() {
        command = Pattern.compile("[!?]([a-zA-Z]+)( +([^-\"][^\\s\"]*|-?\\d*[.,]?\\d+|-([A-Za-z]|-[A-Za-z]+)|\"[^\"]*\"))*");
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
        commandMatcher = command.matcher(message);
        return commandMatcher.matches();
    }

    public void reset() {
        options = null;
        flags = null;
    }

    public String parseCommandName() throws ParseException {
        commandMatcher = command.matcher(stringToParse);
        if (commandMatcher.find())
            return commandMatcher.group(1);
        else
            throw new ParseException("Es ist kein Befehl aus \"" + stringToParse + "\" identifizierbar.",
                "There is no command identifyable in \"" + stringToParse + "\".");
    }

    /** Parses given stringToParse against the given list of options and flags. The first argument is ignored
     * as this is the found command.
     *
     * @throws ParseException if parsing is not successful or a required option is not given.
     */
    public void parse(List<Option> options, List<Flag> flags) throws ParseException {
        this.options = options;
        this.flags = flags;
        resetGivenLists();
        arguments.clear();
        commandName = null;

        commandMatcher = command.matcher(stringToParse);
        String foundArg;
        while(commandMatcher.find()) {
            if (commandName == null) commandName = commandMatcher.group(1);
            if ((foundArg = commandMatcher.group(3)) != null) arguments.add(foundArg);
        }
        //The Matcher finds the last regex matches first for some reason, so we have to reverse the list
        Collections.reverse(arguments);

        int optionsIndex = 0;
        Flag requiresParameter = null;
        for (String argument: arguments) {
            if (isFlag(argument)) {
                if (requiresParameter != null)
                    throw new ParseException("Flag " + requiresParameter.getShortName() + " erwartet einen Parameter.",
                            "Flag " + requiresParameter.getShortName() + " expects a parameter");
                Flag flag = identifyFlag(argument);
                flag.given();
                if (flag.hasParameter()) requiresParameter = flag;
            } else {    //is option
                Option option;
                if (requiresParameter != null) {
                    option = requiresParameter.getParameter();
                    requiresParameter = null;
                } else {
                    if (options == null || optionsIndex >= options.size())
                        throw new ParseException("Argument " + argument + " wurde nicht erwartet.",
                                "Argument " + argument + " was not expected.");
                    option = options.get(optionsIndex++);
                }
                if (option.getType() == OptionType.FLUSH)
                    option.setValue(stringToParse.substring(commandName.length() + 1));
                else
                    option.setValue(argument);
            }
        }
        checkRequiredOptionsForValues();
    }

    private boolean isFlag(String toCheck) {
        return toCheck.matches("-([A-Za-z]|-[A-Za-z]+)");
    }

    private void resetGivenLists() {
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
