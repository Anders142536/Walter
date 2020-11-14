package Walter;

import Walter.entities.BlackCategory;
import Walter.entities.BlackChannel;
import Walter.entities.BlackRole;
import Walter.entities.Language;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

//bundles certain functionalities that are needed all over the place
public class Helper {

    public static Helper instance;
    private final JDA jda;

    final private static long GUILD_ID = 254263827237961729L;


    public Helper(JDA jda) {
        this.jda = jda;
    }

    /* ************* *
     *  JDA Getters  *
     * ************* */

    //this is not stored as instances are invalidated after a certain period of time
    Guild getGuild() {
        return jda.getGuildById(GUILD_ID);
    }

    net.dv8tion.jda.api.entities.Category getCategory(BlackCategory blackCategory) {
        return getCategory(blackCategory.ID);
    }

    net.dv8tion.jda.api.entities.Category getCategory(long categoryID) {
        return getGuild().getCategoryById(categoryID);
    }

    public TextChannel getTextChannel(BlackChannel blackChannel) {
        return getTextChannel(blackChannel.ID);
    }

    public TextChannel getTextChannel(long channelID) {
        return getGuild().getTextChannelById(channelID);
    }

    VoiceChannel getVoiceChannel(BlackChannel blackChannel) {
        return getVoiceChannel(blackChannel.ID);
    }

    VoiceChannel getVoiceChannel(long channelID) {
        return getGuild().getVoiceChannelById(channelID);
    }

    public Member getMember(User user) {
        return getGuild().retrieveMember(user).complete();
    }

    public List<Member> getMembersByName(String name) {
        return getGuild().getMembersByEffectiveName(name, true);
    }


    /* ******************* *
     *  Other Helpmethods  *
     * ******************* */

    public void logCommand(Member author, MessageChannel channel, String commandMessageRaw) {
        String messageToSend =
                "`AUTHOR: ` " + author.getEffectiveName() + " (" + author.getId() + ")\n" +
                "`CHANNEL:` " + channel.getName() + " (" + channel.getId() + ")\n" +
                "`COMMAND:` " + commandMessageRaw;
        System.out.println(messageToSend);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Command");
        builder.setColor(7854123);
        builder.setDescription(messageToSend);
        builder.setFooter("Walter v" + Walter.VERSION);
        getTextChannel(BlackChannel.LOG).sendMessage(builder.build()).queue();
    }

    public void logError(String logMessage) {
        System.out.println("ERROR :   " + logMessage);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Error");
        builder.setColor(13722624);
        builder.setDescription("<@!151010441043116032>\n" + logMessage);
        builder.setFooter("Walter v" + Walter.VERSION);
        getTextChannel(BlackChannel.LOG).sendMessage(builder.build()).queue();
    }

    public void logException(String logMessage) {
        System.out.println("AN UNHANDLED EXCEPTION OCCURED\n" + logMessage);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Exception");
        builder.setColor(13697024);
        builder.setDescription("<@!151010441043116032>\n" + logMessage);
        builder.setFooter("Walter v" + Walter.VERSION);
        getTextChannel(BlackChannel.LOG).sendMessage(builder.build()).queue();
    }

    public void logInfo(String logMessage) {
        System.out.println("INFO:    " + logMessage);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Info");
        builder.setColor(7854123);
        builder.setDescription(logMessage);
        builder.setFooter("Walter v" + Walter.VERSION);
        getTextChannel(BlackChannel.LOG).sendMessage(builder.build()).queue();
    }

    public void respond(Member member, MessageChannel channel, String german, String english) {
        if (RoleHandler.hasRole(member, BlackRole.ENGLISH))
            channel.sendMessage(english).queue();
        else
            channel.sendMessage(german).queue();
    }

    public void respond(MessageChannel channel, String text) {
        channel.sendMessage(text).queue();
    }

    public void respondError(MessageReceivedEvent event, CommandExecutionException e) {
        respond(getMember(event.getAuthor()), event.getChannel(),
                "Es tut mir Leid, doch etwas ist beim Ausführen deines Befehls schief gelaufen:\n" +
                        e.getReason(Language.GERMAN) + "\n" + BlackRole.ADMIN.getAsMention(),
                "I am utterly sorry, but something went wrong trying to execute your command:\n" +
                        e.getReason(Language.ENGLISH) + "\n" + BlackRole.ADMIN.getAsMention());
        String errorlogMessage =
                "```channel:        " + event.getChannel().getName() +
                        "\nauthor:         " + event.getAuthor().getName() + " <@!" + event.getAuthor().getId() + ">" +
                        "\nmessageContent: " + event.getMessage().getContentRaw() +
                        "\nError Reason:   " + e.getReason(Language.ENGLISH) + "```";
        Helper.instance.logError(errorlogMessage);
    }

    public void respondException(MessageReceivedEvent event, Exception e) {
        String corePrint = "timestamp:      " + getFormattedNowString() +
                "\nchannel:        " + event.getChannel().getName() +
                "\nauthor:         " + event.getAuthor().getName() + " <@!" + event.getAuthor().getId() + ">" +
                "\nmessageContent: " + event.getMessage().getContentRaw();

        System.out.println("> ERROR An exception was thrown!" + corePrint);
        event.getChannel().sendMessage("I am utterly sorry, but something went seriously wrong here.\n" +
                (e instanceof ReasonedException ? ((ReasonedException) e).getReason(Language.ENGLISH) : e.getMessage()) +
                "\n\n<@!151010441043116032>:\n```" + corePrint + "```" + getStackTraceString(e)).queue();

        logException("```" + corePrint + "```" + getStackTraceString(e));
        e.printStackTrace();
    }

    void deleteUnpinnedMessagesOlderThan(MessageChannel channel, int limit, int catchAmount) {

        //getting the message history in the given channel
        //unfortunately retrievePast() does not terminate .complete() if more messages are asked for than there are,
        //therefore the forced return after 1 seconds.
        List<Message> pinned = channel.retrievePinnedMessages().completeAfter(1, TimeUnit.SECONDS);  //TODO see if the wait on time basis can be avoided
        List<Message> history = channel.getHistory().retrievePast(Math.min(limit + pinned.size() + catchAmount, 100)).completeAfter(1, TimeUnit.SECONDS);


        //if the number of messages in a channel has surpassed the allowed limit
        if (history.size() > limit) {
            for (int i = limit; i < history.size(); i++) {
                if (!history.get(i).isPinned()) {
                    history.get(i).delete().complete();
                }
            }
        }
    }

    public static void clearChannelOfMessages(MessageChannel channel) {
        getChannelHistory(channel).forEach((msg) -> msg.delete().complete());
    }

    public static List<Message> getChannelHistory(MessageChannel channel) {
        MessageHistory history = channel.getHistory();
        int lastSize;

        do {
            System.out.println("new enter in the loop, history length: " + history.size());
            lastSize = history.size();
            history.retrievePast(100).completeAfter(1, TimeUnit.SECONDS);
        } while (history.size() != lastSize);


        System.out.println("final size: " + history.size());
        return history.getRetrievedHistory();
    }

    public String getFormattedNowString() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR) + "/" + (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DAY_OF_MONTH) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);
    }

    public static ArrayList<String> splitMessageOnLinebreak(String msg, int limit) {
        ArrayList<String> submsgs = new ArrayList<>();

        if (msg.length() < limit) submsgs.add(msg);
        else {
            StringBuilder submsg = new StringBuilder();
            for(String line: msg.split(System.lineSeparator())) {

                if (line.length() + submsg.length() > limit) {
                    submsgs.add(submsg.toString());
                    submsg.setLength(0); //clearing the builder
                }
                submsg.append(line).append("\n");
            }
            submsgs.add(submsg.toString());
        }

        return submsgs;
    }

    private String getStackTraceString(Exception e) {
        StringBuilder builder = new StringBuilder("\nSTACKTRACE:\n`" +
                e.getClass().getSimpleName() + "`");
        StackTraceElement[] stacktrace = e.getStackTrace();
        for (int i = 0; i < 10 && i < stacktrace.length; i++) {
            builder.append("\n").append(stacktrace[i]);
        }
        return builder.toString();
    }
}
