package Walter.commands;

import Walter.Collection;
import Walter.enums.BlackRole;

public class listening extends Command {

    public listening() {
        keywords = new String[]{"listening"};
        minimumRequiredRole = BlackRole.GUEST;
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                " TEXT",
                "Dieser Command ändert was Walter gerade hört zu **TEXT**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                " TEXT",
                "This command changes what Walter is listening to to **TEXT**."
                        + Collection.CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH};
    }

    @Override
    public String[] getKeywords() {
        return keywords;
    }

//    @Override
//    public void execute(List<String> args, MessageReceivedEvent event) {
//    }
}
