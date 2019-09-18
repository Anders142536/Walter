package Walter;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookClientBuilder;
import net.dv8tion.jda.webhook.WebhookMessageBuilder;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ListenerOld extends ListenerAdapter {

    private CommandHandler comm;

    private JDA jda;

    //stuff that can be configured or stored via json
    private int dropzoneLimit;
    private int pullrateFortnite = 2;  //TODO add this, needs commands
    private String idLastPostFortnite;

    //other needed stuff
    private Message configOutput;  //TODO do the config stuff
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //EVENTS

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        //whenever someone gets the member role a welcome message shall be displayed in the general chat
        //this could be done at the end of the member command, but then it would not trigger
        //in case an admin assigns the role via rightclick
        for (Role role: event.getRoles()) {
            if (role.getIdLong() == IDmember) {
                User target = event.getMember().getUser();
                PrivateChannel channel = target.openPrivateChannel().complete();
                String guildname = getGuild().getName();
                channel.sendMessage("Willkommen auf " + guildname + "!" +
                        "\nDu bist nun ein vollwertiges Mitglied dieses Servers." +
                        "\nLies dir am besten " + getTextChannel(430377306520027138L).getName() + " durch damit du dich auf diesem Server ideal zurechtfindest." +
                        "\nSolltest du Hilfe benötigen kannst du jederzeit die Admins mit @ anschreiben oder !help benutzen." +
                        "\n\n*Welcome to " + guildname + "!" +
                        "\nYou are now a full member of this server." +
                        "\nIf you do not speak german feel free to use !english. You will see the english version of the Infos-category " +
                        "then and I will solely speak english with you, instead of german." +
                        "\nMake sure to read " + getTextChannel(431236543651250186L) + " to get along here easily." +
                        "\nIn case you need help you can always @-tag the admins or use !help.*").queue();
            }
            if (role.getIdLong() == IDguest) {
                User target = event.getMember().getUser();
                PrivateChannel channel = target.openPrivateChannel().complete();
                String guildname = getGuild().getName();
                channel.sendMessage("Willkommen auf " + guildname + "!" +
                        "\nDu bist nun ein Gast dieses Servers." +
                        "\nAls solcher hast du nur Zugriff auf die Voice-Channel. Wenn du vollwertiges Mitglied des Servers " +
                        "werden und Zugriff auf alle Textkanäle erhalten möchtest musst du nur irgendein Mitglied darum bitten!" +
                        "\nSolltest du Hilfe benötigen kannst du jederzeit die Admins mit @ anschreiben oder !help benutzen." +
                        "\n\n*Welcome to " + guildname + "!" +
                        "\nYou are now a guest of this server." +
                        "\nIf you do not speak german feel free to use !english. I will solely speak english with you, instead of german." +
                        "In case you want to become a full member of the server and gain access to the text channels feel free to ask any" +
                        "member to make you a member." +
                        "\nIn case you need help you can always @-tag the admins or use !help.*").queue();
            }
        }
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        //getting all the data to log
        long IDmemberJoined = event.getMember().getUser().getIdLong();
        long IDvoiceJoined = event.getChannelJoined().getIdLong();


        //logging voice activity
        try {
            File logFile = new File("/home/pi/walter/voicelogs");

            if (logFile.exists()) {
                FileWriter fw = new FileWriter(logFile, true);  //this only adds to the text file
                fw.write("1 " + IDmemberJoined + " " + IDvoiceJoined + " " + System.currentTimeMillis() + "\n");
                fw.close();
            } else {
                printError("No file called voicelogs found.");
            }
        } catch (IOException e) {
            printError(e.toString());
            e.printStackTrace();
        }

        //if the channel joined is the cinema channel
        if (IDvoiceJoined == IDcinema) {
            VoiceChannel channel = getVoiceChannel(IDvoiceJoined);
            if (channel.getMembers().size() == 1) {
                //gets the current time and truncates it to only show hours and minutes
                LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
                channel.getManager().setName("\uD83C\uDF7F Cinema (" + currentTime + ")").complete();
            }
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        //getting all the data to log
        long IDmemberMoved = event.getMember().getUser().getIdLong();
        long IDvoiceJoined= event.getChannelJoined().getIdLong();
        long IDvoiceLeft = event.getChannelLeft().getIdLong();


        //logging voice activity
        try {
            File logFile = new File("/home/pi/walter/voicelogs");

            if (logFile.exists()) {
                FileWriter fw = new FileWriter(logFile, true);  //this only adds to the text file
                fw.write("0 " + IDmemberMoved + " " + IDvoiceLeft + " " + System.currentTimeMillis() + "\n");
                fw.write("1 " + IDmemberMoved + " " + IDvoiceJoined + " " + System.currentTimeMillis() + "\n");
                fw.close();
            } else {
                printError("No file called voicelogs found.");
            }
        } catch (IOException e) {
            printError(e.toString());
            e.printStackTrace();
        }

        //if the channel joined is the cinema channel
        if (IDvoiceJoined == IDcinema) {
            VoiceChannel channel = getVoiceChannel(IDvoiceJoined);
            if (channel.getMembers().size() == 1) {
                //gets the current time and truncates it to only show hours and minutes
                LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
                channel.getManager().setName("\uD83C\uDF7F Cinema (" + currentTime + ")").complete();
            }
        }
        if (IDvoiceLeft == IDcinema) {
            VoiceChannel channel = getVoiceChannel(IDvoiceLeft);
            if (channel.getMembers().size() == 0) {
                channel.getManager().setName("\uD83C\uDF7F Cinema").complete();
            }
        }

    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        //getting all the data to log
        long IDmemberLeft = event.getMember().getUser().getIdLong();
        long IDvoiceLeft = event.getChannelLeft().getIdLong();

        try {
            File logFile = new File("/home/pi/walter/voicelogs");

            if (logFile.exists()) {
                FileWriter fw = new FileWriter(logFile, true);  //this only adds to the text file
                fw.write("0 " + IDmemberLeft + " " + IDvoiceLeft + " " + System.currentTimeMillis() + "\n");
                fw.close();
            } else {
                printError("No file called voicelogs found.");
            }
        } catch (IOException e) {
            printError(e.toString());
            e.printStackTrace();
        }

        if (IDvoiceLeft == IDcinema) {
            VoiceChannel channel = getVoiceChannel(IDvoiceLeft);
            if (channel.getMembers().size() == 0) {
                channel.getManager().setName("\uD83C\uDF7F Cinema").complete();
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //Don't respond to bots
        //Default is the type of normal text messages. Those are the only messages we want to react to
        if (event.getAuthor().isBot() || event.getMessage().getType() != MessageType.DEFAULT) return;

        String content = event.getMessage().getContentRaw();

        MessageChannel channel = event.getChannel();
        Member author;

        if (event.getGuild() == null) {
            author = getGuild().getMemberById(event.getAuthor().getIdLong());
        } else {
            author = event.getMember();
        }


        //if the content is not empty, triggers at images without text
        if (content.length() != 0) {
            //if a command
            if (content.charAt(0) == '!') {
                handleCommands(content, author, event.getMessage());
            }
            //if a help request
            else if (content.charAt(0) == '?') {
                handleHelp(content, author, channel);
            }

            //if not a command or help request
            else {
                if (channel.getIdLong() == IDdropzone) {
                    //as commands should not trigger the mentioning of the voice chat, the special behavior of the
                    //dropzone needs to be applied in this case here as well

                    //if the message is sent in the dropzone a message is sent, containing everyone as mentions who is also
                    //in the same voice channel as the author
                    mentionVoiceChat(author, channel);

                    deleteMessagesOlderThan(channel, dropzoneLimit);
                }

                //text written in the news channel will be reposted by the news-webhook
                //this way it will be emphasized not to chat in the news channel and the look and feel will be more cleaned up
                if (channel.getIdLong() == IDnews) {
                    List<Webhook> newsHooks = getTextChannel(IDnews).getWebhooks().complete();
                    if (!newsHooks.isEmpty()) {
                        for (Webhook hook: newsHooks) {
                            if (hook.getName().equals("Server News")) {
                                WebhookClientBuilder cBuilder = hook.newClient();
                                WebhookClient client = cBuilder.build();
                                WebhookMessageBuilder mBuilder = new WebhookMessageBuilder();
                                mBuilder.setContent(content + "\n*Brought to you by: " + author.getAsMention() + "*");

                                client.send(mBuilder.build());
                                client.close();

                                event.getMessage().delete().complete();
                            }
                        }
                    }
                }

                //reacting to every message that is not a command
                reactToMessage(event.getMessage());
            }
        } else if (channel.getIdLong() == IDdropzone) {
            //If someone posts a message without text (for example an image) the special behaviour is still
            //required.

            //if the message is sent in the dropzone a message is sent, containing everyone as mentions who is also
            //in the same voice channel as the author
            mentionVoiceChat(author, channel);

            deleteMessagesOlderThan(channel, dropzoneLimit);
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        //assigning the JDA
        jda = event.getJDA();

        //assigning all the messages
        configOutput = event.getJDA().getTextChannelById(403266783844237323L).getMessageById(408060522026631199L).complete();

        //assigning needed objects
        comm =      new CommandHandler();
        survey =    new Survey(getTextChannel(409738568764751902L).getMessageById(410097160022130688L).complete(), this);

        //loading the konfig file
        load();

        //starting the scheduled tasks
        //these need the things above, so it is important that these are last
        postFortniteTwitterFeed();
    }



    //FUNCTIONS AND METHODS

    //changes the value named in the second argument into the value named in the third argument
    private void config(MessageChannel channel, List<String> command, boolean hasEnglish) {
        if (channel.getIdLong() == IDconfigC) {
            if (command.size() < 3) {
                if (hasEnglish) {
                    channel.sendMessage("I'm utterly sorry but I was not given enough arguments.").queue();
                } else {
                    channel.sendMessage("Es tut mir Leid, doch mir wurden nicht genug Argumente gegeben.").queue();
                }
                printError("Not enough Arguments.");
            }

            String key = command.get(1).toLowerCase();

            try {
                switch (key) {
                    case "dropzonelimit":
                        dropzoneLimit = Integer.parseInt(command.get(2));
                        getTextChannel(IDconfigC).sendMessage("Successfully set dropzoneLimit to " + dropzoneLimit).queue(); //TODO check this

                        //TODO the other cases
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            save();
        } else {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but this command is only allowed in #konfig.").queue(); //TODO mention konfig
            } else {
                channel.sendMessage("Es tut mir Leid, doch dieser Command ist nur in #konfig erlaubt.").queue();
            }
            printError("config was not done in #konfig");
        }
    }

    private void handleCommands(String content, Member author, Message msg) {

        MessageChannel channel = msg.getChannel();

        //printing the command into the command line as a log.
        //trying to parse the command
        printCommand(author.getUser().getName(), content);
        List<String> command = parseCommand(content);

        //calling the respective function in the Command class
        if (command != null && command.get(0) != null) {
            try {

                switch (command.get(0).toLowerCase()) {
                    //guest commands
                    case "command":
                    case "commands":
                        comm.commands(channel, author, getRole(IDguest), getRole(IDmember), getRole(IDadmin), author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "deutsch":
                        comm.deutsch(channel, author, getGuild(), getRole(IDenglish));
                        break;
                    case "english":
                        comm.english(channel, author, getGuild(), getRole(IDenglish));
                        break;
                    case "griaßdi":
                    case "grüzi":
                    case "hallo":
                    case "hello":
                    case "hey":
                    case "hi":
                    case "hola":
                    case "moin":
                    case "servus":
                    case "tag":
                        comm.halloWalter(channel, author.getEffectiveName(), command, author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "help":
                    case "hilfe":
                        comm.help(channel, author, getGuild().getMembersWithRoles(getRole(IDadmin)), author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "listening":
                        comm.listening(channel, command, jda.getPresence(), author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "playing":
                        comm.playing(channel, command, jda.getPresence(), author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "roll":
                        if (command.size() == 1) {
                            comm.roll(channel, 6, author.getRoles().contains(getRole(IDenglish)));
                        } else {
                            comm.roll(channel, Integer.parseInt(command.get(1)), author.getRoles().contains(getRole(IDenglish)));
                        }
                        break;
                    case "watching":
                        comm.watching(channel, command, jda.getPresence(), author.getRoles().contains(getRole(IDenglish)));
                        break;

                    //member commands
                    case "guest":
                        if (checkRole(author, getRole(IDmember), channel)) comm.guest(channel, getRole(IDguest), getRole(IDmember), getGuild(), command, author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "member":
                        if (checkRole(author, getRole(IDmember), channel)) comm.member(channel, getRole(IDmember), getRole(IDguest), getGuild(), command, author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "pimmel":
                        if (checkRole(author, getRole(IDmember), channel)) comm.pimmel(channel, author, getRole(IDpimmel), getRole(IDguest), getGuild(), command, author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "werwolf":
                    case "werewolf":
                        //TODO implement this
                        channel.sendMessage("Es tut mir Leid, doch dieser Command tut bisher noch nichts.").queue();
                        break;

                    //admin commands
                    case "analyze":
                        if (checkRole(author, getRole(IDadmin), channel)) comm.analyze(channel, author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "editmsg":
                        if (checkRole(author, getRole(IDadmin), channel)) comm.editMsg(channel, command, content, author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "file":
                        if (checkRole(author, getRole(IDadmin), channel)) comm.file(channel, command, author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "getmsg":
                        if (checkRole(author, getRole(IDadmin), channel)) comm.getMsg(channel, command, author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "konfig":
                    case "config":
                        if (checkRole(author, getRole(IDadmin), channel)) comm.config(channel, command, author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "patch":
                        if (checkRole(author, getRole(IDadmin), channel)) comm.patch(msg, getTextChannel(IDnews), author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "reprint":
                        if (checkRole(author, getRole(IDadmin), channel)) comm.reprint(channel, getGuild(), command, author.getRoles().contains(getRole(IDenglish)));
                        break;
                    case "say":
                        if (checkRole(author, getRole(IDadmin), channel)) comm.say(msg, author.getRoles().contains(getRole(IDenglish)));
                        break;

                    //owner commands
                    case "shutdown":
                        if (checkOwner(author, channel)) comm.shutdown(jda, survey);
                        break;
                    case "test":
                        if (author.isOwner()) {

                        } else {
                            if (author.getRoles().contains(getRole(IDenglish))) {
                                channel.sendMessage(getGuild().getOwner().getEffectiveName() + " has not written any tests for members right now.").queue();
                            } else {
                                channel.sendMessage(getGuild().getOwner().getEffectiveName() + " hat derzeit keine Tests für Member geschrieben.").queue();
                            }
                        }
                        break;
                    default:
                        if(author.getRoles().contains(getRole(IDenglish))) {
                            channel.sendMessage("I'm utterly sorry but I did not understand your command.").queue();
                        } else {
                            channel.sendMessage("Es tut mir leid, ich habe deine Anweisung nicht erkannt.").queue();
                        }
                        printError("Command not recognized.");
                }
            } catch (Exception e) {
                if (author.getRoles().contains(getRole(IDenglish))) {
                    channel.sendMessage("I'm utterly sorry but something did not function properly. Here is the mistake:\n" + e).queue();
                } else {
                    channel.sendMessage("Es tut mir Leid, etwas hat nicht funktioniert. Hier der Fehler:\n" + e).queue();
                }
                printError(e.toString());
            }
        } else {
            if (author.getRoles().contains(getRole(IDenglish))) {
                channel.sendMessage("I'm utterly sorry but you haven't given me a command.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, du hast mir keine Anweisung gegeben.").queue();
            }
            printError("No Argument was given after the command prefix.");
        }
    }

    private void handleHelp(String content, Member author, MessageChannel channel) {

        //printing the command into the command line as a log.
        //trying to parse the command
        printCommand(author.getUser().getName(), content);
        List<String> command = parseCommand(content);

        //calling the respective function in the Command class
        if (command != null && command.get(0) != null) {
            try {
                String keywords = "\n\nSchlüsselworte für diesen Command sind:\n```";
                String keywordsENG = "\n\nKeywords for this command are:\n```";
                String caution = "```\nAchtung:\nDie Groß- und Kleinschreibung ist irrelevant.";
                String cautionENG = "```\nCaution:\nThese are not case-sensitive.";
                String quotes = "\nBedenke, dass du Werte, die Leerzeichen beinhalten, unter Anführungszeichen setzen musst." +
                        "\nZum Beispiel: \"Dragoth the Dark\"";
                String quotesENG = "Keep in mind that you need to put values between quotes if they contain white spaces." +
                        "\nFor example: \"Dragoth the Dark\"";

                switch (command.get(0).toLowerCase()) {
                    //guest commands
                    case "command":
                    case "commands":
                        if (author.getRoles().contains(getRole(IDenglish))) {
                            channel.sendMessage("```!commands ```" +
                                    "\nThis command lists all the commands available to you." + keywordsENG +
                                    "!command" +
                                    "\n!commands" + cautionENG).queue();
                        } else {
                            channel.sendMessage("```!commands ```" +
                                    "\nDieser Command listet alle dir zur Verfügung stehenden Commands." + keywords +
                                    "!command" +
                                    "\n!commands" + caution).queue();
                        }
                        break;
                    case "griaßdi":
                    case "grüzi":
                    case "hallo":
                    case "hello":
                    case "hey":
                    case "hi":
                    case "hola":
                    case "moin":
                    case "servus":
                    case "tag":
                        if (author.getRoles().contains(getRole(IDenglish))) {
                            channel.sendMessage("```!hello walter ```" +
                                    "\nYou greet Walter politely, with him greeting you back." + keywordsENG +
                                    "!hello" +
                                    "\n!hallo" +
                                    "\n!hi" +
                                    "\n!hey" +
                                    "\n!hola" +
                                    "\n!moin" +
                                    "\n!servus" +
                                    "\n!griaßdi" +
                                    "\n!grüzi" +
                                    "\n!tag" + cautionENG).queue();
                        } else {
                            channel.sendMessage("```!hallo walter ```" +
                                    "\nHiermit grüßt du Walter höflich und er grüßt zurück." + keywords +
                                    "!hallo" +
                                    "\n!hello" +
                                    "\n!hi" +
                                    "\n!hey" +
                                    "\n!hola" +
                                    "\n!moin" +
                                    "\n!servus" +
                                    "\n!griaßdi" +
                                    "\n!grüzi" +
                                    "\n!tag" + caution).queue();
                        }
                        break;
                    case "help":
                        if (author.getRoles().contains(getRole(IDenglish))) {
                            channel.sendMessage("```!help ```" +
                                    "\nThis command displays the general help page with a short explanation where you" +
                                    " can find information." + keywordsENG +
                                    "!help" +
                                    "\n!hilfe" + cautionENG).queue();
                        } else {
                            channel.sendMessage("```!hilfe ```" +
                                    "\nDieser Command zeigt dir die allgemeine Hilfe-Seite an mit einer kurzen" +
                                    " Erklärung wo du Informationen findest." + keywords +
                                    "!hilfe" +
                                    "\n!help" + caution).queue();
                        }
                        break;
                    case "listening":
                        if (author.getRoles().contains(getRole(IDenglish))) {
                            channel.sendMessage("```!listening TEXT ```" +
                                    "\nThis command changes what walter is listening to to **TEXT**." + keywordsENG +
                                    "!listening" + cautionENG + quotesENG).queue();
                        } else {
                            channel.sendMessage("```!listening TEXT ```" +
                                    "\nDieser Command ändert was Walter gerade hört zu **TEXT**." + keywords +
                                    "!listening" + caution + quotes).queue();
                        }
                        break;
                    case "playing":
                        if (author.getRoles().contains(getRole(IDenglish))) {
                            channel.sendMessage("```!playing TEXT ```" +
                                    "\nThis command changes the game walter is playing to **TEXT**." + keywordsENG +
                                    "!playing" + cautionENG + quotesENG).queue();
                        } else {
                            channel.sendMessage("```!playing TEXT ```" +
                                    "\nDieser Command ändert das Spiel, das Walter gerade spielt, zu **TEXT**." + keywords +
                                    "!playing" + caution + quotes).queue();
                        }
                        break;
                    case "roll":
                        if (author.getRoles().contains(getRole(IDenglish))) {
                            channel.sendMessage("```!roll NUMBER ```" +
                                    "\nI give you a random number between 1 and **NUMBER**. If no **NUMBER** is given" +
                                    " I give you a random number between 1 and 6." + keywordsENG +
                                    "!roll" + cautionENG).queue();
                        } else {
                            channel.sendMessage("```!roll NUMMER ```" +
                                    "\nIch gebe dir eine Zufallszahl zwischen 1 und **NUMMER**. Wenn keine **NUMMER** angegeben " +
                                    "ist gebe ich dir eine Zufallszahl zwischen 1 und 6." + keywords +
                                    "!roll" + caution).queue();
                        }
                        break;
                    case "watching":
                        if (author.getRoles().contains(getRole(IDenglish))) {
                            channel.sendMessage("```!watching TEXT ```" +
                                    "\nThis command changes what walter is watching to **TEXT**." + keywordsENG +
                                    "!watching" + cautionENG + quotesENG).queue();
                        } else {
                            channel.sendMessage("```!watching TEXT ```" +
                                    "\nDieser Command ändert was Walter gerade schaut zu **TEXT**." + keywords +
                                    "!watching" + caution + quotes).queue();
                        }
                        break;

                    //member commands
                    case "deutsch":
                        if (checkRole(author, getRole(IDmember), channel)) {
                            if (author.getRoles().contains(getRole(IDenglish))) {
                                channel.sendMessage("```!deutsch ```" +
                                        "\nThis command revokes your role *English*, switching the Info-Category and" +
                                        " the language I speak to you in to german. This is the opposite of !english." + keywordsENG +
                                        "!deutsch" + cautionENG).queue();
                            } else {
                                channel.sendMessage("```!deutsch ```" +
                                        "\nDieser Command nimmt dir die Rolle *English*. Dadurch wird die Info-Kategorie " +
                                        "auf Deutsch angezeigt und ich spreche Deutsch mit dir. Dies ist das Gegenstück" +
                                        " zu !english." + keywords +
                                        "!deutsch" + caution).queue();
                            }
                        }
                        break;
                    case "english":
                        if (checkRole(author, getRole(IDmember), channel)) {
                            if (author.getRoles().contains(getRole(IDenglish))) {
                                channel.sendMessage("```!english ```" +
                                        "\nThis command gives you the role *English*, switching the Info-Category and" +
                                        " the language I speak to you in to english. This is the opposite of !deutsch" + keywordsENG +
                                        "!english" + cautionENG).queue();
                            } else {
                                channel.sendMessage("```!english ```" +
                                        "\nDieser Command gibt dir die Rolle *English*. Dadurch wird die Info-Kategorie " +
                                        "auf Englisch angezeigt und ich spreche Englisch mit dir. Dies ist das Gegenstück " +
                                        "zu !deutsch." + keywords +
                                        "!english" + caution).queue();
                            }
                        }
                        break;
                    case "guest":
                        if (checkRole(author, getRole(IDmember), channel)) {
                            if (author.getRoles().contains(getRole(IDenglish))) {
                                channel.sendMessage("```!guest NAME ```" +
                                        "\nThis command assigns the role *guest* to the User with the name **NAME**." + keywordsENG +
                                        "!guest" + cautionENG + quotesENG).queue();
                            } else {
                                channel.sendMessage("```!guest NAME ```" +
                                        "\nDieser Command gibt dem User mit dem Namen **NAME** die *Guest*-Rolle." + keywords +
                                        "!guest" + caution + quotes).queue();
                            }
                        }
                        break;
                    case "member":
                        if (checkRole(author, getRole(IDmember), channel)) {
                            if (author.getRoles().contains(getRole(IDenglish))) {
                                channel.sendMessage("```!member NAME ```" +
                                        "\nThis command assigns the role *member* to the User with the name **NAME**. " +
                                        "This also revokes the *guest* role if the targeted User had that." + keywordsENG +
                                        "!member" + cautionENG + quotesENG).queue();
                            } else {
                                channel.sendMessage("```!member NAME ```" +
                                        "\nDieser Command gibt dem User mit dem Namen **NAME** die *Member*-Rolle. " +
                                        "Dies nimmt außerdem die *Guest*-Rolle falls der Ziel-User diese hat." + keywords +
                                        "!member" + caution + quotes).queue();
                            }
                        }
                        break;
                    case "pimmel":
                        if (checkRole(author, getRole(IDmember), channel)) {
                            if (author.getRoles().contains(getRole(IDenglish))) {
                                channel.sendMessage("```!pimmel NAME ```" +
                                        "\nThis command assigns the role *Pimmel* to the User with the name **NAME**." +
                                        " If the targeted user already has the *Pimmel*-role it will be revoked. " +
                                        "\"Pimmel\" means \"dick\" in german." + keywordsENG +
                                        "!pimmel" + cautionENG + quotesENG).queue();
                            } else {
                                channel.sendMessage("```!pimmel NAME ```" +
                                        "\nDieser Command gibt dem User mit dem Namen **NAME** die *Pimmel*-Rolle. " +
                                        "Wenn der Ziel-User die *Pimmel*-Rolle bereits hat wird sie ihm genommen." + keywords +
                                        "!pimmel" + caution + quotes).queue();
                            }
                        }
                        break;
                    case "werwolf":
                    case "werewolf":
                        //TODO implement this
                        channel.sendMessage("Es tut mir Leid, doch dieser Command tut bisher noch nichts.").queue();
                        break;

                    //admin commands
                    case "editmsg":
                        if (checkRole(author, getRole(IDadmin), channel)) {
                            if (author.getRoles().contains(getRole(IDenglish))) {
                                channel.sendMessage("```!editmsg ID TEXT ```" +
                                        "\nThis command changes the content of the message with the ID **ID** to **TEXT**. " +
                                        "To access the IDs of Messages you need to enable the developer settings under " +
                                        "User Settings > Appearance > Developer Mode. Then you can right click any message " +
                                        "to access its ID." + keywordsENG +
                                        "!editmsg" + cautionENG).queue();
                            } else {
                                channel.sendMessage("```!editmsg ID TEXT ```" +
                                        "\nDieser Command ändert den Inhalt der Nachricht mit der ID **ID** zu **TEXT**. " +
                                        "Um die ID von Nachrichten auslesen zu können musst du die Developer Settings " +
                                        "aktivieren unter User Settings > Appearance > Developer Mode. Dann kannst du " +
                                        "mit einem Rechtsklick die ID von Nachrichten auslesen." + keywords +
                                        "!editmsg" + caution).queue();
                            }
                        }
                        break;
                    case "file":
                        if (checkRole(author, getRole(IDadmin), channel)) {
                            if (author.getRoles().contains(getRole(IDenglish))) {
                                channel.sendMessage("```!file FILENAME ```" +
                                        "\nI post the file with the name FILENAME into the chat. You must include file " +
                                        "extensions." + keywordsENG +
                                        "!file" + cautionENG + quotesENG).queue();
                            } else {
                                channel.sendMessage("```!file DATEINAME ```" +
                                        "\nIch poste die Datei mit dem Namen **DATEINAME** in den Chat. Du musst die " +
                                        "Dateierweiterung angeben." + keywords +
                                        "!file" + caution + quotes).queue();
                            }
                        }
                        break;
                    case "getmsg":
                        if (checkRole(author, getRole(IDadmin), channel)) {
                            if (author.getRoles().contains(getRole(IDenglish))) {
                                channel.sendMessage("```!getmsg ID ```" +
                                        "\nI print the message with the ID ID both into the chat and the console. " +
                                        "To access the IDs of Messages you need to enable the developer settings under " +
                                        "User Settings > Appearance > Developer Mode. Then you can right click any message " +
                                        "to access its ID." + keywordsENG +
                                        "!getmsg" + cautionENG).queue();
                            } else {
                                channel.sendMessage("```!getmsg ID ```" +
                                        "\nIch werfe die Nachricht mit der ID **ID** in den Chat und die Konsole aus. " +
                                        "Um die ID von Nachrichten auslesen zu können musst du die Developer Settings " +
                                        "aktivieren unter User Settings > Appearance > Developer Mode. Dann kannst du " +
                                        "mit einem Rechtsklick die ID von Nachrichten auslesen." + keywords +
                                        "!getmsg" + caution).queue();
                            }
                        }
                        break;
                    case "konfig":
                    case "config":
                        if (checkRole(author, getRole(IDadmin), channel)) {
                            if (author.getRoles().contains(getRole(IDenglish))) {
                                channel.sendMessage("```!config KEY VALUE ```" +
                                        "\nThis command changes the variable with the name **KEY** to the value **VALUE**. " +
                                        "Those are displayed in <#403266783844237323>." + keywordsENG +
                                        "!config" +
                                        "\n!konfig" + cautionENG + quotesENG).queue();
                            } else {
                                channel.sendMessage("```!konfig SCHLÜSSEL WERT ```" +
                                        "\nDieser Command ändert den Wert der Variable **SCHLÜSSEL** zum Wert **WERT**. Diese sind " +
                                        "in <#403266783844237323> aufgelistet." + keywords +
                                        "!konfig" +
                                        "\n!config" + caution).queue();
                            }
                        }
                        break;
                    case "patch":
                        if (checkRole(author, getRole(IDadmin), channel)) {
                            channel.sendMessage("```!patch TEXT```" +
                                    "\nWrites the text TEXT into <#405462316470108160> using the Patchnotes Webhook. " +
                                    "There are no quotes needed." + keywordsENG +
                                    "!patch" + cautionENG).queue();
                        } else {
                            channel.sendMessage("```!patch TEXT```" +
                                    "\nSchreibt den Text TEXT in <#405462316470108160> mit Hilfe des Patchnotes-Webhook. " +
                                    "Es werden keine Anführungszeichen benötigt." + keywords +
                                    "!patch" + caution).queue();
                        }
                        break;
                    case "reprint":
                        if (checkRole(author, getRole(IDadmin), channel)) {
                            if (author.getRoles().contains(getRole(IDenglish))) {
                                channel.sendMessage("```!reprint ID```" +
                                        "\nClears and reprints the channel with the ID **ID** if there is a file " +
                                        "called **ID**.channel on the server. To access the IDs of channels you need " +
                                        "to enable the developer settings under User Settings > Appearance > " +
                                        "Developer Mode. Then you can right click any channel to access its ID." + keywordsENG +
                                        "!reprint" + cautionENG).queue();
                            } else {
                                channel.sendMessage("```!reprint ID``` " +
                                        "\nLeert und füllt den Kanal mit der ID **ID** wenn es eine Datei namens **ID**.channel" +
                                        " auf dem Server gibt. Um die ID von Kanälen auslesen zu können musst du die" +
                                        " Developer Settings aktivieren unter User Settings > Appearance > Developer Mode." +
                                        " Dann kannst du mit einem Rechtsklick die ID von Nachrichten auslesen." + keywords +
                                        "!reprint" + caution).queue();
                            }
                        }
                        break;
                    case "say":
                        if (checkRole(author, getRole(IDadmin), channel)) {
                            if (author.getRoles().contains(getRole(IDenglish))) {
                                channel.sendMessage("```!say TEXT ```" +
                                        "\nI post the message **TEXT** into the Chat." + keywordsENG +
                                        "!say" + cautionENG).queue();
                            } else {
                                channel.sendMessage("```!say TEXT ```" +
                                        "\nIch poste die Nachricht **TEXT** in den Chat." + keywords +
                                        "!say" + caution).queue();
                            }
                        }
                        break;
                    default:
                        if(author.getRoles().contains(getRole(IDenglish))) {
                            channel.sendMessage("I'm utterly sorry but I did not understand your command.").queue();
                        } else {
                            channel.sendMessage("Es tut mir leid, ich habe deine Anweisung nicht erkannt.").queue();
                        }
                        printError("Command not recognized.");
                }
            } catch (Exception e) {
                if (author.getRoles().contains(getRole(IDenglish))) {
                    channel.sendMessage("I'm utterly sorry but something did not function properly. Here is the mistake:\n" + e).queue();
                } else {
                    channel.sendMessage("Es tut mir Leid, etwas hat nicht funktioniert. Hier der Fehler:\n" + e).queue();
                }
                printError(e.toString());
            }
        } else {
            if (author.getRoles().contains(getRole(IDenglish))) {
                channel.sendMessage("I'm utterly sorry but you haven't given me a command.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, du hast mir keine Anweisung gegeben.").queue();
            }
            printError("No Argument was given after the command prefix.");
        }
    }




    private void mentionVoiceChat(Member author, MessageChannel channel) {
        String mentions = "";

        //get a list of all the members that are in the same voice channel as the author of the message
        if (author.getVoiceState().getChannel() != null) {
            List<Member> vmembers = author.getVoiceState().getChannel().getMembers();
            for (Member temp : vmembers) {
                //everyone but the author of the message will be added as a mention
                if (author != temp) mentions += temp.getAsMention() + " ";
            }
        }

        if (!mentions.equals("")) channel.sendMessage(mentions).queue();
    }

    private List<String> readMsg(Message msg) {
        List<String> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new StringReader(msg.getContentStripped()))) {
            String temp = reader.readLine();
            while (temp != null) {
                result.add(temp);
                temp = reader.readLine();
            }
        } catch (IOException exc) {
            // quit
        }
        return result;
    }

    //pulls the last post from the twitter feed
    private JSONObject pullFortniteTwitterPostJSON () {
        JSONArray tweetList = null;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.twitter.com/1.1/statuses/user_timeline.json?id=425871040&include_rts=false&exclude_replies=true&tweet_mode=extended&count=5")
                .get()
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .addHeader("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAALoZ5gAAAAAAL9iUTozO5slRhxOV5rtCisY%2BgT4%3D4NlLSiXUMv1FBpxZ9CDlGSTQLQX3yE9q7oMpZEVtANNV39n7uY")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "2732c66e-233e-4f60-a6a2-bf5ce5d15844")
                .build();

        try {
            Response response = client.newCall(request).execute();
            tweetList = new JSONArray(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tweetList != null) return (JSONObject)tweetList.get(0);

        return null;
    }

    //schedules the fortnite twitter posting
    private void scheduleFortniteTwitterFeed() {

        final ScheduledFuture<?> postFortniteTwitterFeed = scheduler.schedule(this::postFortniteTwitterFeed, pullrateFortnite, TimeUnit.MINUTES);
    }

    private void postFortniteTwitterFeed() {
        JSONObject tweet = pullFortniteTwitterPostJSON();

        if (tweet != null && !tweet.getString("id_str").equals(idLastPostFortnite)) {
            //gather filtered information
            String content = filterLinks(tweet.getString("full_text")) + checkUrlTwitter(tweet);
            String videolink = checkVideoTwitter(tweet);
            byte[] image = checkImageTwitter(tweet);

            if (!videolink.equals("")
                    || content.toLowerCase().contains("issue")
                    || content.toLowerCase().contains("patch")
                    || content.toLowerCase().contains("investigat")
                    || content.toLowerCase().contains("maintenance")
                    || content.toLowerCase().contains("update")
                    || content.toLowerCase().contains("matchmaking")
                    || content.toLowerCase().contains("releas")
                    || content.toLowerCase().contains("download")
                    || content.toLowerCase().contains("server")
                    || content.toLowerCase().contains("announc")
                    || content.toLowerCase().contains("downtime")) {

                if (!videolink.equals("")) {
                    content += "\n\n" + videolink;
                }

                List<Webhook> fortniteHooks = getTextChannel(IDfortniteC).getWebhooks().complete();
                if (!fortniteHooks.isEmpty()) {
                    WebhookClientBuilder cBuilder = fortniteHooks.get(0).newClient();
                    WebhookClient client = cBuilder.build();
                    WebhookMessageBuilder mBuilder = new WebhookMessageBuilder();
                    mBuilder.setContent(content);

                    if (image != null) {
                        mBuilder.setFile(image, "tweetimage.gif");
                    }

                    client.send(mBuilder.build());
                    client.close();
                }
            }
            idLastPostFortnite = tweet.getString("id_str");
            save();
        }
        scheduleFortniteTwitterFeed();
    }

    static void postFileToChannel(MessageChannel channel, String filename, boolean hasEnglish, boolean complete) {
        try {
            //getting the file by adding the name given in the command to the path of the image folder
            File toPost = new File("/home/pi/walter/" + filename);

            //handling the case that the given file name does not lead to an existing file
            if (!toPost.exists()) {
                if (hasEnglish) {
                    channel.sendMessage("I'm utterly sorry but I have not found a file named " + filename + ".").queue();
                } else {
                    channel.sendMessage("Es tut mir leid, ich habe keine Datei mit den Namen " + filename + " gefunden.").queue();
                }
                Walter.ListenerOld.printError("No file found. Given name: " + filename);
                return;
            }

            if (complete) {
                //posting the file to chat
                channel.sendFile(toPost).complete();
            } else {
                channel.sendFile(toPost).queue();
            }
        } catch (Exception e) {
            Walter.ListenerOld.printError(e.toString());
            e.printStackTrace();
        }
    }

    private String filterLinks(String content) {
        String[] fragments = content.split(" ");
        StringBuilder result = new StringBuilder();

        for (String fragment:
             fragments) {
            if (!fragment.toLowerCase().contains("http")) {
                result.append(fragment + " ");
            }
        }
        return result.toString();
    }

    private String checkUrlTwitter(JSONObject tweet) {
        if (tweet.has("entities")) {
            JSONArray urls = tweet.getJSONObject("entities").getJSONArray("urls");
            if (urls.length() > 0) {
                String url = ((JSONObject) urls.get(0)).getString("url");
                return url;
            }
        }
        return "";
    }

    private byte[] checkImageTwitter(JSONObject tweet) {
        if (tweet.has("extended_entities")) {
            JSONObject firstMedia = (JSONObject)tweet.getJSONObject("extended_entities")
                    .getJSONArray("media")
                    .get(0);
            //if the json states that there is actually a photo in the post
            if (firstMedia.getString("type").equals("photo")) {
                String imageURL = firstMedia.getString("media_url");
                //get that image
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(imageURL)
                        .get()
                        .addHeader("Cache-Control", "no-cache")
                        .addHeader("Postman-Token", "e61f77d4-84be-4909-a3e6-31b201f8d79b")
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    return response.body().bytes();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String checkVideoTwitter(JSONObject tweet) {
        if (tweet.has("extended_entities")) {
            JSONObject firstMedia = (JSONObject)tweet.getJSONObject("extended_entities")
                    .getJSONArray("media")
                    .get(0);
            if (firstMedia.getString("type").equals("video")) {
                JSONArray videoVariants = firstMedia.getJSONObject("video_info").getJSONArray("variants");
                String largestVideoURL = "";
                int bitrate = 0;
                for (Object variant :
                        videoVariants) {
                    JSONObject temp = (JSONObject) variant;
                    if (temp.has("bitrate")) {
                        if (temp.getInt("bitrate") > bitrate) {
                            bitrate = temp.getInt("bitrate");
                            largestVideoURL = temp.getString("url");
                        }
                    }
                }
                return "\n\n" + largestVideoURL;
            }
        }
        return "";
    }

    //if no value is given for how many messages are to be catched, catch one
    private void deleteMessagesOlderThan(MessageChannel channel, int limit) {
        deleteMessagesOlderThan(channel, limit, 4);
    }


    private void load() {
        printInfo("Loading...");
        String jsonString = "";

        try {
            BufferedReader file = new BufferedReader(new FileReader("/home/pi/walter/walter.json"));
            //TODO add check if file exists and handle the case when it does not
            jsonString = file.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!jsonString.equals("")) {
            JSONObject data = new JSONObject(jsonString);

            dropzoneLimit = data.getInt("dropzoneLimit");
            pullrateFortnite = data.getInt("pullrateFortnite");
            idLastPostFortnite = data.getString("idLastPostFortnite");
        } else {
            printError("There was nothing given by the bufferedreader.");
        }
    }

    void save() {
        printInfo("Saving...");
        JSONObject save = new JSONObject();

        save.put("dropzoneLimit", dropzoneLimit);
        save.put("pullrateFortnite", pullrateFortnite);
        save.put("idLastPostFortnite", idLastPostFortnite);

        try (FileWriter file = new FileWriter("/home/pi/walter/walter.json")){
            save.write(file);
            System.out.println("save to string: " + save.toString() +
            "\nkeyset size: " + save.keySet().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //reacts to messages with some emojis if certain words are mentioned
    //was added as a play thing to prepare the reaction handling of the survey
    private void reactToMessage(Message msg) {
        String content = msg.getContentRaw().toLowerCase();
        if (content.contains("tea") || content.toLowerCase().contains("tee")) msg.addReaction("\uD83C\uDF75").queue();
        if (content.contains("coffee") || content.toLowerCase().contains("kaffee")) msg.addReaction("\u2615").queue();
    }

    //helper functions for easier printing
    static void printInfo(String toPrint) {
        System.out.println("Info    > " + toPrint);
    }

    static void printError(String toPrint) {
        System.out.println("Error   > " + toPrint);
    }

    private static void printCommand(String name, String toPrint) {
        System.out.println("Command > " + name + ": " + toPrint);
    }

}

/*
List of Role-IDs:

Info   > Bot: 391285765893783554
Info   > Pimmel: 305413276181987329
Info   > Overlord: 391257227975196673
Info   > Admin: 254264388209475584
Info   > Member: 254264987294498817
Info   > Netflix: 397462377521610762
Info   > Guest: 401876564154777620
Info   > Game Master: 423570509154877441
Info   > Newsbringer: 430366734617280542
Info   > English: 431236126443831308
Info   > Werwolf: 431444524925976579
Info   > @everyone: 254263827237961729
 */
