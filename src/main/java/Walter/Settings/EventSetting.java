package Walter.Settings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;

public abstract class EventSetting {
    Date startDate;
    private String name;

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean hasStartDate() { return startDate != null; }

    public String getName() { return (name == null ? "Unnamed" : name); }

    @Nullable
    public Date getStartDate() { return startDate; }


    public abstract String toString();
}
