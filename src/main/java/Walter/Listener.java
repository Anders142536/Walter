package Walter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

//handles events and, for reasons of simplicity, holds settings.
public class Listener extends ListenerAdapter {

    //required objects
    private JDA jda;
    private CommandHandler commandHandler;
    private TwitterFeed fortniteFeed;

    //settings
    private int dropzoneLimit;
    private int pullrateTwitterFeed;

    /* ******** *
     *  EVENTS  *
     * ******** */

    //new members are announced in the general channel, tagging the admins
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TextChannel general = Helper.instance.getTextChannel(Collection.GENERAL_CHANNEL_ID);
        Role admin = RoleID.ADMIN.getRoleInstance();

        //sending the message. it shall look like this:
        //  @admin: NewMember hat sich im #foyer eingefunden.
        general.sendMessage(admin.getAsMention() + ": " + event.getMember().getEffectiveName() +
                " hat sich im <#" + Collection.FOYER_CHANNEL_ID + "> eingefunden.").queue();
    }

    //leaving members will be announced in the admin channel
    //furthermore they will receive a polite farewell by walter with a link to join the server in case they want to come back
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        TextChannel admin = Helper.instance.getTextChannel(Collection.ADMIN_CHANNEL_ID);

        admin.sendMessage(event.getMember().getEffectiveName() + " hat unseren Server verlassen.").queue();
        //TODO: this
    }

    //when someone is given the member or guest role walter sends them a private message with some basic information
    //this is done here instead of on the end of the command so that it will also be triggered when an admin
    //gives one of these roles via right-click
    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        //TODO: this
    }

    //log functionality to monitor the usage of voice channels and to see who are the most active members
    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        long IDvoiceJoined = event.getChannelJoined().getIdLong();

        //if the channel joined is the cinema channel
        if (IDvoiceJoined == Collection.CINEMA_CHANNEL_ID) {
            VoiceChannel channel = Helper.instance.getVoiceChannel(IDvoiceJoined);
            if (channel.getMembers().size() == 1) {
                //gets the current time and truncates it to only show hours and minutes
                LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
                channel.getManager().setName("\uD83C\uDF7F Cinema (" + currentTime + ")").complete();
            }
        }
    }

    //log functionality to monitor the usage of voice channels and to see who are the most active members
    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        long IDvoiceLeft = event.getChannelLeft().getIdLong();
        long IDvoiceJoined = event.getChannelJoined().getIdLong();
        if (IDvoiceLeft == Collection.CINEMA_CHANNEL_ID) {
            VoiceChannel channel = Helper.instance.getVoiceChannel(IDvoiceLeft);
            if (channel.getMembers().size() == 0)
                channel.getManager().setName("\uD83C\uDF7F Cinema").complete();
        } else if (IDvoiceJoined == Collection.CINEMA_CHANNEL_ID) {
            VoiceChannel channel = Helper.instance.getVoiceChannel(IDvoiceJoined);
            if (channel.getMembers().size() == 1) {
                //gets the current time and truncates it to only show hours and minutes
                LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
                channel.getManager().setName("\uD83C\uDF7F Cinema (" + currentTime + ")").complete();
            }
        }
    }

    //log functionality to monitor the usage of voice channels and to see who are the most active members
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        long IDvoiceLeft = event.getChannelLeft().getIdLong();

        //if the channel joined is the cinema channel
        if (IDvoiceLeft == Collection.CINEMA_CHANNEL_ID) {
            VoiceChannel channel = Helper.instance.getVoiceChannel(IDvoiceLeft);
            if (channel.getMembers().size() == 0)
                channel.getManager().setName("\uD83C\uDF7F Cinema").complete();
        }
    }

    //TODO: write comment about what exactly is done here
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String messageContent = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        long channelID = channel.getIdLong();

        try {
            if (messageContent.length() != 0 && (messageContent.charAt(0) == '!' || messageContent.charAt(0) == '?'))
                commandHandler.process(event);
        } catch (Exception e) {
            String informationToAdd = "channel:        " + channel.getName() +
                    "\nauthor:         " + event.getAuthor().getName() +
                    "\nmessageContent: \"" + messageContent + "\"\n";
            Helper.instance.respondException(channel, informationToAdd, e);
        }
        if (channelID == Collection.DROPZONE_CHANNEL_ID) {
            Member author = event.getMember();
            mentionVoiceChat(author, channel);
        }
    }

    private void mentionVoiceChat(Member author, MessageChannel channel) {
        StringBuilder mentions = new StringBuilder();

        //get a list of all the members that are in the same voice channel as the author of the message
        if (author.getVoiceState().inVoiceChannel()) {
            VoiceChannel voice = author.getVoiceState().getChannel();
            List<Member> vmembers = voice.getMembers();
            for (Member temp : vmembers) {
                if (author != temp) mentions.append(temp.getAsMention() + " ");
            }
            if (mentions.length() > 0) channel.sendMessage(mentions).queue();
        }
    }

    //does stuff that only needs to be done when walter is started
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("wooooohoooooo!!!!");
        jda = event.getJDA();
        Helper.instance = new Helper(jda);
        commandHandler = new CommandHandler();
        //TODO: Twitterfeeds
    }

    //prints the shudown code in the logger
    @Override
    public void onShutdown(ShutdownEvent event) {
        //TODO: make the logger print event.getCloseCode().getMeaning() once its implemented
    }




    /* ********* *
     *  METHODS  *
     * ********* */

    void test() {

    }

    void load() {

    }

    void save() {

    }

}
