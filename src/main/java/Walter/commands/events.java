package Walter.commands;

import Walter.Parsers.Flag;
import Walter.Parsers.StringOption;

public class events extends Command {

    StringOption eventname;
    Flag verbose;

    public events() {
        super(new String[] {
                ""
        });
    }
}
