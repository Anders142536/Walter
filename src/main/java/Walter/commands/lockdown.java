package Walter.commands;

import Walter.Config;
import Walter.entities.BlackWebhook;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class lockdown extends Command {

    public lockdown() {
        super(new String[] {
                "Stops the automatic assignment of guest roles to new users on the server, effectively locking them out " +
                        "of the server. This is made for the case of invite link leakage. To stop the lockdown just use the" +
                        " same command again.",
                "Unterbindet die automatische Vergabe der Gast Rolle an neue User auf diesem Server, was sie aussperrt. " +
                        "Dies soll inivte-link leaks abwehren. Um den Lockdown zu beenden benutze den selben Befehl nochmal."
        });
        keywords = new String[][] {{"lockdown", "panic"}};
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        try {
            boolean newState = !Config.getIsLockdown();
            Config.setIsLockdown(newState);
            event.getChannel().sendMessage("The Server is now " + (newState ? "" : "no longer ") + "in lockdown.").queue();
            if (newState)
                BlackWebhook.SERVERNEWS.client.send("The server is now in lockdown. This means new users will no" +
                        " longer get the guest role, effectively locking them out of this server. If you have questions " +
                        "don't hesitate an admin.");
            else
                BlackWebhook.SERVERNEWS.client.send("The server is no longer in lockdown.");
        } catch (ReasonedException e) {
            throw new CommandExecutionException(e);
        }
    }
}
