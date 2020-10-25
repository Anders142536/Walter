package Walter.commands;

import Walter.*;
import Walter.Parsers.Flag;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class commands extends Command {
    //lists contain one embed per language
    List<MessageEmbed> guestCommands;
    List<MessageEmbed> memberCommands;
    List<MessageEmbed> adminCommands;

    private Flag all;

    public commands() {
        super(new String[] {
            "This command lists all the commands available to you",
            "Dieser Command listet alle dir zur Verfügung stehenden Commands auf"});
        keywords = new String[][]{
                {"commands"},
                {"befehle"}};
        all = new Flag('a', "all", new String[] {
                "Also show non-available commands",
                "Zeige auch nicht verfügbare Befehle"
        });
        flags = new ArrayList<>();
        flags.add(all);
        minimumRequiredRole = BlackRole.GUEST;

        fillCommandEmbeds();
    }

    private void fillCommandEmbeds() {
        List<StringBuilder> guestCommandsStrings = new ArrayList<>();
        List<StringBuilder> memberCommandsStrings = new ArrayList<>();
        List<StringBuilder> adminCommandsStrings = new ArrayList<>();

        //One StringBuilder per commandtype (guest, member, admin) and language, as those represent logically distinct blocks
        for (Language lang : Language.values()) {
            guestCommandsStrings.add(new StringBuilder());
            memberCommandsStrings.add(new StringBuilder());
            adminCommandsStrings.add(new StringBuilder());
        }

        for (Command command : CommandProcessor.instance.getListOfCommands()) {
            if (command.getMinimumRequiredRole() == BlackRole.GUEST) appendCommandString(guestCommandsStrings, command);
            else if (command.getMinimumRequiredRole() == BlackRole.MEMBER) appendCommandString(memberCommandsStrings, command);
            else appendCommandString(adminCommandsStrings, command);
        }

        EmbedBuilder builder;
        String[] headers = getHeaderStrings();
        for (Language lang : Language.values()) {
            builder = new EmbedBuilder();
            builder.setColor(7854123);
            builder.setDescription(headers[lang.index]);
            builder.setFooter("Walter v" + Walter.VERSION);

            builder.addField("Guest", guestCommandsStrings.get(lang.index).toString(), false);
            guestCommands.add(builder.build());

            builder.addField("Member", memberCommandsStrings.get(lang.index).toString(), false);
            memberCommands.add(builder.build());

            builder.addField("Admin", adminCommandsStrings.get(lang.index).toString(), false);
            adminCommands.add(builder.build());
        }
}

    private String[] getHeaderStrings() {
        String[] headers = {
                "These are the commands at your disposal:\n\n" +
                        "Please keep in mind that many of the commands listed here have synonyms. For a " +
                        "detailed explanation of a command please call the command with a ? instead of a !.",
                "Dies sind die Commands die dir zur Verfügung stehen. \n\n" +
                    "Bitte bedenke, dass viele der hier gelisteten Commands Synonyme haben. Für eine genaue " +
                    "Erklärung eines Commands rufe ihn bitte mit einem ? anstelle eines ! auf."
                };
        if (headers.length < Language.values().length)
            Helper.instance.logError("Walter.commands.commands::getHeaderStrings\n" +
                    "headers list is shorter than number of languages");
        return headers;
    }

    private void appendCommandString(List<StringBuilder> builders, Command command) {
        for (Language lang : Language.values()) {
            builders.get(lang.index)
                    .append("!")
                    .append(command.getKeywords(lang)[0])
                    .append("\n");
        }
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        Member author = Helper.instance.getMember(event.getAuthor());
        MessageChannel channel = event.getChannel();
        int languageIndex = Language.getLanguage(author).index;

        if (all.isGiven() || RoleHandler.hasRole(author, BlackRole.ADMIN))
            if (languageIndex >= adminCommands.size()) channel.sendMessage(adminCommands.get(0)).queue();
            else channel.sendMessage(adminCommands.get(languageIndex)).queue();
        else if (RoleHandler.hasRole(author, BlackRole.MEMBER))
            if (languageIndex >= memberCommands.size()) channel.sendMessage(memberCommands.get(0)).queue();
            else channel.sendMessage(memberCommands.get(languageIndex)).queue();
        else
        if (languageIndex >= guestCommands.size()) channel.sendMessage(guestCommands.get(0)).queue();
        else channel.sendMessage(guestCommands.get(languageIndex)).queue();
    }
}