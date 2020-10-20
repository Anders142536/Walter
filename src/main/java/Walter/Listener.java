package Walter;

import Walter.Parsers.CommandParser;
import Walter.entities.BlackChannel;
import Walter.entities.BlackRole;
import Walter.entities.BlackWebhook;
import Walter.exceptions.ParseException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

//handles events and, for reasons of simplicity, holds settings.
public class Listener extends ListenerAdapter {

    //required objects
    private JDA jda;

    //new members are announced in the general channel, tagging the admins
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TextChannel general = Helper.instance.getTextChannel(BlackChannel.GENERAL);
        RoleHandler.instance.assignRole(event.getMember(), BlackRole.GUEST);

        general.sendMessage("Werte " + BlackRole.MEMBER.getName() + ", unser Gast " + event.getMember().getAsMention() +
                " ist eingetroffen. Herzlich Willkommen!").queue();
    }

    //leaving members will be announced in the admin channel
    //furthermore they will receive a polite farewell by walter with a link to join the server in case they want to come back
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        TextChannel admin = Helper.instance.getTextChannel(BlackChannel.ADMIN);

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
        if (IDvoiceJoined == BlackChannel.CINEMA.ID) {
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
        if (IDvoiceLeft == BlackChannel.CINEMA.ID) {
            VoiceChannel channel = Helper.instance.getVoiceChannel(IDvoiceLeft);
            if (channel.getMembers().size() == 0)
                channel.getManager().setName("\uD83C\uDF7F Cinema").complete();
        } else if (IDvoiceJoined == BlackChannel.CINEMA.ID) {
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
        if (IDvoiceLeft == BlackChannel.CINEMA.ID) {
            VoiceChannel channel = Helper.instance.getVoiceChannel(IDvoiceLeft);
            if (channel.getMembers().size() == 0)
                channel.getManager().setName("\uD83C\uDF7F Cinema").complete();
        }
    }

    //TODO: write comment about what exactly is done here
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot()) return;
        String messageContent = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        Member member = event.getMember();

        //check if member of server
        if (member == null) {
            member = Helper.instance.getMember(author);
            if (member == null) {
                Helper.instance.respond(channel, "I am utterly sorry, but my services are strictly limited to members of our server.");
                Helper.instance.logInfo("Unknown User " + author.getAsMention() +
                        " (" + author.getId() + ") tried to contact me using the following message:\n" + messageContent);
                return;
            }
        }

        //decision time
        long channelID = channel.getIdLong();
        try {
            if (CommandParser.isCommand(messageContent))
                CommandProcessor.instance.process(event);
            else if (channelID == BlackChannel.DROPZONE.ID && !messageContent.matches("[$%].*")) //$ and % are prefixes that should be ignoredcd
                mentionVoiceChat(member, channel);

            if (channelID == BlackChannel.DROPZONE.ID) Helper.instance.deleteMessagesOlderThan(channel, 50, 10);

            if (channelID == BlackChannel.NEWS.ID) {
                List<Attachment> attachments = event.getMessage().getAttachments();

                BlackWebhook.SERVERNEWS.sendMessage(messageContent + "\n\n*Brought to you by:* " + author.getAsMention(), attachments);
                event.getMessage().delete().queue();
            }
        } catch (ParseException e) {
            Helper.instance.respond(member, channel,
                    "Es tut mir Leid, doch etwas ist beim Verstehen deines Befehls schief gelaufen.\n" + e.getReason(Language.GERMAN) + "\n" + BlackRole.ADMIN.getAsMention(),
                    "I am utterly sorry, but something went wrong trying to understand your command.\n" + e.getReason(Language.ENGLISH) + "\n" + BlackRole.ADMIN.getAsMention());
        } catch (Exception e) {
            String informationToAdd = "channel:        " + channel.getName() +
                    "\nauthor:         " + author.getName() + " <@!" + author.getId() + ">" +
                    "\nmessageContent: " + messageContent + "";
            Helper.instance.respondException(event, e);
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
        System.out.println("onReady triggered, JDA object launched without exception.");
        jda = event.getJDA();
        Helper.instance = new Helper(jda);
        CommandProcessor.instance = new CommandProcessor();
        RoleHandler.instance = new RoleHandler();

        try {
            Config.startUp();
            System.out.println("Walter launched successfully");
        } catch (ReasonedException e) {
            jda.shutdown();
        } catch (Exception e) {
            System.out.println("> ERROR An exception was thrown!" +
                    "\n" + e.toString() + "\n");
            e.printStackTrace();
            jda.shutdown();
        }
    }

    //prints the shudown code in the logger
    @Override
    public void onShutdown(ShutdownEvent event) {
        //TODO: make the logger print event.getCloseCode().getMeaning() once its implemented
    }

}
