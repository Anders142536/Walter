package Walter.commands;

import Walter.Parsers.StringOption;
import Walter.Walter;
import Walter.entities.Language;
import Walter.HelpPages;
import Walter.entities.BlackRole;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class help extends Command {
    StringOption chosenPage;

    public help() throws ReasonedException {
        super(new String[] {
                "This command displays a specified help page or a list of available pages if no page is specified.",
                "Dieser Command zeigt dir eine gegebene Hilfe-Seite an oder eine Liste an verfügbaren Seiten falls keine seite gegeben ist."
        });
        keywords = new String[][]{
                {"help"},
                {"hilfe"}};
        minimumRequiredRole = BlackRole.GUEST;

        chosenPage = new StringOption(new String[] {"topic", "thema"},
                new String[] {
                        "Which page is shown",
                        "Welche Seite gezeigt wird"
                }, false);

        options = new ArrayList<>();
        options.add(chosenPage);
    }



    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        Language desiredLanguage = Language.getLanguage(event.getAuthor());

        try {
            if (chosenPage.hasValue()) {
                event.getChannel().sendMessage(HelpPages.instance.getPageEmbed(chosenPage.getValue(), desiredLanguage)).queue();
            } else {
                event.getChannel().sendMessage(HelpPages.instance.getPageListEmbed(desiredLanguage)).queue();
            }
        } catch (ReasonedException e) {
            throw new CommandExecutionException(e);
        }
    }


}
