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
    public void execute(MessageReceivedEvent event) throws CommandExecutionException {
        String[] messages = getPatchMessage(version.hasValue() ? version.getValue() : Walter.VERSION);

        if (here.isGiven()) {
            for (String msg: messages) {
                Helper.instance.respond(event.getChannel(), msg);
            }
        } else {
            for (String msg: messages) {
                BlackWebhook.PATCHNOTES.sendMessage(msg);
            }
        }
    }

    public String[] getPatchMessage(String version) throws CommandExecutionException {
        loadPatchFileToYaml(version);

        return null;
//        return "__**Walter " + Walter.VERSION + " Commands Update**__\n" +
//                "\n**New Features**\n" +
//                // enter new features between here..
//                // ..and here
//                "\n**Bug Fixes & Improvements**\n" +
//                // enter bugfixes between here..
//                // ..and here
//                "\nIn case you encounter any issues, have any questions or wish for new features please contact <@!151010441043116032>";
    }

    private void loadPatchFileToYaml(String version) throws CommandExecutionException {
        String filename = Walter.location + "/patchnotes/"+ version + ".patch";
        try {
            File file = new File(filename);
            FileInputStream reader = new FileInputStream(file);
            byte[] filedata = new byte[(int) file.length()];
            reader.read(filedata);
            reader.close();

            notes.load(new String(filedata, "UTF-8"));
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

    private String item(String text) {
        return ":small_orange_diamond: " + text + "\n";
    }

    private String note(String text) {
        return "    *" + text + "*\n";
    }
}
