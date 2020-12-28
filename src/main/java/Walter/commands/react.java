package Walter.commands;

import Walter.Helper;
import Walter.Parsers.Flag;
import Walter.Parsers.FlushOption;
import Walter.Parsers.StringOption;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.requests.Route;

import java.util.ArrayList;
import java.util.List;

public class react extends Command {
    StringOption messageID;
    FlushOption rest;

    Flag clear;

    public react() {
        super(new String[]{
                "Reacts to the message with ID **messageID** with the given emojis",
                "Reagiert auf die Nachricht mit der ID **messageID** mit den benutzten Emojis"
        });
        keywords = new String[][]{
                {"react"},
                {"reagiere"}
        };
        minimumRequiredRole = BlackRole.MEMBER;
        messageID = new StringOption(new String[]{"messageID"}, new String[] {
                "Message to react to",
                "Nachricht auf die reagiert werden soll"
        });
        rest = new FlushOption(new String[] {"Emojis"}, new String[] {
                "Emojis to react with",
                "Emojis mit denen reagiert werden soll"
        });

        clear = new Flag('c', "clear", new String[] {
                "Removes all reacted emojis",
                "Entfernt alle Emojis mit denen reagiert wurde"
        });

        options = new ArrayList<>();
        flags = new ArrayList<>();
        options.add(messageID);
        options.add(rest);
        flags.add(clear);

    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) {
        event.getChannel().retrieveMessageById(messageID.getValue()).queue(
                success -> {
                    if (clear.isGiven()) success.clearReactions().queue();
                    else {
                        List<Emote> emotes = event.getMessage().getEmotes();
                        if (emotes.size() == 0)
                            Helper.respondError(event, new CommandExecutionException(new String[] {
                                "There were no Emojis given",
                                "Es wurden keine Emojis gegeben"
                            }));
                        else {
                            for (Emote e : emotes) {
                                success.addReaction(e).queue();
                            }
                        }
                    }
                },
                error -> {
                    Helper.respondError(event, new CommandExecutionException(new String[]{
                            "Couldn't get the given message. It has to be in the same channel as your " + usedKeyword + " command!",
                            "Konnte die gegebene Nachricht nicht finden. Sie muss im selben Kanal sein wie dein " + usedKeyword + " Befehl!"
                    }));}
        );
    }
}
