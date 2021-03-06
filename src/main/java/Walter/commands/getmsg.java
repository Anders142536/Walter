package Walter.commands;

import Walter.Helper;
import Walter.Parsers.StringOption;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.utils.Checks;

import java.util.ArrayList;

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
        try {
            event.getChannel().retrieveMessageById(messageID.getValue()).queue(
                    success -> {
                        //we first send a message we will edit immediatly. On edit the mention tag does not push a notification
                        event.getChannel().sendMessage(new EmbedBuilder().setTitle("Metainfo").build()).queue(
                                mes -> {
                                    EmbedBuilder metaInfo = new EmbedBuilder();
                                    metaInfo.setTitle("Metainfo");
                                    metaInfo.addField("Id", success.getId(), false);
                                    String authorName = success.getAuthor().getName();
                                    String memberName = Helper.getMember(success.getAuthor()).getEffectiveName();
                                    metaInfo.addField("Author", success.getAuthor().getId() + "   " + success.getAuthor().getAsMention() +
                                            (authorName.equals(memberName) ? "" : " (" + memberName + ")"), false);
                                    mes.editMessage(metaInfo.build()).queue();
                                    event.getChannel().sendMessage("```\n" + success.getContentRaw().replaceAll("```", "") + "```").queue();
                                    event.getMessage().delete().queue();
                                }
                        );
                    },
                    error -> {
                        Helper.respondError(event, new CommandExecutionException(new String[]{
                                "Couldn't get the given message. It has to be in the same channel as your " + commandName + " command!",
                                "Konnte die gegebene Nachricht nicht finden. Sie muss im selben Kanal sein wie dein " + commandName + " Befehl!"
                        }));
                    }
            );
        } catch (IllegalArgumentException e) {
            throw new CommandExecutionException(new String[] {
                    String.format("\"%s\" is not a message ID", messageID.getValue()),
                    String.format("\"%s\" ist keine message ID", messageID.getValue())
            });
        }

    }
}
