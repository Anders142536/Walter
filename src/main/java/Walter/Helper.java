package Walter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

//bundles certain functionalities that are needed all over the place
public class Helper {

    public static Helper instance;
    private JDA jda;


    public Helper (JDA jda) {
        this.jda = jda;
    }

    /* ************* *
     *  JDA Getters  *
     * ************* */

    //this is not stashed as instances are invalidated after a certain period of time
    Guild getGuild() {
        return jda.getGuildById(Collection.GUILD_ID);
    }

    TextChannel getTextChannel(long channelID) {
        return getGuild().getTextChannelById(channelID);
    }

    VoiceChannel getVoiceChannel(long channelID) {
        return getGuild().getVoiceChannelById(channelID);
    }

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
        if (RoleHandler.instance.hasRole(member, RoleID.ENGLISH))
            channel.sendMessage(english).queue();
        else
            channel.sendMessage(german).queue();
    }

    public void respond (MessageChannel channel, String text) {
        channel.sendMessage(text).queue();
    }

    public void respondException(MessageChannel channel, String informationToAdd, Exception e) {

        Calendar now = Calendar.getInstance();
        String corePrint = "\n<@!151010441043116032>:\nTIME AND DATE:  " +
                now.get(Calendar.YEAR) + "/" + now.get(Calendar.MONTH) + "/" + now.get(Calendar.DAY_OF_MONTH) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);
        if (informationToAdd.length() != 0) corePrint += "\n\nADDITIONAL INFORMATION:\n" + informationToAdd;
        corePrint += "\n\nSTACKTRACE:\n" + e.getStackTrace()[0];
        channel.sendMessage("I am utterly sorry, but something went seriously wrong here." + corePrint).queue();
        System.out.println("> ERROR An exception was thrown!" + corePrint);
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

}
