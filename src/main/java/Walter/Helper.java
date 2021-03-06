package Walter;

import Walter.Settings.EventSetting;
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
import net.dv8tion.jda.api.managers.AccountManager;
import net.dv8tion.jda.api.managers.GuildManager;
import net.dv8tion.jda.api.managers.Presence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

//bundles certain functionalities that are needed all over the place
public class Helper {
    private static JDA jda;

    public static void setJda(JDA jdaToSet) { jda = jdaToSet; }

    final private static long GUILD_ID = 254263827237961729L;

    /* ************* *
     *  JDA Getters  *
     * ************* */

    //this is not stored as instances are invalidated after a certain period of time
    static Guild getGuild() {
        return jda.getGuildById(GUILD_ID);
    }

    public static GuildManager getGuildManager() {
        return getGuild().getManager();
    }

    public static AccountManager getWalterAccountManager() {
        return jda.getSelfUser().getManager();
    }

    static net.dv8tion.jda.api.entities.Category getCategory(BlackCategory blackCategory) {
        return getCategory(blackCategory.ID);
    }

    static net.dv8tion.jda.api.entities.Category getCategory(long categoryID) {
        return getGuild().getCategoryById(categoryID);
    }

    public static TextChannel getTextChannel(BlackChannel blackChannel) {
        return getTextChannel(blackChannel.ID);
    }

    public static TextChannel getTextChannel(long channelID) {
        return getGuild().getTextChannelById(channelID);
    }

    static VoiceChannel getVoiceChannel(BlackChannel blackChannel) {
        return getVoiceChannel(blackChannel.ID);
    }

    static VoiceChannel getVoiceChannel(long channelID) {
        return getGuild().getVoiceChannelById(channelID);
    }

    public static Member getMember(User user) {
        return getGuild().retrieveMember(user).complete();
    }

    public static List<Member> getMembersByName(String name) {
        return getGuild().getMembersByEffectiveName(name, true);
    }


    /* ******************* *
     *  Other Helpmethods  *
     * ******************* */

    public static void logCommand(Member author, MessageChannel channel, String commandMessageRaw) {
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

    public static void logError(String logMessage) {
        System.out.println("ERROR :   " + logMessage);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Error");
        builder.setColor(13722624);
        builder.setDescription("<@!151010441043116032>\n" + logMessage);
        builder.setFooter("Walter v" + Walter.VERSION);
        getTextChannel(BlackChannel.LOG).sendMessage(builder.build()).queue();
    }

    public static void logException(String logMessage) {
        System.out.println("AN UNHANDLED EXCEPTION OCCURED\n" + logMessage);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Exception");
        builder.setColor(13697024);
        builder.setDescription("<@!151010441043116032>\n" + logMessage);
        builder.setFooter("Walter v" + Walter.VERSION);
        getTextChannel(BlackChannel.LOG).sendMessage(builder.build()).queue();
    }

    public static void logInfo(String logMessage) {
        System.out.println("INFO:    " + logMessage);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Info");
        builder.setColor(7854123);
        builder.setDescription(logMessage);
        builder.setFooter("Walter v" + Walter.VERSION);
        getTextChannel(BlackChannel.LOG).sendMessage(builder.build()).queue();
    }

    public static void respond(Member member, MessageChannel channel, String german, String english) {
        if (RoleHandler.hasRole(member, BlackRole.ENGLISH))
            channel.sendMessage(english).queue();
        else
            channel.sendMessage(german).queue();
    }

    public static void respond(Member member, MessageChannel channel, String[] text) {
        Language lang = Language.getLanguage(member);
        if (text.length <= lang.index) respond(channel, text[0]);
        else respond(channel, text[lang.index]);
    }

    public static void respond(User user, MessageChannel channel, String[] text) {
        Language lang = Language.getLanguage(user);
        if (text.length <= lang.index) respond(channel, text[0]);
        else respond(channel, text[lang.index]);
    }

    //all responds shall end in this one
    public static void respond(MessageChannel channel, String text) {
        //TODO: check if message is over 2k length and split if necessary
        channel.sendMessage(text).queue();
    }

    public static void respondError(MessageReceivedEvent event, CommandExecutionException e) {
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
        Helper.logError(errorlogMessage);
    }

    public static void respondException(MessageReceivedEvent event, Exception e) {
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

    static void deleteUnpinnedMessagesOlderThan(MessageChannel channel, int limit) {

        //getting the message history in the given channel
        //unfortunately retrievePast() does not terminate .complete() if more messages are asked for than there are,
        //therefore the forced return after 1 seconds.
        List<Message> pinned = channel.retrievePinnedMessages().completeAfter(1, TimeUnit.SECONDS);  //TODO see if the wait on time basis can be avoided
        List <Message> history = getChannelHistory(channel);

        //if the number of messages in a channel has surpassed the allowed limit
        if (history.size() > limit) {
            history.subList(limit, history.size()).forEach((x) -> {
                if (!x.isPinned()) x.delete().queue();
            });
        }
    }

    public static void clearChannelOfMessages(MessageChannel channel) {
        getChannelHistory(channel).forEach((msg) -> msg.delete().complete());
    }

    public static List<Message> getChannelHistory(MessageChannel channel) {
        MessageHistory history = channel.getHistory();
        int lastSize;

        do {
            lastSize = history.size();
            history.retrievePast(100).completeAfter(1, TimeUnit.SECONDS);
        } while (history.size() != lastSize);

        return history.getRetrievedHistory();
    }

    public static String getFormattedNowString() {
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

    private static String getStackTraceString(Exception e) {
        StringBuilder builder = new StringBuilder("\nSTACKTRACE:\n`" +
                e.getClass().getSimpleName() + "`");
        StackTraceElement[] stacktrace = e.getStackTrace();
        for (int i = 0; i < 10 && i < stacktrace.length; i++) {
            builder.append("\n").append(stacktrace[i]);
        }
        return builder.toString();
    }

    public static void sortListByDate(List<EventSetting> list) {
        list.sort((x, y) -> {
            LocalDateTime xTime = x.getStartDateValue();
            LocalDateTime yTime = y.getStartDateValue();

            //if none have a start date defined
            if (xTime == null && yTime == null) {
                return x.getName().compareToIgnoreCase(y.getName());
            }

            //the one having no start date defined comes first
            if (xTime == null) return -1;
            if (yTime == null) return 1;

            //if both have a start date defined
            if (x.getStartDateValue().equals(y.getStartDateValue())) return 0;
            return (x.getStartDateValue().isBefore(y.getStartDateValue()) ? -1 : 1);
        });
    }
}
