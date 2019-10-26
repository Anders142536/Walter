package Walter.commands;

import Walter.Collection;
import Walter.Command;
import Walter.Helper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class commands extends Command {

    List<Command> guestCommands;
    List<Command> memberCommands;
    List<Command> adminCommands;

    public commands() {
        keywords = new String[]{"command", "commands"};
        minimumRequiredRole = Collection.GUEST_ROLE_ID;
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

     //this can probably be returned on the abstract class already, avoiding having the same code 20 times
     @Override
     public String[] getKeywords() {
         return keywords;
     }

     @Override
     public void execute(List<String> args, MessageReceivedEvent event, Helper helper) {
         Member author = event.getMember();
         MessageChannel channel = event.getChannel();

         if (adminCommands == null) {
             checkCommandLists(helper);
         }

         helper.respond(author, channel, createCommandList(author, helper), createCommandListEnglish(author, helper));

     }

     private void checkCommandLists(Helper helper) {
         List<Command> commands = helper.getCommandHandler().getListOfCommands();
         guestCommands = new ArrayList<Command>();
         memberCommands = new ArrayList<Command>();
         adminCommands = new ArrayList<Command>();
         for (Command command :
                 commands) {
             if (command.getMinimumRequiredRole() == Collection.GUEST_ROLE_ID) guestCommands.add(command);
             else if (command.getMinimumRequiredRole() == Collection.MEMBER_ROLE_ID) memberCommands.add(command);
             else adminCommands.add(command);
         }
     }

     private String createCommandList(Member author, Helper helper) {
        StringBuilder result = new StringBuilder();
        result.append("Dies sind die Commands die dir zur Verfügung stehen:\n");
        if (helper.hasMinimumRequiredRole(author, Collection.GUEST_ROLE_ID))  result.append(createStringFromList(guestCommands));
        if (helper.hasMinimumRequiredRole(author, Collection.MEMBER_ROLE_ID)) result.append(createStringFromList(memberCommands));
        if (helper.hasMinimumRequiredRole(author, Collection.ADMIN_ROLE_ID)) result.append(createStringFromList(adminCommands));c

        return "";
     }

     private String createStringFromList(List<Command> list) {
        return "";
     }

     private String createCommandListEnglish(Helper helper) {
        return "";
     }

     private String createStringFromListEnglish(List<Command> list) {
        return "";
     }

}
