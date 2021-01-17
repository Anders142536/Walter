package Walter.Settings;

import Walter.Config;
import Walter.EventScheduler;
import Walter.exceptions.ReasonedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;

public abstract class EventSetting implements Runnable {
    LocalDateTime startDate;
    String name;

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    //this is required in order for snakeyaml to be able to load
    public void setStartDate(@Nonnull String value) {
        if (value.equals("Undefined")) startDate = null;
        else startDate = LocalDateTime.parse(value, Config.dateFormat);
    }

    public void setStartDate(LocalDateTime value) { this.startDate = value; }

    public boolean hasName() { return !(name == null || name.isBlank()); }

    public boolean hasStartDate() { return startDate != null; }

    @Nonnull
    public String getName() { return (hasName() ? name : "Unnamed"); }

    @Nonnull    //has to return Object as otherwise snakeyaml wont find the getter for some stupid reason
    public Object getStartDate() { return (hasStartDate() ? startDate.format(Config.dateFormat) : "Undefined"); }

    @Nullable
    public LocalDateTime getStartDateValue() { return startDate; }

    //public abstract void merge(EventSetting event) throws ReasonedException;

    @Override
    public abstract void run();

    public abstract String toString();

    public String shortToString() {
        //| 11/11/2021 00:00:00 (Scheduled) |
        return String.format("`| %-31s | %-10s |` %s",
                (hasStartDate() ? getStartDate()  + " (" + EventScheduler.instance.getEventState(this) + ")" : "Undefined"),
                getType(), getName());
    }

    abstract String getType();
}
