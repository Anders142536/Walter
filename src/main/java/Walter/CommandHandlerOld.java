package Walter;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.Presence;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookClientBuilder;
import net.dv8tion.jda.webhook.WebhookMessageBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

class CommandHandlerOld {

    //This class is a collection of functions that represent the commands Walter can do.
    //Only exception are the commands that fiddle with walters settings directly, as those direclty use methods
    //and variables written in ListenerOld.java

    //guest commands

    //prints a list of the commands usable by the author into the chat
    void commands(MessageChannel channel, Member author, Role guest,  Role member, Role admin, boolean hasEnglish){
        if (hasEnglish) {
            String guestcommands = "These are the commands available to you:" +
                    "\n" + guest.getName() + "-Commands:" +
                    "\n```!commands" +
                    "\n!deutsch" +
                    "\n!english" +
                    "\n!hello walter" +
                    "\n!help" +
                    "\n!playing TEXT" +
                    "\n!roll NUMBER" +
                    "\n!test```";
            String membercommands = member.getName() + "-Commands:" +
                    "\n```!guest NAME" +
                    "\n!member NAME" +
                    "\n!pimmel NAME" +
                    "\n!werewolf```";
            String admincommands = admin.getName() + "-Commands:" +
                    "\n```!editmsg ID TEXT" +
                    "\n!getmsg ID" +
                    "\n!file NAME" +
                    "\n!config KEY VALUE" +
                    "\n!reprint" +
                    "\n!say TEXT```";
            String alternatives = "Please keep in mind that there are a few alternative commands that have the same effect." +
                    " Here you only have one version due to spacing. To see the full list and deeper explanation see " +
                    "<#435061241942245376> or replace the ! with ?. This will display a help page for the given command.";
            if (author.getRoles().contains(member)) guestcommands += membercommands;
            if (author.getRoles().contains(admin)) guestcommands += admincommands;
            channel.sendMessage(guestcommands + alternatives).queue();
        } else {
            String guestcommands = "Dies sind die Commands die dir zur Verfügung stehen:" +
                    "\n" + guest.getName() + "-Commands:" +
                    "\n```!commands" +
                    "\n!deutsch" +
                    "\n!english" +
                    "\n!hallo walter" +
                    "\n!hilfe" +
                    "\n!playing TEXT" +
                    "\n!roll NUMMER" +
                    "\n!test```";
            String membercommands = member.getName() + "-Commands:" +
                    "\n```!guest NAME" +
                    "\n!member NAME" +
                    "\n!pimmel NAME" +
                    "\n!werwolf```";
            String admincommands = admin.getName() + "-Commands" +
                    "\n```!editmsg ID TEXT" +
                    "\n!getmsg ID" +
                    "\n!file NAME" +
                    "\n!konfig KEY VALUE" +
                    "\n!patch TEXT" +
                    "\n!reprint" +
                    "\n!say TEXT```";
            String alternatives = "Bitte bedenkt, dass es ein paar Alternativen zu diesen Commands gibt, die den selben Effekt haben " +
                    "Hier ist aus Platzgründen jeweils nur eine Variante aufgelistet. Für die volle Liste" +
                    " und vollständige Erklärung siehe <#431229903619227678> oder ersetze das ! mit einem ?. Dies wird" +
                    " eine Hilfe-Seite für den jeweiligen Command auswerfen.";
            if (author.getRoles().contains(member)) guestcommands += membercommands;
            if (author.getRoles().contains(admin)) guestcommands += admincommands;
            channel.sendMessage(guestcommands + alternatives).queue();
        }
    }

    //the bot responds to the author
    void halloWalter(MessageChannel channel, String name, List<String> command, boolean hasEnglish) {
        //command is missing arguments
        if (command.size() < 2) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I was not given enough arguments.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, mir wurden zu wenige Argumenten gegeben.").queue();
            }
            Walter.ListenerOld.printError("Not enough arguments. Number of arguments: " + command.size() + ".");
            return;
        }

        //check if "walter" is written correctly
        if (command.get(1).toLowerCase().contains("walter")) channel.sendMessage(command.get(0) + ", " + name + "!").queue();
        else {
            if (hasEnglish) {
                channel.sendMessage("You know, you actually need to spell my name correctly! Like this: W-a-l-t-e-r.").queue();
            } else {
                channel.sendMessage("Du musst meinen Namen schon richtig aussprechen! So: W-a-l-t-e-r.").queue();
            }
            Walter.ListenerOld.printError("no correct input after " + command.get(0));
        }
    }

    //prints a help page into the chat
    void help(MessageChannel channel, Member author, List<Member> admins, boolean hasEnglish) {
        String adminNames = "";
        for (int i = 0; i < admins.size(); i++) {
            adminNames += admins.get(i).getEffectiveName();
            if (i + 1 != admins.size()) adminNames += ", ";
        }
        if (hasEnglish) {
            channel.sendMessage("On this server we have the **Infos & Schnurr Schnurr**-Category." +
                    " If it appears empty you need to click it once, so it will expand and reveal all its channels." +
                    "\nThere you will find a few channels:" +
                    "\n<#431236543651250186> has the most important infos summarized, the other channels are specialized on a specific topic." +
                    " Please first make sure that your questions is not answered there before asking someone else." +
                    "\nYou can use !commands to see a list of commands you can use and what arguments they need." +
                    " If you need further explanation of commands have a look at <#435061241942245376> or simply replace " +
                    "the ! with a ?. This will display a full explanation of the specific command." +
                    "\nLast but not least you can ask your Admins for help." +
                    "\nAdmins: " + adminNames).queue();
        } else {
            channel.sendMessage("*If you do not speak german please use !english to obtain the english-role." +
                    "This will cause me to talk english with you and changes the Infos & Schnurr Schnurr-category to english.*" +
                    "\nAuf diesem Server haben wir die **Infos & Schnurr Schnurr**-Kategorie." +
                    " Falls diese leer erscheint ist sie zusammengeklappt. Klicke einmal auf sie und die Channel sollten erscheinen." +
                    "\nHier findest du ein paar Channel:" +
                    "\n<#430377306520027138> ist eine einseitige Zusammenfassung der wichtigsten Informationen." +
                    " Die anderen Channel sind auf ein spezielles Thema spezialisiert. Vergewissere dich, diesen " +
                    "Channel gelesen zu haben bevor du jemand anderen um Hilfe bittest." +
                    "\nDu kannst !commands verwenden um dir eine Liste der dir zur Verfügung stehenden Commands " +
                    "und deren erforderliche Argumente anzeigen zu lassen. Schlage unter <#431229903619227678> nach " +
                    "falls du weitere Erklärungen benötigst oder ersetzte einfach das ! mit ?. Dies wirft eine " +
                    "vollständige Erklärung für diesen Command in den Chat." +
                    "\nZu guter Letzt kannst du natürlich deine Admins um Hilfe bitten." +
                    "\nAdmins: " + adminNames).queue();
        }
    }

    //changes the listening-text below the bot into the given string
    void listening(MessageChannel channel, List<String> command, Presence pres, boolean hasEnglish) {
        if (command.size() < 2) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I was not given enough arguments.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, mir wurden zu wenige Argumente gegeben.").queue();
            }
            Walter.ListenerOld.printError("Not enough arguments. Number of arguments: " + command.size() + ".");
            return;
        }
        if (command.get(1).charAt(0) == ' ') {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but the name of what you want me to listen to must not start with a whitespace.").queue();
            } else {
                channel.sendMessage("Es tut mir Leid, der Name dessen, das ich hören soll, darf nicht mit einem Leerzeichen beginnen.").queue();
            }
            Walter.ListenerOld.printError("Sound name started with a whitespace.");
            return;
        }
        pres.setGame(Game.listening(command.get(1)));
    }

    //changes the playing-text below the bot into the given string
    void playing(MessageChannel channel, List<String> command, Presence pres, boolean hasEnglish) {
        if (command.size() < 2){
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I was not given enough arguments.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, mir wurden zu wenige Argumenten gegeben.").queue();
            }
            Walter.ListenerOld.printError("Not enough arguments. Number of arguments: " + command.size() + ".");
            return;
        }
        if (command.get(1).charAt(0) == ' ') {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but the name of the game you want me to play must not start with a whitespace.").queue();
            } else {
                channel.sendMessage("Es tut mir Leid, der Name des Spiels, das ich spielen soll, darf nicht mit einem Leerzeichen beginnen.").queue();
            }
            Walter.ListenerOld.printError("Game name started with a whitespace.");
            return;
        }
        pres.setGame(Game.playing(command.get(1)));
    }

    //prints a random number between 1 and the given limit into the chat
    void roll(MessageChannel channel, int limit, boolean hasEnglish) {
        int result = (int)(Math.random() * limit + 1);

        if (hasEnglish) {
            channel.sendMessage("Your random number between 1 and " + limit + " is: " + result).queue();
        } else {
            channel.sendMessage("Deine zufällige Zahl zwischen 1 und " + limit + " lautet: " + result).queue();
        }
    }

    //changes the watching-text below the bot into the given string
    void watching(MessageChannel channel, List<String> command, Presence pres, boolean hasEnglish) {
        if (command.size() < 2) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I was not given enough arguments.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, mir wurden zu wenige Argumente gegeben.").queue();
            }
            Walter.ListenerOld.printError("Not enough arguments. Number of arguments: " + command.size() + ".");
            return;
        }
        if (command.get(1).charAt(0) == ' ') {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but the name of what you want me to watch must not start with a whitespace.").queue();
            } else {
                channel.sendMessage("Es tut mir Leid, der Name dessen, das ich schauen soll, darf nicht mit einem Leerzeichen beginnen.").queue();
            }
            Walter.ListenerOld.printError("Watch name started with a whitespace.");
            return;
        }
        pres.setGame(Game.watching(command.get(1)));
    }



    //member commands

    //used to revoke the "english" role. causes walter to talk in german,
    //hides the english info-category and shows the german one.
    void deutsch(MessageChannel channel, Member author, Guild guild, Role english) {
        guild.getController().removeSingleRoleFromMember(author, english).queue();
    }

    //used to self-assign the "english" role. causes walter to talk in english,
    //hides the german info-category and shows the english one.
    void english(MessageChannel channel, Member author, Guild guild, Role english) {
        guild.getController().addSingleRoleToMember(author, english).queue();
    }

    //assigns the role guest to a user
    void guest(MessageChannel channel, Role guest, Role member, Guild guild, List<String> command, boolean hasEnglish) {
        if (command.size() < 2) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but you need to give me the name of the user you want to make a guest.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, Du musst mir den Namen des Users geben den du zum Guest machen willst.").queue();
            }
            Walter.ListenerOld.printError("Not enough arguments. Number of arguments: " + command.size() + ".");
            return;
        }

        List<Member> servermember = guild.getMembersByEffectiveName(command.get(1), true);

        if (servermember.size() == 0) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I have not found a user named " + command.get(1) + ".").queue();
            } else {
                channel.sendMessage("Es tut mir leid, doch ich habe keinen User mit dem Namen " + command.get(1) + " gefunden.").queue();
            }
            Walter.ListenerOld.printError("No member called " + command.get(1) + " could be found.");
            return;
        }
        if (servermember.size() > 1) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but " + command.get(1) + " fits several users.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, doch " + command.get(1) + " könnte auf mehrere Benutzer zutreffen.").queue();
            }
            Walter.ListenerOld.printError("More than one member called " + command.get(1) + " found");
            return;
        }

        Member target = servermember.get(0);

        if (target.getRoles().contains(member)) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but you cannot give the role " + guest.getName() + " to a " + member.getName() + ".").queue();
            } else {
                channel.sendMessage("Es tut mir leid, doch du kannst einem " + member.getName() + " nicht die Rolle " + guest.getName() + " zuweisen").queue();
            }
            Walter.ListenerOld.printError("Tried to give a " + member.getName() + "the role " + guest.getName());
            return;
        }

        if (target.getRoles().contains(guest)) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but " + target.getEffectiveName() + " already has the role " + guest.getName() + ".").queue();
            } else {
                channel.sendMessage("Es tut mir leid, doch " + target.getEffectiveName() + " hat bereits die Rolle " + guest.getName() + ".").queue();
            }
            Walter.ListenerOld.printError("Tried to give a member (" + command.get(1) + ") a role (" + guest.getName() + ") it already had.");
            return;
        }

        guild.getController().addSingleRoleToMember(target, guest).queue();
        if (hasEnglish) {
            channel.sendMessage(target.getEffectiveName() + " now has the role " + guest.getName()).queue();
        } else {
            channel.sendMessage(target.getEffectiveName() + " hat nun die Rolle " + guest.getName()).queue();
        }
        Walter.ListenerOld.printInfo("Member " + command.get(1) + "got the Role " + guest.getName() + ".");
    }

    //assigns the role member to a user
    void member(MessageChannel channel, Role member, Role guest, Guild guild, List<String> command, boolean hasEnglish) {
        if (command.size() < 2) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but you need to give me the name of the user you want to make a member.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, Du musst mir den Namen des Users geben den du zum Member machen willst.").queue();
            }
            Walter.ListenerOld.printError("Not enough arguments. Number of arguments: " + command.size() + ".");
            return;
        }

        List<Member> servermember = guild.getMembersByEffectiveName(command.get(1), true);

        if (servermember.size() == 0) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I have not found a user named " + command.get(1) + ".").queue();
            } else {
                channel.sendMessage("Es tut mir leid, doch ich habe keinen User mit dem Namen " + command.get(1) + " gefunden.").queue();
            }
            Walter.ListenerOld.printError("No member called " + command.get(1) + " could be found.");
            return;
        }
        if (servermember.size() > 1) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but " + command.get(1) + " fits several users.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, doch " + command.get(1) + " könnte auf mehrere Benutzer zutreffen.").queue();
            }
            Walter.ListenerOld.printError("More than one member called " + command.get(1) + " found");
            return;
        }

        Member target = servermember.get(0);

        //if the targeted user already has the role guest it is revoked, as member > guest
        if (target.getRoles().contains(guest)) {
            guild.getController().removeSingleRoleFromMember(target, guest).queue();
        }

        if (target.getRoles().contains(member)) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but " + target.getEffectiveName() + " already has the role " + member.getName() + ".").queue();
            } else {
                channel.sendMessage("Es tut mir leid, doch " + target.getEffectiveName() + " hat bereits die Rolle " + member.getName() + ".").queue();
            }
            Walter.ListenerOld.printError("Tried to give a member (" + command.get(1) + ") a role (" + member.getName() + ") it already had.");
            return;
        }

        guild.getController().addSingleRoleToMember(target, member).queue();
        if (hasEnglish) {
            channel.sendMessage(target.getEffectiveName() + " now has the role " + member.getName()).queue();
        } else {
            channel.sendMessage(target.getEffectiveName() + " hat nun die Rolle " + member.getName()).queue();
        }
        Walter.ListenerOld.printInfo("Member " + command.get(1) + "got the Role " + member.getName() + ".");
    }

    //takes care of assigning and revoking the "pimmel" role
    void pimmel(MessageChannel channel, Member author, Role pimmel, Role guest, Guild server, List<String> command, boolean hasEnglish) {
        //checking if the name of the target is given
        if (command.size() < 2) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I was not given enough Arguments.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, mir wurden zu wenige Argumenten gegeben.").queue();
            }
            Walter.ListenerOld.printError("Not enough arguments. Number of arguments: " + command.size() + ".");
            return;
        }

        //getting all members that have the given name
        List<Member> servermember = server.getMembersByEffectiveName(command.get(1), true);

        if (servermember.size() == 0) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I have not found a user named " + command.get(1) + ".").queue();
            } else {
                channel.sendMessage("Es tut mir leid, doch ich habe keinen User mit dem Namen " + command.get(1) + " gefunden.").queue();
            }
            Walter.ListenerOld.printError("No member called " + command.get(1) + " could be found.");
            return;
        }
        if (servermember.size() > 1) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but " + command.get(1) + " fits several users.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, doch " + command.get(1) + " könnte auf mehrere Benutzer zutreffen.").queue();
            }
            Walter.ListenerOld.printError("More than one member called " + command.get(1) + " found");
            return;
        }

        Member target = servermember.get(0);

        //if someone tries to give Walter the pimmel-status, the author becomes the pimmel instead
        if (target.getEffectiveName().equals("Walter")) {
            if (hasEnglish) {
                channel.sendMessage("En gardé! Nice try, pimmel!").queue();
            } else {
                channel.sendMessage("En gardé! Netter Versuch, du Pimmel!").queue();
            }
            server.getController().addSingleRoleToMember(author, pimmel).queue();
        } else {
            //checks whether or not the target already has the "pimmel" role.
            //if not, it is assigned.
            //if it has, it is revoked.
            if (target.getRoles().contains(pimmel)) {
                server.getController().removeSingleRoleFromMember(target, pimmel).queue();
                Walter.ListenerOld.printInfo("Member " + command.get(1) + " got his Pimmel role revoked");
            } else {
                //assigning the pimmel role
                //if the target is a guest, the author gets the pimmel role instead
                if (target.getRoles().contains(guest)) {
                    if (hasEnglish) {
                        channel.sendMessage("Your mother didn't raise you right! You are supposed to be polite to your guests!").queue();
                    } else {
                        channel.sendMessage("Deine Mutter hat dich wohl nicht gut erzogen! Man ist höflich zu seinen Gästen!").queue();
                    }
                    server.getController().addSingleRoleToMember(author, pimmel).queue();
                } else {
                    server.getController().addSingleRoleToMember(target, pimmel).queue();
                    Walter.ListenerOld.printInfo("Member " + command.get(1) + " got the role Pimmel");
                }
            }
        }
    }

    //placeholder for werewolf stuff
    void werewolf() {
        //TODO do this
    }



    //admin commands

    void analyze(MessageChannel channel, boolean hasEnglish) {
        //TODO this
        try {
            File logFile = new File("/home/pi/walter/voicelogs");

            if (logFile.exists()) {

                HashMap<String, UserInfo> userData = new HashMap<String, UserInfo>();
                BufferedReader br = new BufferedReader(new FileReader(logFile));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] sepLine = line.split(" ");
                    switch (sepLine[0]) {
                        case "0":
                            if (userData.containsKey(sepLine[1])) userData.get(sepLine[1]).disconnect(Long.parseLong(sepLine[3]));
                            else userData.put(sepLine[1], new UserInfo());
                            break;
                        case "1":
                            if (userData.containsKey(sepLine[1])) userData.get(sepLine[1]).connect(Long.parseLong(sepLine[3]));
                            else userData.put(sepLine[1], new UserInfo(Long.parseLong(sepLine[3])));
                            break;
                        default:
                            Walter.ListenerOld.printError("Analizing logFile encountered unexpected logID: " + sepLine[0]);
                    }

                }

                //TODO analyze the HashMap

            } else {
                Walter.ListenerOld.printError("No file called voicelogs found.");
                if (hasEnglish) {
                    channel.sendMessage("I'm utterly sorry but I did not find the log-file.").queue();
                } else {
                    channel.sendMessage("Es tut mir Leid, doch ich habe keine log-Datei gefunden.").queue();
                }
            }
        } catch (Exception e) {
            Walter.ListenerOld.printError(e.toString());
            e.printStackTrace();
        }
    }

    void config(MessageChannel channel, List<String> command, boolean hasEnglish) {
        //TODO this
    }

    //changes the content of the message with the given id into the given text
    void editMsg(MessageChannel channel, List<String> command,  String content, boolean hasEnglish) {
        //the command requires at least three arguments
        if (command.size() < 2) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I was not given a message-ID.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, mir wurde keine Message-ID gegeben.").queue();
            }
            Walter.ListenerOld.printError("Not enough arguments. Number of arguments: " + command.size() + ".");
            return;
        }

        //getting the target message with the message ID
        Message msg = channel.getMessageById(command.get(1)).complete();

        //replacing the content of the message with the temporary string
        msg.editMessage(content.substring(10 + command.get(1).length())).queue();
    }

    //sends the file with the given filename into the chat
    void file(MessageChannel channel, List<String> command, boolean hasEnglish) {

        //if the name of the file is missing
        if (command.size() < 2) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I was not given a file name.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, mir wurde kein Dateiname gegeben.").queue();
            }
            Walter.ListenerOld.printError("Not enough arguments. Number of arguments: " + command.size() + ".");
            return;
        }

        Walter.ListenerOld.postFileToChannel(channel, command.get(1), hasEnglish, false);
    }

    //prints the message with the given id into the stdout and the chat
    void getMsg(MessageChannel channel, List<String> command, boolean hasEnglish) {
        //ID is missing
        if (command.size() < 2) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I was not given a message ID.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, mir wurde keine Message-ID gegeben.").queue();
            }
            Walter.ListenerOld.printError("Not enough arguments. Number of arguments: " + command.size() + ".");
            return;
        }

        if (command.size() > 3) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I was given too many arguments.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, mir wurden zu viele Argumenten gegeben.").queue();
            }
            Walter.ListenerOld.printError("Too many arguments. Number of arguments: " + command.size() + ".");
            return;
        }

        //If there is no message found an exception is thrown that is caught and printed back at the switch
        Message msg = channel.getMessageById(command.get(1)).complete();

        Walter.ListenerOld.printInfo("Pulled Message" +
                "\nID:     " + command.get(1) +
                "\nAuthor: " + msg.getMember().getEffectiveName() +
                "\n" + msg.getContentRaw());

        channel.sendMessage("Pulled Message:\n```" +
                "\nID:     " + command.get(1) +
                "\nAuthor: " + msg.getMember().getEffectiveName() +
                "\n" + msg.getContentRaw().replaceAll("```", "") + "```").queue();
    }

    void patch(Message msg, TextChannel news,  boolean hasEnglish) {
        //0123456789
        //!patch blindtext
        //the text after !patch will be posted as a patchnote from the server patch webhook
        List<Webhook> newsHooks = news.getWebhooks().complete();
        if (!newsHooks.isEmpty()) {
            for (Webhook hook: newsHooks) {
                if (hook.getName().equals("Patch Notes")) {
                    WebhookClientBuilder cBuilder = hook.newClient();
                    WebhookClient client = cBuilder.build();
                    WebhookMessageBuilder mBuilder = new WebhookMessageBuilder();
                    mBuilder.setContent(msg.getContentRaw().substring(7));

                    client.send(mBuilder.build());
                    client.close();
                    return;
                }
            }
            if (hasEnglish) {
                msg.getChannel().sendMessage("I'm utterly sorry, but I did not find a Webhook to post the Patchnotes!").queue();
            } else {
                msg.getChannel().sendMessage("Es tut mir Leid, doch ich habe keinen Webhook für die Patchnotes gefunden!").queue();
            }
        }
    }

    void reprint(MessageChannel channel,Guild guild, List<String> command, boolean hasEnglish) {
        if (command.size() < 2) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but you need to give me the ID of the channel you want to reprint.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, Du musst mir die ID des Channels geben, den du neu printen möchtest.").queue();
            }
            Walter.ListenerOld.printError("Not enough arguments. Number of arguments: " + command.size() + ".");
            return;
        }

        Long channelID = 0L;
        MessageChannel target = null;

        try {
            channelID = Long.parseLong(command.get(1));
            target = guild.getTextChannelById(channelID);
        } catch (NumberFormatException e) {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but you have not given me a valid ID of a channel.").queue();
            } else {
                channel.sendMessage("Es tut mir leid, doch du hast mir keine valide ID gegeben.").queue();
            }
            Walter.ListenerOld.printError("The given ID was not valid.");
            e.printStackTrace();
            return;
        }

        //if the given channel id was found on the server
        if (target != null) {
            //find file
            try {
                File cFile = new File("/home/pi/walter/" + channelID + ".channel");

                if (cFile.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(cFile));
                    String line = "";
                    String tempMsg = "";
                    Walter.ListenerOld.deleteMessagesOlderThan(target, 0, 50);
                    while ((line = br.readLine()) != null) {
                        switch (line) {
                            case "FILE":
                                if (!tempMsg.equals("")) {
                                    target.sendMessage(tempMsg).complete();
                                    tempMsg = "";
                                }
                                line = br.readLine();
                                Walter.ListenerOld.postFileToChannel(target, line, hasEnglish, true);
                                break;
                            case "MESSAGE":
                                if (!tempMsg.equals("")) {
                                    target.sendMessage(tempMsg).complete();
                                    tempMsg = "";
                                }
                                break;
                            default:
                                tempMsg += "\n" + line;
                        }
                    }

                    if (!tempMsg.equals("")) target.sendMessage(tempMsg).complete();
                } else {
                    if (hasEnglish) {
                        channel.sendMessage("I'm utterly sorry but I have not found a file named " + channelID + ".channel").queue();
                    } else {
                        channel.sendMessage("Es tut mir leid, ich habe keine Datei mit den Namen " + channelID + ".channel gefunden.").queue();
                    }
                    Walter.ListenerOld.printError("No file found. Given name: " + channelID);
                }
            } catch (Exception e) {
                Walter.ListenerOld.printError(e.toString());
                e.printStackTrace();
            }

            //delete channel
            //reprint according to file

        } else {
            if (hasEnglish) {
                channel.sendMessage("I'm utterly sorry but I could not find a channel with the ID " + command.get(1) + ".").queue();
            } else {
                channel.sendMessage("Es tut mir leid, doch ich habe keinen Kanal mit der ID " + command.get(1) + " gefunden.").queue();
            }
            Walter.ListenerOld.printError("No channel was found with the given ID.");
        }
    }



    //the bot posts the given text into the chat
    void say(Message msg, boolean hasEnglish) {

        //012345678
        //!say blindtext
        //deleting the original message (that triggered this command) and writing the message again with walter as author
        if (msg.getContentRaw().length() < 6) {
            if (hasEnglish) {
                msg.getChannel().sendMessage("I'm utterly sorry but you did not give me a message to send.").queue();
            } else {
                msg.getChannel().sendMessage("Es tut mir Leid, doch du hast mir keine Nachricht zum senden gegeben.").queue();
            }
            return;
        }
        msg.delete().queue();
        msg.getChannel().sendMessage(msg.getContentRaw().substring(5)).queue();
    }



    //owner commands

    //stops the bot safely
    void shutdown(JDA jda, Survey survey) {
        survey.clear();
        survey.print();
        jda.shutdown();
    }
}
