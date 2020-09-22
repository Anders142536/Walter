package Walter;



import Walter.Parsers.*;
import Walter.commands.*;
import Walter.exceptions.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//this class handles the recognition of commands and their execution, as well as their loading
public class CommandHandler {

    public static CommandHandler instance;
    private final HashMap<String[], Command> commands = new HashMap<String[], Command>();
    private List<Command> commandList;
    private final CommandParser parser;

    CommandHandler() {
        parser = new CommandParser();
        loadCommandsToHashMap();
    }

    private void loadCommandsToHashMap() {
        //TODO: in the long run, use reflection to find all classes instead of this list here
        for (Command command : createListOfCommands()) {
            for (String[] keyword : command.getKeywords()) {
                commands.put(keyword, command);
            }
        }
    }

    //If it is missing here it cannot be found in any way
    private List<Command> createListOfCommands() {
        commandList = new ArrayList<Command>();

//        commandList.add(new analyse());
        commandList.add(new commands());
//        commandList.add(new config());
//        commandList.add(new editmsg());
        commandList.add(new english());
//        commandList.add(new file());
        commandList.add(new german());
//        commandList.add(new getmsg());
//        commandList.add(new guest());
//        commandList.add(new hello());
//        commandList.add(new help());
        commandList.add(new listening());
        commandList.add(new member());
        commandList.add(new patch());
        commandList.add(new playing());
//        commandList.add(new reprint());
        commandList.add(new roll());
//        commandList.add(new say());
//        commandList.add(new shutdown());
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
        Member author = Helper.instance.getMember(event.getAuthor());
        MessageChannel channel = event.getChannel();
        String messageContent = event.getMessage().getContentRaw();

        Helper.instance.logCommand(author, channel, messageContent);

        parser.setStringToParse(messageContent);
        String commandName = parser.parseCommandName();
        if (!commands.containsKey(commandName))
            throw new ParseException("There is no command called " + commandName,
                    "Es gibt keinen Command namens " + commandName);

        Command toExecute = commands.get(commandName);
        if (messageContent.charAt(0) == '!') {      //command
            if (RoleHandler.instance.hasMinimumRequiredRole(author, toExecute.getMinimumRequiredRole())) {
                parser.parse(toExecute.getOptions(), toExecute.getFlags());
                toExecute.execute(event);
            } else {
                String roleName = toExecute.getMinimumRequiredRole().getName();
                throw new CommandExecutionException("You do not have the minimum required role \"" + roleName + "\" for this command.",
                        "Du hast nicht die minimale benötigte Rolle \"" + roleName + "\" für diesen Command.");
            }
        } else if (messageContent.charAt(0) == '?') //help request
            Helper.instance.respond(author, channel,
                    getGermanHelpText(commandName, toExecute),
                    getEnglishHelpText(commandName, toExecute));
    }

    private MessageEmbed getGermanHelpText(String usedArgument, Command comm) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(7854123);
        builder.setTitle(usedArgument + " Hilfeseite");
        builder.setDescription(comm.getDescription(Language.GERMAN));
        builder.addField("Syntax", getSyntaxString(comm, Language.GERMAN), false);
        String[] keywordsString = getKeywordsString(comm, Language.GERMAN);
        builder.addField("Options", keywordsString[0], true);
        builder.addField("Benötigt", keywordsString[1], true);
        builder.addField("Beschreibung", keywordsString[2], true);
        builder.addField("Synonyme", "", false);
        builder.setFooter("Walter " + Walter.VERSION);

//        String helpReturn = toExecute.getDescription();
//        return "__**Hilfeseite " + usedArgument +  "**__\n\n" +
//                "**Syntax:**" +
//                "```diff\n!" +
//                usedArgument + helpReturn[0] +
//                "```\n**Synonyme:**```" +
//                buildKeywordList(toExecute.getKeywords()) +
//                "```\n**Minimale benötigte Rolle:** " + toExecute.getMinimumRequiredRole().getName() + "\n\n" +
//                helpReturn[1];
        return builder.build();
    }

    private MessageEmbed getEnglishHelpText(String usedArgument, Command toExecute) {
//        String[] helpReturn = toExecute.getDescriptionEnglish();
//        return "__**Help page " + usedArgument +  "**__\n\n" +
//                "**Syntax:**" +
//                "```diff\n!" +
//                usedArgument + helpReturn[0] +
//                "```\n**Synonyms:**```" +
//                buildKeywordList(toExecute.getKeywords()) +
//                "```\n**Minimum required Role:** " + toExecute.getMinimumRequiredRole().getName() + "\n\n" +
//                helpReturn[1];
        return null;
    }

    private String getSyntaxString(Command comm, Language lang) {
        return "";
    }

    private String[] getKeywordsString(Command comm, Language lang) {
        return null;
    }

    private String getSynonymsString(Command comm, Language lang) {
        return "";
    }

    private String buildKeywordList(String[] keywords) {
        StringBuilder keywordList = new StringBuilder();
        for (String keyword : keywords) {
            keywordList.append(keyword).append("\n");
        }
        return keywordList.toString();
    }
}
