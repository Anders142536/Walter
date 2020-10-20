package Walter;

import Walter.entities.BlackCategory;
import Walter.entities.BlackChannel;
import Walter.entities.BlackRole;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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

    TextChannel getTextChannel(BlackChannel blackChannel) {
        return getTextChannel(blackChannel.ID);
    }

    TextChannel getTextChannel(long channelID) {
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
        String messageToSend = "\nAUTHOR:  " + author.getEffectiveName() + " (" + author.getId() + ")" +
                "\nCHANNEL: " + channel.getName() + " (" + channel.getId() + ") " +
                "\nCOMMAND: " + commandMessageRaw;
        getTextChannel(BlackChannel.LOG).sendMessage("```yaml" + messageToSend + "```").queue();
        System.out.println(messageToSend);
    }

    public void logError(String logMessage) {
        String messageToSend = "ERROR :   " + logMessage;

        getTextChannel(BlackChannel.LOG).sendMessage("```yaml\n<@!151010441043116032>\n" + messageToSend + "```").queue();
        System.out.println(messageToSend);
    }

    public void logException(String logMessage) {
        logError("AN UNHANDLED EXCEPTION OCCURED\n" + logMessage);
    }

    public void logInfo(String logMessage) {
        String messageToSend = "INFO:    " + logMessage;
        getTextChannel(BlackChannel.LOG).sendMessage("```yaml\n" + messageToSend + "```").queue();
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

    public void respondException(MessageReceivedEvent event, Exception e) {
        String corePrint = "I am utterly sorry, but something went seriously wrong here." +
                "\n\n<@!151010441043116032>:\n" + //tagging anders
                "```timestamp:      " + getFormattedNowString() +
                "\nchannel:        " + event.getChannel().getName() +
                "\nauthor:         " + event.getAuthor().getName() + " <@!" + event.getAuthor().getId() + ">" +
                "\nmessageContent: " + event.getMessage().getContentRaw() + "```";

        Helper.instance.respondException(event, e);
        System.out.println("> ERROR An exception was thrown!" + corePrint);
        event.getChannel().sendMessage(corePrint + getStackTraceString(e)).queue();

        logException(corePrint + getStackTraceString(e));
        e.printStackTrace();
    }

    void deleteMessagesOlderThan(MessageChannel channel, int limit, int catchAmount) {

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

    public String getFormattedNowString() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR) + "/" + now.get(Calendar.MONTH) + "/" + now.get(Calendar.DAY_OF_MONTH) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);
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
