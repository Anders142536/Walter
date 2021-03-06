package Walter;



import Walter.Parsers.*;
import Walter.commands.*;
import Walter.entities.BlackChannel;
import Walter.entities.Language;
import Walter.exceptions.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//this class handles the recognition of commands and their execution, as well as their loading
public class CommandProcessor {

    public static CommandProcessor instance;
    private final HashMap<String, Command> commands = new HashMap<>();
    private List<Command> commandList;
    private final CommandParser parser;
    private final HelpEmbedFactory helpEmbedFactory;

    CommandProcessor() throws ReasonedException {
        parser = new CommandParser();
        loadCommandsToHashMap();
        helpEmbedFactory = new HelpEmbedFactory();
    }

    private void loadCommandsToHashMap() throws ReasonedException {
        //TODO: in the long run, use reflection to find all classes instead of this list here
        for (Command command : createListOfCommands()) {
            for (String[] keywords : command.getKeywords()) {
                for (String keyword : keywords) {
                    commands.put(keyword.toLowerCase(), command);
                }
            }
        }
    }

    //If it is missing here it cannot be found in any way
    private List<Command> createListOfCommands() throws ReasonedException {
        commandList = new ArrayList<Command>();

//        commandList.add(new analyse());
        commandList.add(new commands());
        commandList.add(new config());
        commandList.add(new delEvent());
        commandList.add(new editEvent());
        commandList.add(new editmsg());
        commandList.add(new english());
        commandList.add(new events());
//        commandList.add(new file());
        commandList.add(new german());
        commandList.add(new getmsg());
        commandList.add(new guest());
        commandList.add(new hello());
        commandList.add(new help());
        commandList.add(new listening());
        commandList.add(new load());
        commandList.add(new lockdown());
        commandList.add(new maintenance());
        commandList.add(new member());
        commandList.add(new newEvent());
        commandList.add(new patch());
        commandList.add(new pin());
        commandList.add(new playing());
        commandList.add(new react());
        commandList.add(new reprint());
        commandList.add(new roll());
        commandList.add(new save());
        commandList.add(new say());
        commandList.add(new shutdown());
        commandList.add(new test());
        commandList.add(new version());
        commandList.add(new watching());

        return commandList;
    }

    public List<Command> getListOfCommands() {
        return commandList;
    }

    /**Attempts to parse the given message as command and triggers either the execution of a command 
     * or the display of a help page depending on the prefix
     */
    void process(MessageReceivedEvent event) throws ParseException, CommandExecutionException {
        Member author = Helper.getMember(event.getAuthor());
        MessageChannel channel = event.getChannel();
        String messageContent = event.getMessage().getContentRaw();

        if (channel.getIdLong() != BlackChannel.TESTING.ID)
            Helper.logCommand(author, channel, messageContent);

        parser.setStringToParse(messageContent);
        String commandName = parser.parseCommandName();
        if (!commands.containsKey(commandName.toLowerCase()))
            throw new ParseException(new String[] {
                    "There is no command called " + commandName,
                    "Es gibt keinen Command namens " + commandName});

        Command toExecute = commands.get(commandName.toLowerCase());
        if (messageContent.charAt(0) == '!') {      //command
            if (RoleHandler.hasMinimumRequiredRole(author, toExecute.getMinimumRequiredRole())) {
                parser.parse(toExecute.getOptions(), toExecute.getFlags());
                toExecute.execute(commandName, event);
            } else {
                String roleName = toExecute.getMinimumRequiredRole().getName();
                throw new CommandExecutionException(new String[] {
                        "You do not have the minimum required role \"" + roleName + "\" for this command.",
                        "Du hast nicht die minimale benötigte Rolle \"" + roleName + "\" für diesen Command."});
            }
        } else if (messageContent.charAt(0) == '?') //help request
            channel.sendMessage(helpEmbedFactory.create(commandName, toExecute, Language.getLanguage(author))).queue();
    }


}
