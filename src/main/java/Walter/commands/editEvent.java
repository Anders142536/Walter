package Walter.commands;

import Walter.Config;
import Walter.EventScheduler;
import Walter.Helper;
import Walter.Parsers.DateTimeOption;
import Walter.Parsers.Flag;
import Walter.Parsers.StringOption;
import Walter.Settings.SeasonSetting;
import Walter.entities.BlackChannel;
import Walter.entities.Language;
import Walter.exceptions.CommandExecutionException;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class editEvent extends Command {
    StringOption eventname;

    Flag startdate;
    Flag serverLogo;
    Flag walterLogo;
    Flag memberColor;

    DateTimeOption startDateOption;
    StringOption serverLogoOption;
    StringOption walterLogoOption;
    StringOption memberColorOption;

    public editEvent () {
        super(new String[] {
                "This command allows you to edit the event **EVENT NAME** by overwriting the values you give " +
                        "via the flags.\n\n" +
                        "`!editevent Halloween --serverlogo halloweenServerLogo.png`\n" +
                        "This changes the server logo for the duration of the halloween event to \"halloweenServerLogo.png\".\n\n" +
                        "`!editevent Halloween --serverlogo default`\n" +
                        "This clears the defined server logo for the halloween event. \"default\" always clears " +
                        "a field and uses the default for the duration of the event. The start date is the only thing " +
                        "that cannot be cleared.\n\n" +
                        "`!editevent Halloween --startdate 25/10/2021`\n" +
                        "This sets the start of halloween to 25th of October 2021, 00:00.\n\n" +
                        "`!editevent Halloween --startdate \"25/10/2021 13:37\"`\n" +
                        "This sets the start of halloween to 25th of October 2021, 13:37.\n\n" +
                        "`!editevent Halloween --membercolor #e3780a`\n" +
                        "This sets the color of the member role to a nice orange for the duration of the event. Colors " +
                        "are accepted in hex form with no transparency.\n\n" +
                        "This is rather complicated, so please have a look at the help pages if you have any questions, " +
                        "or simply ask Anders.",
                "Dieser Befehl ermöglicht dir das Editieren des Events **EVENTNAME** durch Überschreiben der mit Flags " +
                        "gegebenen Werte.\n\n" +
                        "`!editiereEvent Halloween --serverlogo halloweenServerLogo.png`\n" +
                        "Dies ändert das Serverlogo für die Dauer des Halloween-Events zu \"halloweenServerLogo.png\".\n\n" +
                        "`!editiereEvent Halloween --serverlogo default`\n" +
                        "Dies entfernt das für die Dauer des Halloween-Events definierte Serverlogo. \"default\" entfernt " +
                        "immer definierte Werte und nutzt stattdessen den default für die Dauer des Events. Das Startdatum " +
                        "ist das Einzige, das nicht entfernt werden kann.\n\n" +
                        "`!editiereEvent Halloween --startdate 25/10/2021`\n" +
                        "Dies setzt den Anfang des Halloween-Events auf den 25ten Oktober 2021, 00:00 Uhr.\n\n" +
                        "`!editiereEvent Halloween --startdate \"25/10/2021 13:37\"`\n" +
                        "Dies setzt den Anfang des Halloween-Events auf den 25ten Oktober 2021, 13:37 Uhr.\n\n" +
                        "`!editiereEvent Halloween --membercolor #e3780a`\n" +
                        "Dies setzt die Farbe der Member-Rolle für die Dauer des Halloween-Events auf ein hübsches Orange. " +
                        "Farben werden in transparenzloser Hexadezimal-Darstellung akzeptiert.\n\n" +
                        "Diese Dinge sind recht komplex, daher zieh bitte die Hilfe-Seiten zu Rat oder frag einfach Anders."
        });

        keywords = new String[][] {
                {"editEvent"},
                {"editiereEvent", "bearbeiteEvent"}
        };

        eventname = new StringOption(new String[] {"event name", "Eventname"},
                new String[] {"Name of event to edit",
                "Name des zu editierenden Events"});
        options = new ArrayList<>();
        options.add(eventname);

        startDateOption = new DateTimeOption(new String[] {"start date", "Startdatum"},
                new String[] {"Date and time for the event", "Datum und Uhrzeit für das Event"});
        startdate = new Flag('d', "startdate", new String[] {
                "Date and time for the event", "Datum und Uhrzeit für das Event"},
                startDateOption);

        serverLogoOption = new StringOption(new String[] {"server logo file", "Serverlogo-Datei"},
                new String[] {"file to use as server logo", "Datei, die als Serverlogo genutzt wird"});
        serverLogo = new Flag('s', "serverlogo", new String[] {
                "file to use as server logo", "Datei, die als Serverlogo genutzt wird"},
                serverLogoOption);

        walterLogoOption = new StringOption(new String[] {"walter logo file", "Walterlogo-Datei"},
                new String[] {"file to use as walter logo", "Datei, die als Walterlogo genutzt wird"});
        walterLogo = new Flag('w', "walterlogo", new String[] {
                "file to use as walter logo", "Datei, die als Walterlogo genutzt wird"},
                walterLogoOption);

        memberColorOption = new StringOption(new String[] {"member color", "Member-Farbe"},
                new String[] {"color for the member role", "Farbe für die Member-Rolle"});
        memberColor = new Flag('m', "membercolor", new String[] {
                "color for the member role", "Farbe für die Member-Rolle"},
                memberColorOption);

        flags = new ArrayList<>();
        flags.add(startdate);
        flags.add(serverLogo);
        flags.add(walterLogo);
        flags.add(memberColor);
    }

    @Override
    public void execute(String usedKeyword, MessageReceivedEvent event) throws CommandExecutionException {
        if (!(startdate.isGiven() || serverLogo.isGiven() || walterLogo.isGiven() || memberColor.isGiven()))
            throw new CommandExecutionException(new String[] {
                    "You did not do any changes",
                    "Du hast keine Änderungen gemacht"
            });

        try {
            SeasonSetting editedEvent = (SeasonSetting)EventScheduler.instance.getEvent(eventname.getValue());
            if (startdate.isGiven()) editedEvent.setStartDate(startDateOption.getValue());
            if (serverLogo.isGiven())
                editedEvent.setServerLogoFile(isDefault(serverLogoOption)? null : serverLogoOption.getValue());
            if (walterLogo.isGiven())
                editedEvent.setWalterLogoFile(isDefault(walterLogoOption) ? null : walterLogoOption.getValue());
            if (memberColor.isGiven())
                editedEvent.setMemberColor(isDefault(memberColorOption) ? null : memberColorOption.getValue());
            EventScheduler.instance.editEvent(editedEvent);
            Config.save();
            if (BlackChannel.CONFIG.ID != event.getChannel().getIdLong()) {
                String eventData = EventScheduler.instance.getEvent(eventname.getValue()).toString();
                Helper.respond(event.getAuthor(), event.getChannel(), new String[]{
                        String.format("%s event successfully edited\n```%s```", eventname.getValue(), eventData),
                        String.format("%s Event wurde erfolgreich editiert\n```%s```", eventname.getValue(), eventData),
                });
            }
        } catch (ReasonedException e) {
            throw new CommandExecutionException(new String[] {
                    "The edit could not be done:\n" + e.getReason(Language.ENGLISH),
                    "Die Editierung konnte nicht durchgeführt werden:\n" + e.getReason(Language.GERMAN)
            });
        }

    }

    private boolean isDefault(StringOption option) {
        return option.getValue().trim().equalsIgnoreCase("default");
    }
}
