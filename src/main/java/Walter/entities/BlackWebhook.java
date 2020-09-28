package Walter.entities;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.Message.Attachment;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BlackWebhook {

    public static BlackWebhook SERVERNEWS;
    public static BlackWebhook PATCHNOTES;

    public final String url;

    public BlackWebhook(String url) {
        this.url = url;
    }

    public void sendMessage(String toSend, List<Attachment> attachments) throws InterruptedException, ExecutionException {
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
}
