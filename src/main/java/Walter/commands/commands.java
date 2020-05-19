package Walter.commands;

import Walter.*;
import Walter.entities.BlackRole;
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
        keywords = new String[]{"commands", "command"};
        minimumRequiredRole = BlackRole.GUEST;
    }

    @Override
    public String[] getHelp() {
        return new String[]{
                "",
                "Dieser Command listet alle dir zur Verfügung stehenden Commands auf."};
    }

    @Override
    public String[] getHelpEnglish() {
        return new String[]{
                "",
                "This command lists all the commands available to you."};
    }

    @Override
    public String[] execute(List<String> args, MessageReceivedEvent event) {
        Member author = Helper.instance.getMember(event.getAuthor());
        MessageChannel channel = event.getChannel();

        if (adminCommands == null) {
            fillCommandStrings();
        }

        if (RoleHandler.instance.hasRole(author, BlackRole.ADMIN))
            Helper.instance.respond(author, channel, adminCommands, adminCommandsEnglish);
        else if (RoleHandler.instance.hasRole(author, BlackRole.MEMBER))
            Helper.instance.respond(author, channel, memberCommands, memberCommandsEnglish);
        else if (RoleHandler.instance.hasRole(author, BlackRole.GUEST))
            Helper.instance.respond(author, channel, guestCommands, guestCommandsEnglish);
        else
            //TODO: print error message
            Helper.instance.respond(author, channel, "", "");

        return null;
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

        String guestCommandsListString = createStringFromList("__Guest-Commands:__",guestCommandsList);
        String memberCommandsListString = createStringFromList("__Member-Commands:__", memberCommandsList);
        String adminCommandListString = createStringFromList("__Admin-Commands:__", adminCommandsList);
        String guestCommandsListStringEnglish = createStringFromListEnglish("__Guest-Commands:__", guestCommandsList);
        String memberCommandsListStringEnglish = createStringFromListEnglish("__Member-Commands:__", memberCommandsList);
        String adminCommandsListStringEnglish = createStringFromListEnglish("__Admin-Commands:__", adminCommandsList);

        guestCommands = header + guestCommandsListString + footer;
        memberCommands = header + guestCommandsListString + memberCommandsListString + footer;
        adminCommands = header + guestCommandsListString + memberCommandsListString + adminCommandListString + footer;
        guestCommandsEnglish = headerEnglish + guestCommandsListStringEnglish + footerEnglish;
        memberCommandsEnglish = headerEnglish + guestCommandsListStringEnglish + memberCommandsListStringEnglish + footerEnglish;
        adminCommandsEnglish = headerEnglish + guestCommandsListStringEnglish + memberCommandsListStringEnglish + adminCommandsListStringEnglish + footerEnglish;
    }

    private String createStringFromList(String header, List<Command> list) {
        StringBuilder builder = new StringBuilder();
        builder.append(header);
        builder.append("\n```diff");

        for (Command command :
                list) {
            if (command.keywords == null) continue;
            builder.append("\n!");
            builder.append(command.keywords[command.mainKeywordGerman]);
        }
        builder.append("```\n");
        return builder.toString();
    }

    private String createStringFromListEnglish(String header, List<Command> list) {
        StringBuilder builder = new StringBuilder();
        builder.append(header);
        builder.append("\n```diff");

        for (Command command :
                list) {
            if (command.keywords == null) continue;
            builder.append("\n!");
            builder.append(command.keywords[command.mainKeywordEnglish]);
        }
        builder.append("```\n");
        return builder.toString();
    }

}