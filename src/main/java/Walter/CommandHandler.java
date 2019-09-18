package Walter;

import Walter.Walter;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//this class handles the recognition of commands and their execution, as well as their loading
class CommandHandler {

    HashMap<String, Command>  commands = new HashMap<>();
    Helper helper;

    CommandHandler(Helper helper) {
        this.helper = helper;
        initializeCommands();
    }

    //loads the commands
    void initializeCommands() {
        //TODO: in the long run, use reflection to find all classes instead of this list here

    }

    //converts the given string into an Arraylist that represents the command arguments
    //String is split on white spaces. If an argument is supposed to contain white spaces, it is bracketed with "
    private List<String> parseCommand(String content) {
        List<String> result = new ArrayList<>();

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

    //processes the given command
    //if the command object returns -1 or null it is not yet implemented.
    void process(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        List<String> arguments = parseCommand(content);
        Command toExecute = commands.get(arguments.get(0));

        //detecting wether it was a !-  or a ?-command
        char prefix = content.charAt(0);
        if (prefix == '!') {

        } else if (prefix == '?') {
            MessageChannel channel = event.getChannel();
            Member author = event.getMember();

            //TODO get help string[] from list depending on language of the author
            if (helper.checkRole(author, helper.getRole(Collection.ENGLISH_ROLE_ID))) {
                String[] helpText = toExecute.getHelpEnglish();
            } else {
                String[] helpText = toExecute.getHelp();
            }

            String result = "```!" + arguments.get(0) + "```"
        } else {
            //TODO: Error, something went terribly wrong
        }

        /*TODO: parse command and determine wether the command was a !- or ?-command
            if it was a !-command, execute it
            if it was a ?-command, build the message that is returned with the String-Array returned
                by the getHelp() method.

            help method, depending on the language:
            String result = "```!" + arguments[0] + helpReturn[0]"```\n" +
                helpReturn[1] +
                "\n\nSchlüsselworte für diesen Command sind:```" +
                for (String keyword: command.getKeywords) {
                    result[1] += keyword + "\n";
                }
                "```Achtung:\nDie Groß- und Kleinschreibung ist immer irrelevant.";

            channel.sendMessage(result);
         */
    }

    List<Command> getCommandList() {
        List<Command> list = new ArrayList<Command>();


        return list;
    }
}
