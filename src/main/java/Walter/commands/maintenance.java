package Walter.commands;

import Walter.Helper;
import Walter.entities.BlackChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class maintenance extends Command {

    public maintenance() {
        super(new String[] {
                "Sends a message into the news channel about going into maintenance and then shutting Walter down."
        });
        keywords = new String[][] { new String[] {"maintenance"}};
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() == 151010441043116032L) { // if Anders
            BlackChannel.NEWS.getInstance().sendMessage("I will shut down shortly for maintenance.").queue();
            event.getJDA().shutdown();
        } else {
            Helper.respond(event.getMember(), event.getChannel(),
                    "Es tut mir Leid, doch dieser Command steht nur Anders zu Verfügung",
                    "I am utterly sorry, but this command is only meant for Anders");
        }
    }
}
