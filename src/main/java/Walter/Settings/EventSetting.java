package Walter.Settings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class EventSetting implements Runnable {
    LocalDateTime startDate;
    protected String name;

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public boolean hasStartDate() { return startDate != null; }

    public String getName() { return (name == null || name.isBlank() ? "Unnamed" : name); }

    @Nonnull
    public String getStartDate() { return (hasStartDate() ? startDate.toString() : "Undefined"); }

    @Nullable
    public LocalDateTime getStartDateValue() { return startDate; }

    public long getDelayUntilStartDate() {
        return LocalDate.now().until(startDate, ChronoUnit.MILLIS);
    }

    @Override
    public abstract void run();

    public abstract String toString();
}
