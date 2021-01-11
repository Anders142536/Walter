package Walter.entities;

import Walter.exceptions.ReasonedException;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.Message.Attachment;

import java.nio.file.ReadOnlyFileSystemException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BlackWebhook {

    public static BlackWebhook SERVERNEWS;
    public static BlackWebhook PATCHNOTES;

    public final WebhookClient client;

    public BlackWebhook(String url) throws ReasonedException {
        try {
            this.client = WebhookClient.withUrl(url);
        } catch (IllegalArgumentException e) {
            throw new ReasonedException("Failed to initialize webhook  with url \"" + url + "\"\n"
                    + e.getMessage());
        }
    }

    public void sendMessageWithAttachments(String toSend, List<Attachment> attachments) throws InterruptedException, ExecutionException {
        WebhookMessageBuilder msg = new WebhookMessageBuilder();
        msg.setContent(toSend);
        for (Attachment att : attachments)
            msg.addFile(att.getFileName(), att.retrieveInputStream().get());

        client.send(msg.build());
    }
}
