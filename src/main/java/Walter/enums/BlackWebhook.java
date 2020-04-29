package Walter.enums;

import Walter.Helper;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;

import java.io.File;
import java.util.List;

public enum BlackWebhook {
    SERVERNEWS ("https://discordapp.com/api/webhooks/472362132004929548/y3GhOq3I3_Mc7yQvj3-MYEnoX64MinYORIoW78zbB4jpFaLYeq-tT3wRqaiaKO46Vcgp"),
    PATCHNOTES ("https://discordapp.com/api/webhooks/472362475820548096/4tb7RHJtMHoOPpPN1M1qfyXCFDTKem5eWev3CXhY9MU1ZaNwcGrsixJ8Xiuh60g7SP4J");

    public final String url;

    BlackWebhook(String url) {
        this.url = url;
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
}
