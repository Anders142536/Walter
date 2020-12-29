package Walter;

import Walter.Settings.EventSetting;
import Walter.exceptions.ReasonedException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventScheduler {
    public static EventScheduler instance;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private LocalDateTime lastEventExecution;
    HashMap<String, EventSetting> scheduledEvents = new HashMap<>();

    //required for loading
    public void setLastEventExecution(LocalDateTime lastEventExecution) {
        if (lastEventExecution.isAfter(LocalDateTime.now())) this.lastEventExecution = LocalDateTime.now();
        else this.lastEventExecution = lastEventExecution;
    }

    //required for saving
    public LocalDateTime getLastEventExecution() { return lastEventExecution; }

    public void executionNotify(EventSetting event) {
        lastEventExecution = event.getStartDateValue();
        scheduledEvents.remove(event.getName());
    }

    public void scheduleEvent(EventSetting event) throws ReasonedException {
        if (scheduledEvents.containsKey(event.getName())) throw new ReasonedException(new String[] {
                "There is already an event scheduled called " + event.getName(),
                "Es ist bereits ein Event namens " + event.getName() + " definiert"
        });
        if (!event.hasStartDate()) throw new ReasonedException(new String[] {
                "The event " + event.getName() + " has no start date defined",
                "Das Event " + event.getName() + " hat kein Startdatum definiert"
        });
        if (lastEventExecution != null && event.getStartDateValue().isBefore(lastEventExecution)) throw new ReasonedException(new String[] {
                "The event " + event.getName() + " would be overwritten by the active event",
                "Das Event " + event.getName() + " würde vom aktuellen Event überschrieben werden"
        });

        scheduler.schedule(event, getDelayUntilStartDate(event), TimeUnit.MILLISECONDS);
        scheduledEvents.put(event.getName(), event);
    }

    public void resetAndScheduleEvents(Collection<EventSetting> events) throws ReasonedException {
        scheduler.shutdownNow();
        scheduledEvents.clear();
        lastEventExecution = null;

        EventSetting current = null;
        LocalDateTime now = LocalDateTime.now();

        for (EventSetting event: events) {
            if (!event.hasStartDate()) continue;
            if (event.getStartDateValue().isAfter(now)) scheduleEvent(event);
            else if (current == null || current.getStartDateValue().isBefore(event.getStartDateValue()))
                current = event;
        }

        if (current != null) scheduleEvent(current);
    }

    private long getDelayUntilStartDate(EventSetting event) {
        return LocalDateTime.now().until(event.getStartDateValue(), ChronoUnit.MILLIS);
    }

    public void editEvent(EventSetting event) throws ReasonedException {
        deleteEvent(event.getName());
        scheduleEvent(event);
    }

    public void deleteEvent(String name) throws ReasonedException {
        scheduledEvents.remove(name);
        resetAndScheduleEvents(scheduledEvents.values());
    }

    public String getFormattedListOfScheduledEvents() {
        return "";
    }
}
