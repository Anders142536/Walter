package Walter.Parsers;

import Walter.Walter;
import Walter.commands.Command;
import Walter.exceptions.ParseException;

import java.util.ArrayList;
import java.util.List;

public class CommandParser extends Parser {

    final char quote = 34; // "
    String stringToParse = null;
    Command foundCommand = null;
    List<String> arguments = new ArrayList<>();

    public void setStingToParse(String stringToParse) {
        super.setStringToParse(stringToParse);
        findCommand();
    }

    private void findCommand() {
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

    public Command getFoundCommand() {
        return foundCommand;
    }
}
