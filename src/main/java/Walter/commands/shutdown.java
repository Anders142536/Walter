package Walter.commands;


import Walter.Helper;
import Walter.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class shutdown extends Command {

    public shutdown() {
        super(new String[] {
                "I shut down. I can only be started again via an SSH connection.",
                "Ich fahre herunter. Ich kann danach nur noch über eine SSH Verbindung erneut gestartet werden."
        });
        keywords = new String[][]{
                {"shutdown"}
        };
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        if (event.getAuthor().getIdLong() == 151010441043116032L) { // if Anders
            event.getJDA().shutdown();
        } else {
            Helper.instance.respond(event.getMember(), event.getChannel(),
                    "Es tut mir Leid, doch dieser Command steht nur Anders zu Verfügung",
                    "I am utterly sorry, but this command is only meant for Anders");
        }
    }
}