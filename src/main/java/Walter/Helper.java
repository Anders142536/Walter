package Walter;

import Walter.enums.BlackCategory;
import Walter.enums.BlackChannel;
import Walter.enums.BlackRole;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

//bundles certain functionalities that are needed all over the place
public class Helper {

    public static Helper instance;
    private JDA jda;

    final private static long GUILD_ID = 254263827237961729L;


    public Helper (JDA jda) {
        this.jda = jda;
    }

    /* ************* *
     *  JDA Getters  *
     * ************* */

    //this is not stored as instances are invalidated after a certain period of time
    Guild getGuild() {
        return jda.getGuildById(GUILD_ID);
    }

    net.dv8tion.jda.api.entities.Category getCategory(BlackCategory blackCategory) { return getCategory(blackCategory.ID); }

    net.dv8tion.jda.api.entities.Category getCategory(long categoryID) { return getGuild().getCategoryById(categoryID); }

    TextChannel getTextChannel(BlackChannel blackChannel) { return getTextChannel(blackChannel.ID); }

    TextChannel getTextChannel(long channelID) { return getGuild().getTextChannelById(channelID);}

    VoiceChannel getVoiceChannel(BlackChannel blackChannel) {
        return getVoiceChannel(blackChannel.ID);
    }

    VoiceChannel getVoiceChannel(long channelID) { return getGuild().getVoiceChannelById(channelID); }

    public Member getMember(User user) {
        return getGuild().getMember(user);
    }

    public List<Member> getMembersByName(String name) {
        return getGuild().getMembersByEffectiveName(name, true);
    }

    /* ******************* *
     *  Other Helpmethods  *
     * ******************* */

    public void respond(Member member, MessageChannel channel, String german, String english) {
        if (RoleHandler.instance.hasRole(member, BlackRole.ENGLISH))
            channel.sendMessage(english).queue();
        else
            channel.sendMessage(german).queue();
    }

    public void respond (MessageChannel channel, String text) {
        channel.sendMessage(text).queue();
    }

    public void respondException(MessageChannel channel, String informationToAdd, Exception e) {


        String corePrint = "I am utterly sorry, but something went seriously wrong here." +
                "\n\n<@!151010441043116032>:\n" +
                "```timestamp:      " + getFormattedNowString() + "\n" +
                (informationToAdd != null ? informationToAdd + "" : "") + "```";
        System.out.println("> ERROR An exception was thrown!" + corePrint);
        channel.sendMessage(corePrint + getStackTraceString(e)).queue();
        e.printStackTrace();
    }

    void deleteMessagesOlderThan(MessageChannel channel, int limit, int catchAmount) {

        //getting the message history in the given channel
        //unfortunately retrievePast() does not terminate .complete() if more messages are asked for than there are,
        //therefore the forced return after 1 seconds.
        List<Message> pinned = channel.retrievePinnedMessages().completeAfter(1, TimeUnit.SECONDS);  //TODO see if the wait on time basis can be avoided
        List<Message> history = channel.getHistory().retrievePast(limit + pinned.size() + catchAmount).completeAfter(1, TimeUnit.SECONDS);


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
        StringBuilder builder = new StringBuilder("\nSTACKTRACE:\n");
        StackTraceElement[] stacktrace = e.getStackTrace();
        for (int i = 0; i < 10 && i < stacktrace.length; i++) {
            builder.append("\n").append(stacktrace[i]);
        }
        return builder.toString();
    }

    private void dummy() {

    }
}
