package Walter.Settings;

import javax.annotation.Nonnull;
import java.util.Date;

//TODO: make this abstract, make all events extend this class
public class EventSetting extends Setting {
    //Datetime start
    public String name;
    public Date start;
    public Date end;

    public EventSetting() {

    }
}
