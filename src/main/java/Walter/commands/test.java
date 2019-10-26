package Walter.commands;

import Walter.Collection;
import Walter.Command;

public class test extends Command {

    public test () {
        //keywords = new String[]{"test"};
        minimumRequiredRole = Collection.GUEST_ROLE_ID;
    }
}
