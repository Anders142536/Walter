package Walter.enums;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;

public enum BlackWebhook {
    SERVERNEWS ("https://discordapp.com/api/webhooks/472362132004929548/y3GhOq3I3_Mc7yQvj3-MYEnoX64MinYORIoW78zbB4jpFaLYeq-tT3wRqaiaKO46Vcgp"),
    PATCHNOTES ("https://discordapp.com/api/webhooks/472362475820548096/4tb7RHJtMHoOPpPN1M1qfyXCFDTKem5eWev3CXhY9MU1ZaNwcGrsixJ8Xiuh60g7SP4J");

    public final String url;

    BlackWebhook(String url) {
        this.url = url;
    }

    public void sendMessage(String toSend) {
        WebhookClient client = WebhookClient.withUrl(url);
        client.send(toSend);
        client.close();
    }
}
