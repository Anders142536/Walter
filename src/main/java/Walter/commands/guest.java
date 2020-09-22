package Walter.commands;

import Walter.Collection;
import Walter.entities.BlackRole;

public class guest extends Command {

    public guest() {
        keywords = new String[][]{new String[]{"guest", "gast"}};
        minimumRequiredRole = BlackRole.GUEST;
        mainKeywordGerman = 1;
    }

    @Override
    public String getDescription() {
        return "Dieser Command gibt dem User mit dem Namen **NAME** die *Guest*-Rolle." +
                        Collection.CAUTION_QUOTES_FOR_WHITESPACE;
    }

    @Override
    public String getDescriptionEnglish() {
        return "This command assigns the role *guest* to the user with the name **NAME**." +
                        Collection.CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH;
    }

//    @Override
//    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
    //TODO: add in CommandHandler.createListOfCommands()
//    }
}
