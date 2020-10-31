package Walter.commands;

import Walter.Helper;
import Walter.Parsers.StringOption;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class getmsg extends Command {

    StringOption messageID;

    public getmsg() {
        super(new String[] {
                "I print the message with the ID **ID** both into the chat and the console. To access the IDs of " +
                    "messages you need to enable the developer settings under User Settings > Appearance > " +
                    "Developer Mode. Then you can right click any message to access its ID. The message to be printed " +
                    "needs not to be posted by me.",
                "Ich werfe die Nachricht mit der ID **ID** in den Chat und die Konsole aus. Um die ID von Nachrichten " +
                        "auslesen zu können musst du die Developer Settings aktivieren unter User Settings > Appearance " +
                        "> Developer Mode. Dann kannst du mit einem Rechtsklick die ID von Nachrichten auslesen. Die " +
                        "auszuwerfende Nachricht muss nicht von mir verfasst worden sein."
        });
        minimumRequiredRole = BlackRole.GUEST;
        keywords = new String[][]{
                {"getmsg"}};
        messageID = new StringOption(new String[] {"message ID", "Nachrichten ID"}, new String[] {
                "ID of the message to throw",
                "ID der auszuwerfenden Nachricht"
        });
        options = new ArrayList<>();
        options.add(messageID);
    }

    @Override
    public void execute(String commandName, MessageReceivedEvent event) throws CommandExecutionException {
        event.getChannel().retrieveMessageById(messageID.getValue()).queue(
                success -> {
                    //we first send a message we will edit immediatly. On edit the mention tag does not push a notification
                    event.getChannel().sendMessage(" ").queue(
                            mes -> {
                                EmbedBuilder metaInfo = new EmbedBuilder();
                                metaInfo.addField("Id", success.getId(), false);
                                String authorName = success.getAuthor().getName();
                                String memberName = Helper.instance.getMember(success.getAuthor()).getEffectiveName();
                                metaInfo.addField("Author", success.getAuthor().getId() + "   " + success.getAuthor().getAsMention() +
                                        (authorName.equals(memberName) ? "" : " (" + memberName + ")"), false);
                                mes.editMessage(metaInfo.build()).queue();
                                event.getChannel().sendMessage("```\n" + success.getContentRaw().replaceAll("```", "") + "```").queue();
                                event.getMessage().delete().queue();
                            }
                    );
                },
                error -> {
                    Helper.instance.respondError(event, new CommandExecutionException(new String[]{
                            "Couldn't get the given message. It has to be in the same channel as your " + commandName + " command!",
                            "Konnte die gegebene Nachricht nicht finden. Sie muss im selben Kanal sein wie dein " + commandName + " Befehl!"
                    }));}
        );

    }
}
