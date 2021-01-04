package Walter.commands;

import Walter.Parsers.Flag;
import Walter.Parsers.StringOption;

public class editEvent extends Command {
    StringOption eventname;

    Flag startdate;
    Flag serverLogo;
    Flag walterLogo;
    Flag memberColor;

    StringOption startDateOption;
    StringOption serverLogoOption;
    StringOption walterLogoOption;
    StringOption memberColorOption;

    public editEvent () {
        super(new String[] {
                "This command allows you to edit the event **EVENT NAME** by overwriting the values you give " +
                        "via the flags.\n\n" +
                        "`!editevent Halloween --serverlogo halloweenServerLogo.png`\n" +
                        "This would change the defined server logo for the halloween event to \"halloweenServerLogo.png\".\n\n" +
                        "`!editevent Halloween --serverlogo default`\n" +
                        "This would clear the defined server logo for the halloween event. \"default\" will always clear " +
                        "a field and use the default for the duration of the event. If the start date is cleared it is " +
                        "simply taken of schedule, but not deleted.\n\n" +
                        "`!editevent Halloween --startdate 25/10/2021`\n" +
                        "This would set the start of Halloween to 25th of October 2021, 00:00.\n\n" +
                        "`!editevent Halloween --startdate \"25/10/2021 13:37\"`\n" +
                        "This would set the start of Halloween to 25th of October 2021, 13:37.\n\n" +
                        "`!editevent Halloween --membercolor #e3780a`\n" +
                        "This would set the member color to a nice orange for the duration of the event. Every hex color " +
                        "with no transparency is accepted.\n\n" +
                        "This is rather complicated, so please have a look at the help pages if you have any questions, " +
                        "or simply ask Anders."
        })
    }
}
