package Walter.commands;

import Walter.*;
import Walter.Parsers.Flag;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class commands extends Command {

    private String guestCommands;
    private String memberCommands;
    private String adminCommands;
    private String guestCommandsEnglish;
    private String memberCommandsEnglish;
    private String adminCommandsEnglish;

    public commands() {
        super(new String[] {
            "This command lists all the commands available to you.",
            "Dieser Command listet alle dir zur Verfügung stehenden Commands auf."});
        keywords = new String[][]{
                {"commands"},
                {"befehle"}};
//        Flag all = new Flag('a', "all", new String[] {"Also show non-available commands", "Zeige auch nicht verfügbare Befehle"});
        minimumRequiredRole = BlackRole.GUEST;
    }

    @Override
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        Member author = Helper.instance.getMember(event.getAuthor());
        MessageChannel channel = event.getChannel();

        if (adminCommands == null)
            fillCommandStrings();

        if (RoleHandler.instance.hasRole(author, BlackRole.ADMIN))
            Helper.instance.respond(author, channel, adminCommands, adminCommandsEnglish);
        else if (RoleHandler.instance.hasRole(author, BlackRole.MEMBER))
            Helper.instance.respond(author, channel, memberCommands, memberCommandsEnglish);
        else
            Helper.instance.respond(author, channel, guestCommands, guestCommandsEnglish);
    }

    private void fillCommandStrings() {
        List<Command> commands = CommandHandler.instance.getListOfCommands();
        List<Command> guestCommandsList = new ArrayList<Command>();
        List<Command> memberCommandsList = new ArrayList<Command>();
        List<Command> adminCommandsList = new ArrayList<Command>();

        //filling the command lists
        for (Command command :
                commands) {
            if (command.getMinimumRequiredRole() == BlackRole.GUEST) guestCommandsList.add(command);
            else if (command.getMinimumRequiredRole() == BlackRole.MEMBER) memberCommandsList.add(command);
            else adminCommandsList.add(command);
        }

        String header = "Dies sind die Commands die dir zur Verfügung stehen:\n\n";
        String headerEnglish = "These are the commands at your disposal:\n\n";
        String footer = "Bitte bedenke, dass viele der hier gelisteten Commands Synonyme haben. Für eine " +
                "genaue Erklärung eines Commands rufe ihn bitte mit einem ? anstelle eines ! auf.";
        String footerEnglish = "Please keep in mind that many of the commands listet here have synonyms. For a" +
                "detailed explanation of a command please call the command with a ? instead of a !.";

        String guestCommandsListString = createStringFromList("__Guest-Commands:__",guestCommandsList, Language.GERMAN);
        String memberCommandsListString = createStringFromList("__Member-Commands:__", memberCommandsList, Language.GERMAN);
        String adminCommandListString = createStringFromList("__Admin-Commands:__", adminCommandsList, Language.GERMAN);
        String guestCommandsListStringEnglish = createStringFromList("__Guest-Commands:__", guestCommandsList, Language.ENGLISH);
        String memberCommandsListStringEnglish = createStringFromList("__Member-Commands:__", memberCommandsList, Language.ENGLISH);
        String adminCommandsListStringEnglish = createStringFromList("__Admin-Commands:__", adminCommandsList, Language.ENGLISH);

        guestCommands = header + guestCommandsListString + footer;
        memberCommands = header + guestCommandsListString + memberCommandsListString + footer;
        adminCommands = header + guestCommandsListString + memberCommandsListString + adminCommandListString + footer;
        guestCommandsEnglish = headerEnglish + guestCommandsListStringEnglish + footerEnglish;
        memberCommandsEnglish = headerEnglish + guestCommandsListStringEnglish + memberCommandsListStringEnglish + footerEnglish;
        adminCommandsEnglish = headerEnglish + guestCommandsListStringEnglish + memberCommandsListStringEnglish + adminCommandsListStringEnglish + footerEnglish;
    }

    private String createStringFromList(String header, List<Command> list, Language lang) {
        StringBuilder builder = new StringBuilder();
        builder.append(header);
        builder.append("\n```diff");

        for (Command command :
                list) {
            if (command.keywords == null) continue;
            builder.append("\n!");
            builder.append(command.keywords[lang.index][0]);
        }
        builder.append("```\n");
        return builder.toString();
    }
}