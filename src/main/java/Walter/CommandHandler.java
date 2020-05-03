package Walter;



import Walter.commands.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//this class handles the recognition of commands and their execution, as well as their loading
public class CommandHandler {

    public static CommandHandler instance;
    private HashMap<String, Command>  commands = new HashMap<>();
    private List<Command> commandList;

    CommandHandler() {
        loadCommandsToHashMap();
    }

    private void loadCommandsToHashMap() {
        //TODO: in the long run, use reflection to find all classes instead of this list here
        for (Command command :
                createListOfCommands()) {
            String[] keywords = command.getKeywords();
            if (keywords != null) {
                for (String keyword :
                        keywords) {
                    commands.put(keyword, command);
                }
            }
        }
    }

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
//        commandList.add(new pimmel());
        commandList.add(new playing());
//        commandList.add(new reprint());
        commandList.add(new roll());
//        commandList.add(new say());
//        commandList.add(new shutdown());
        commandList.add(new test());
        commandList.add(new version());
        commandList.add(new watching());

        System.out.println("length of commandlist: " + commandList.size());
        return commandList;
    }

    public List<Command> getListOfCommands() {
        return commandList;
    }

    void process(MessageReceivedEvent event) {
        String messageContent = event.getMessage().getContentRaw();
        List<String> arguments = parseCommand(messageContent);
        Command toExecute = commands.get(arguments.get(0));
        Member author = Helper.instance.getMember(event.getAuthor());
        MessageChannel channel = event.getChannel();

        //happens if author is not member of server
        if (author == null) {
            Helper.instance.respond(channel, "I am utterly sorry, but my services are strictly limited to members of our server.");
            return;
        }

        System.out.println("> COMM: " + author.getEffectiveName() + " issued the following command:\n" + messageContent);

        //if there was no command found with the given keyword
        if (toExecute == null) {
            Helper.instance.respond(author, channel,
                    "Es tut mir Leid, doch ich habe keinen Command für " + arguments.get(0) + " gefunden.",
                    "I am utterly sorry, but I did not find a command for " + arguments.get(0) + ".");
            return;
        }

        if (messageContent.charAt(0) == '!') {
            if (RoleHandler.instance.hasMinimumRequiredRole(author, toExecute.getMinimumRequiredRole())) {
                String[] returnCode = toExecute.execute(arguments, event);
                if (returnCode != null) {
                    Helper.instance.respond(author, channel,
                            "Es tut mir Leid, doch etwas ist schief gelaufen:\n\n**Fehlermeldung:** *" + returnCode[0] + "*\n\n" + getGermanHelpText(arguments.get(0), toExecute),
                            "I am utterly sorry, but something went wrong.\n\n**Error message:** *" + returnCode[1] + "*\n\n" + getEnglishHelpText(arguments.get(0), toExecute));
                }
            } else {
                String minimumRequiredRole = toExecute.getMinimumRequiredRole().getName();
                Helper.instance.respond(author, channel,
                        "Es tut mir Leid, doch du hast nicht die minimale benötigte Rolle \"" + minimumRequiredRole + "\" für diesen Command.",
                        "I am utterly sorry, but you do not have the minimum required role \"" + minimumRequiredRole + "\" for this command.");
            }
        } else if (messageContent.charAt(0) == '?') {
            Helper.instance.respond(author, channel,
                    getGermanHelpText(arguments.get(0), toExecute),
                    getEnglishHelpText(arguments.get(0), toExecute));
        }
    }

    @NotNull
    private String getGermanHelpText(String usedArgument, Command toExecute) {
        String[] helpReturn = toExecute.getHelp();
        return "__**Hilfeseite " + usedArgument +  "**__\n\n" +
                "**Syntax:**" +
                "```diff\n!" +
                usedArgument + helpReturn[0] +
                "```\n**Synonyme:**```" +
                buildKeywordList(toExecute.getKeywords()) +
                "```\n**Minimale benötigte Rolle:** " + toExecute.getMinimumRequiredRole().getName() + "\n\n" +
                helpReturn[1];
    }

    @NotNull
    private String getEnglishHelpText(String usedArgument, Command toExecute) {
        String[] helpReturn = toExecute.getHelpEnglish();
        return "__**Help page " + usedArgument +  "**__\n\n" +
                "**Syntax:**" +
                "```diff\n!" +
                usedArgument + helpReturn[0] +
                "```\n**Synonyms:**```" +
                buildKeywordList(toExecute.getKeywords()) +
                "```\n**Minimum required Role:** " + toExecute.getMinimumRequiredRole().getName() + "\n\n" +
                helpReturn[1];
    }

    private String buildKeywordList(String[] keywords) {
        StringBuilder keywordList = new StringBuilder();
        for (String keyword : keywords) {
            keywordList.append(keyword).append("\n");
        }
        return keywordList.toString();
    }

    //converts the given string into an Arraylist that represents the command arguments
    //String is split on white spaces. If an argument is supposed to contain white spaces, it is bracketed with "
    private List<String> parseCommand(String content) {
        List<String> result = new ArrayList<>();
        //TODO: change this to use "\"" instead of "" + char
        char quote = 34;  // "
        String[] split = content.substring(1).split("" + quote);

        for (int i = 0; i < split.length; i++) {
            //as arguments that are bracketed with " are always bracketed using exactly two "s it is certain
            //that every even index in the resulted array needs to be split at white spaces
            if (i % 2 == 0) {
                for (String temp : split[i].split(" ")) {
                    //sorting out empty entries, in case someone forgot to put a whitespace between two bracketed arguments
                    if (!temp.equals("")) result.add(temp);
                }
            } else result.add(split[i]);
        }
        return result;
    }
}
