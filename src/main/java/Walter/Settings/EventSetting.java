package Walter.Settings;

import javax.annotation.Nonnull;

//TODO: make this abstract, make all events extend this class
public class EventSetting extends Setting {
    //Datetime start
    String name;



    public EventSetting(@Nonnull String name) {
        this.name = name;
    }

    public String getName() { return name; }




}
