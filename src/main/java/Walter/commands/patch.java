package Walter.commands;

import Walter.Parsers.Flag;
import Walter.Parsers.StringOption;
import Walter.Walter;
import Walter.Helper;
import Walter.entities.BlackChannel;
import Walter.entities.BlackWebhook;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class patch extends Command {

    Yaml notes;
    StringOption version;
    Flag here;

    public patch() {
        super(new String[] {
                "This command writes hardcoded patch notes into <#" + BlackChannel.NEWS.ID + "> using the Patchnotes webhook.",
                "Dieser Command schreibt hardcodierte patch notes in <#" + BlackChannel.NEWS.ID + "> mit Hilfe des Patchnotes-Webhook."
        });
        keywords = new String[][]{new String[]{"patch"}};

        version = new StringOption(new String[] {"version"}, new String[] {
                "What version patchnotes should be printed of",
                "Welche Version die Patchnotes beschreiben sollen"}, false);
        here = new Flag('h', "here", new String[] {
                "Print patchnotes in channel of command",
                "Wirft Patchnotes im Channel des Befehlts"
        });
        options = new ArrayList<>();
        flags = new ArrayList<>();
        options.add(version);
        flags.add(here);

        notes = new Yaml();
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        ArrayList<String> messages = getPatchMessage(version.hasValue() ? version.getValue() : Walter.VERSION);

        if (here.isGiven()) {
            for (String msg: messages)
                event.getChannel().sendMessage(msg).complete();
        } else {
            for (String msg: messages)
                BlackWebhook.PATCHNOTES.client.send(msg);
        }
    }

    public ArrayList<String> getPatchMessage(String version) throws CommandExecutionException {
        Map<String, Object> list = loadPatchFileToYaml(version);
        String msg = convertYamlToFormattedMessage(list, version);
        return splitMessageOnLinebreak(msg, 1900);
    }

    private Map<String, Object> loadPatchFileToYaml(String version) throws CommandExecutionException {
        String filename = Walter.location + "/patchnotes/"+ version + "-patchnotes.yaml";
        try {
            File file = new File(filename);
            FileInputStream reader = new FileInputStream(file);
            byte[] filedata = new byte[(int) file.length()];
            reader.read(filedata);
            reader.close();

            return notes.load(new String(filedata, "UTF-8"));
        } catch (FileNotFoundException e) {
            throw new CommandExecutionException(new String[] {
                    "The file " + filename + " was not found",
                    "Die datei " + filename + " wurde nicht gefunden"
            });
        } catch (IOException e) {
            throw new CommandExecutionException(new String[] {
                    "Something went wrong on reading the file " + filename,
                    "Etwas ist beim lesen der Date " + filename + " schief gelaufen"
            });
        }
    }

    private String convertYamlToFormattedMessage(Map<String, Object> list, String version) throws CommandExecutionException {
        if (!list.containsKey("name")) throw new CommandExecutionException(new String[] {"Failed to find patch notes name"});

        StringBuilder patchmsg = new StringBuilder("__**" + version + " " + list.get("name") + "**__\n");
        for (Map.Entry<String, Object> entry: list.entrySet()) {
            if (entry.getKey().equals("name")) continue;

            patchmsg.append("\n**").append(entry.getKey()).append("**\n");
            for (LinkedHashMap<String, String> item : (ArrayList<LinkedHashMap>) entry.getValue()) {
                if (!item.containsKey("title")) throw new CommandExecutionException(new String[] {
                        "No title found for " + entry.getKey() + " entry"});
                patchmsg.append(":small_orange_diamond: ").append(item.get("title")).append("\n");
                if (item.containsKey("description"))
                    patchmsg.append("    *").append(item.get("description")).append("*\n");
            }
        }
        return patchmsg.toString();
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
}
