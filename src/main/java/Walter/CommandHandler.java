package Walter;



import Walter.commands.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//this class handles the recognition of commands and their execution, as well as their loading
class CommandHandler {

    private HashMap<String, Command>  commands = new HashMap<>();
    private Helper helper;

    CommandHandler(Helper helper) {
        this.helper = helper;
        loadCommandsToHashMap();
    }

    private void loadCommandsToHashMap() {
        //TODO: in the long run, use reflection to find all classes instead of this list here
        for (Command command :
                getListOfCommands()) {
            for (String keyword :
                    command.getKeywords()) {
                commands.put(keyword, command);
            }
        }
    }

    private List<Command> getListOfCommands() {
        List<Command> commandList = new ArrayList<Command>();

        commandList.add(new analyze());
        commandList.add(new commands());
        commandList.add(new config());
        commandList.add(new editmsg());
        commandList.add(new english());
        commandList.add(new file());
        commandList.add(new german());
        commandList.add(new getmsg());
        commandList.add(new guest());
        commandList.add(new hello());
        commandList.add(new help());
        commandList.add(new listening());
        commandList.add(new member());
        commandList.add(new patch());
        commandList.add(new pimmel());
        commandList.add(new playing());
        commandList.add(new reprint());
        commandList.add(new roll());
        commandList.add(new say());
        commandList.add(new shutdown());
        commandList.add(new test());
        commandList.add(new watching());

        System.out.println("length of commandlist: " + commandList.size());
        return commandList;
    }

    void execute(MessageReceivedEvent event) {
        List<String> arguments = parseCommand(event.getMessage().getContentRaw());
        Command toExecute = commands.get(arguments.get(0));
        Member author = helper.getMember(event.getAuthor());

        //happens if author is not member of server
        if (author == null) {
            helper.respond(event.getChannel(), "I am utterly sorry, but my services are strictly limited to members of our server.");
            return;
        }

        //if there was no command found with the given keyword
        if (toExecute == null) {
            helper.respond(author, event.getChannel(),
                    "Es tut mir Leid, doch ich habe keinen Command für " + arguments.get(0) + " gefunden.",
                    "I am utterly sorry, but I did not find a command for " + arguments.get(0) + ".");
            return;
        }

        String minimumRequiredRole = helper.getRole(toExecute.minimumRequiredRole).getName();
        if (helper.hasMinimumRequiredRole(author, toExecute.minimumRequiredRole)) {
            System.out.println("Minimum required Role: " + minimumRequiredRole);
            toExecute.execute(arguments, event, helper);
        } else
            helper.respond(author, event.getChannel(),
                    "Es tut mir Leid, doch du hast nicht die minimale benötigte Rolle \"" + minimumRequiredRole + "\" für diesen Command.",
                    "I am utterly sorry, but you do not have the minimum required role \"" + minimumRequiredRole + "\" for this command.");
    }

    void printHelp(MessageReceivedEvent event) {
        List<String> arguments = parseCommand(event.getMessage().getContentRaw());
        Command toExecute = commands.get(arguments.get(0));
        Member author = helper.getMember(event.getAuthor());
        MessageChannel channel = event.getChannel();

        //happens if author is not member of server
        if (author == null) {
            helper.respond(channel, "I am utterly sorry, but my services are strictly limited to members of our server.");
            return;
        }

        //building the respective help texts and sending them
        if (helper.hasRole(author, Collection.ENGLISH_ROLE_ID)) {
            helper.respond(channel, getEnglishHelpText(arguments.get(0), toExecute));
        } else {
            helper.respond(channel, getGermanHelpText(arguments.get(0), toExecute));
        }
    }

    @NotNull
    private String getGermanHelpText(String usedArgument, Command toExecute) {
        String[] helpReturn = toExecute.getHelp();
        StringBuilder result = new StringBuilder();
        result.append("```diff\n!")
                .append(usedArgument)
                .append(helpReturn[0])
                .append("```\n**Synonyme:**```");

        for (String keyword :
                toExecute.getKeywords()) {
            result.append(keyword)
                    .append("\n");
        }

        result.append("```\n**Minimale benötigte Rolle:** ")
                .append(helper.getRole(toExecute.minimumRequiredRole).getName())
                .append("\n\n")
                .append(helpReturn[1]);
        return result.toString();
    }

    @NotNull
    private String getEnglishHelpText(String usedArgument, Command toExecute) {
        String[] helpReturn = toExecute.getHelpEnglish();
        StringBuilder result = new StringBuilder();
        result.append("```diff\n!")
                .append(usedArgument)
                .append(helpReturn[0])
                .append("```\n**Synonyms:**```");

        for (String keyword :
                toExecute.getKeywords()) {
            result.append(keyword)
                    .append("\n");
        }

        result.append("```\n**Minimum required Role:** ")
                .append(helper.getRole(toExecute.minimumRequiredRole).getName())
                .append("\n\n")
                .append(helpReturn[1]);
        return result.toString();
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
