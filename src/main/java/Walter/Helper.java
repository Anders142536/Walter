package Walter;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

//bundles certain functionalities that are needed all over the place
public class Helper {

    private JDA jda;

    public Helper(JDA jda) {
        this.jda = jda;
    }


    /* ************* *
     *  JDA Getters  *
     * ************* */

    Guild getGuild() {
        return jda.getGuildById(Collection.GUILD_ID);
    }

    TextChannel getTextChannel(long channelID) {
        return getGuild().getTextChannelById(channelID);
    }

    VoiceChannel getVoiceChannel(long channelID) {
        return getGuild().getVoiceChannelById(channelID);
    }

    Role getRole(long roleID) {
        return getGuild().getRoleById(roleID);
    }



    /* ******************* *
     *  Other Helpmethods  *
     * ******************* */

    boolean checkRole(Member member, Role role) {
        return member.getRoles().contains(role);
    }

    void deleteMessagesOlderThan(MessageChannel channel, int limit, int catchAmount) {

        //getting the message history in the given channel
        //unfortunately retrievePast() does not terminate .complete() if more messages are asked for than there are,
        //therefore the forced return after 1 seconds.
        List<Message> pinned = channel.getPinnedMessages().completeAfter(1, TimeUnit.SECONDS);  //TODO see if the wait on time basis can be avoided
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
