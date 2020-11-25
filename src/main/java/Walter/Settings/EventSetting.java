package Walter.Settings;

import java.util.Date;

//TODO: make this abstract, make all events extend this class
public abstract class EventSetting extends Setting {
    Date startDate;

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean hasStartDate() { return startDate != null; }

    public Date getStartDate() { return startDate; }


    public abstract String toString();
}
