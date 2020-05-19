package Walter.entities;

import Walter.Walter;
import Walter.exceptions.ParseException;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.Message.Attachment;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class BlackWebhook {

    public static BlackWebhook SERVERNEWS;
    public static BlackWebhook PATCHNOTES;

    public final String url;

    BlackWebhook(String url) {
        this.url = url;
    }

    public static void loadWebhooks() throws ParseException {
        HashMap<String, String> foundWebhooks = parseWebhookFile("entities/webhooks.walter");

        //list of all webhooks
        SERVERNEWS = new BlackWebhook(foundWebhooks.get("servernews"));
        PATCHNOTES = new BlackWebhook(foundWebhooks.get("patchnotes"));
    }


    public void sendMessage(String toSend, List<Attachment> attachments) throws Exception {
        WebhookClient client = WebhookClient.withUrl(url);
        WebhookMessageBuilder msg = new WebhookMessageBuilder();
        msg.setContent(toSend);
        for (Attachment att :
                attachments) {
            msg.addFile(att.getFileName(), att.retrieveInputStream().get());
        }

        client.send(msg.build());
        client.close();
    }

    public void sendMessage(String toSend) {
        WebhookClient client = WebhookClient.withUrl(url);
        client.send(toSend);
        client.close();
    }

    private static HashMap<String, String> parseWebhookFile(String path) throws ParseException {
        //URGENT TODO: make this part of the parser of 2.5!!!!!!!!
        try {
            BufferedReader file = new BufferedReader(new FileReader(Walter.location + path));

            HashMap<String, String> result = new HashMap<>();
            String nextLine;
            String[] splitLine;
            while ((nextLine = file.readLine()) != null) {
                splitLine = nextLine.split("=");
                try {
                    result.put(splitLine[0].toLowerCase(), splitLine[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ParseException("Couldnt parse line \"" + nextLine + "\"");
                }
            }
            return result;
        } catch (IOException e) {
            throw new ParseException(e.getMessage());
        }
    }
}
