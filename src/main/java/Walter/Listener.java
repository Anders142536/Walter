package Walter;



import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

//handles events and, for reasons of simplicity, holds settings.
public class Listener extends ListenerAdapter {

    //required objects
    private JDA jda;
    private Helper helper;
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
        //fetching required objects
        TextChannel general = helper.getTextChannel(Collection.GENERAL_CHANNEL_ID);
        Role admin = helper.getRole(Collection.ADMIN_ROLE_ID);

        //sending the message. it shall look like this:
        //  @admin: NewMember hat sich im #foyer eingefunden.
        general.sendMessage(admin.getAsMention() + ": " + event.getMember().getEffectiveName() +
                " hat sich im <#" + Collection.FOYER_CHANNEL_ID + "> eingefunden.").queue();
    }

    //leaving members will be announced in the admin channel
    //furthermore they will receive a polite farewell by walter with a link to join the server in case they want to come back
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        TextChannel admin = helper.getTextChannel(Collection.ADMIN_CHANNEL_ID);

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
        //getting all the data to log
        long IDmemberJoined = event.getMember().getUser().getIdLong();
        long IDvoiceJoined = event.getChannelJoined().getIdLong();


        //logging voice activity
        try {
            File logFile = new File("./voice.logs");    //TODO: rename voicelogs file to voice.logs

            if (logFile.exists()) {
                FileWriter fw = new FileWriter(logFile, true);  //this only adds to the text file
                fw.write("1 " + IDmemberJoined + " " + IDvoiceJoined + " " + System.currentTimeMillis() + "\n");
                fw.close();
            } else {
                //TODO: log Collection.N;
            }
        } catch (IOException e) {
            //TODO: log e.toString();
            e.printStackTrace();
        }

        //if the channel joined is the cinema channel
        if (IDvoiceJoined == Collection.CINEMA_CHANNEL_ID) {
            VoiceChannel channel = helper.getVoiceChannel(IDvoiceJoined);
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
        //TODO: this
    }

    //log functionality to monitor the usage of voice channels and to see who are the most active members
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        //TODO: this
    }

    //TODO: write comment about what exactly is done here
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String messageContent = event.getMessage().getContentRaw();

        if (messageContent.charAt(0) == '!') commandHandler.execute(event);
        if (messageContent.charAt(0) == '?') commandHandler.printHelp(event);
    }

    //does stuff that only needs to be done when walter is started
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("wooooohoooooo!!!!");
        jda = event.getJDA();
        helper = new Helper(jda);
        commandHandler = new CommandHandler(helper);
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
