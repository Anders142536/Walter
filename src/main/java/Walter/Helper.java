package Walter;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
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

    //this is not stashed as instances are invalidated after a certain period of time
    private Guild getGuild() {
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

    Member getMember(User user) {
        return getGuild().getMember(user);
    }


    /* ******************* *
     *  Other Helpmethods  *
     * ******************* */

    boolean hasRole(Member member, Role role) {
        return member.getRoles().contains(role);
    }

    boolean hasRole(Member member, long roleID) {
        return member.getRoles().contains(getRole(roleID));
    }

    boolean hasMinimumRequiredRole(Member member, long roleID) {
        System.out.println("checking minimum required role: " + getRole(roleID).getName());
        boolean hasMinimumRequiredRole = false;
        switch (roleID + "") {
            case Collection.GUEST_ROLE_ID + "":
                if (hasRole(member, Collection.GUEST_ROLE_ID)) hasMinimumRequiredRole = true;
            case Collection.MEMBER_ROLE_ID + "":
                if (hasRole(member, Collection.MEMBER_ROLE_ID)) hasMinimumRequiredRole = true;
            case Collection.ADMIN_ROLE_ID + "":
                if (hasRole(member, Collection.ADMIN_ROLE_ID)) hasMinimumRequiredRole = true;
        }
        System.out.println("has minimum required role: " + hasMinimumRequiredRole);
        return hasMinimumRequiredRole;
    }

    void respond(Member member, MessageChannel channel, String german, String english) {
        System.out.println("responding");
        if (hasRole(member, Collection.ENGLISH_ROLE_ID))
            channel.sendMessage(english).queue();
        else
            channel.sendMessage(german).queue();
    }

    void respond (MessageChannel channel, String text) {
        System.out.println("responding");
        channel.sendMessage(text).queue();
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
