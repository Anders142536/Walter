package Walter;

import Walter.Settings.EventSetting;
import Walter.Settings.SeasonSetting;
import Walter.exceptions.ReasonedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EventScheduler {
    public static EventScheduler instance;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private LocalDateTime lastEventExecution;

    private boolean isEventSettingListSorted = false;

    /* It would make the code a bit easier to read to just use a HashMap, as all events are identified via
     * name, but I want to use Streams a bit more, and this seems like a good use case */
    private final List<EventSetting> eventSettingList = new ArrayList<>();
    private final HashMap<String, EventSetting> scheduledEvents = new HashMap<>();
    private final HashMap<String, ScheduledFuture<?>> scheduledFutures = new HashMap<>();

    //required for loading
    public void setLastEventExecution(LocalDateTime lastEventExecution) {
        if (lastEventExecution != null && lastEventExecution.isAfter(LocalDateTime.now())) this.lastEventExecution = LocalDateTime.now();
        else this.lastEventExecution = lastEventExecution;
    }

    //required for saving
    @Nullable
    public LocalDateTime getLastEventExecution() { return lastEventExecution; }

    private void flagEventSettingListAsUnsorted() {
        if (isEventSettingListSorted) isEventSettingListSorted = false;
    }

    public boolean hasEvents() {
        return !eventSettingList.isEmpty();
    }

    public List<EventSetting> getEventSettingList() {
        /* Without this flag it would sort it even if a different setting is changed. As the
         * sorting is done via mergesort it will still use n log n even if sorted. It's minor
         * but hey, why not avoid it if we can? */
        if (!isEventSettingListSorted) {
            Helper.sortListByDate(eventSettingList);
            isEventSettingListSorted = true;
        }
        return eventSettingList;
    }

    public String getEventState(EventSetting event) {
        if (eventSettingList.stream().noneMatch((x) -> x.getName().equals(event.getName()))) return "Unknown";
        if (scheduledEvents.containsKey(event.getName())) return "Scheduled";
        return "Done";
    }

    public synchronized void executionNotify(EventSetting event) {
        lastEventExecution = event.getStartDateValue();
        scheduledEvents.remove(event.getName());
        scheduledFutures.remove(event.getName());
    }

    //for loading
    public void reset() {
        eventSettingList.clear();

        scheduledFutures.forEach((name, future) -> future.cancel(false));
        scheduledFutures.clear();
        scheduledEvents.clear();
        lastEventExecution = null;
    }

    public void resetAndScheduleEvents(@Nonnull Collection<EventSetting> events) throws ReasonedException {
        eventSettingList.clear();
        eventSettingList.addAll(events);

        reschedule();
    }

    private void reschedule() throws ReasonedException {
        scheduledFutures.forEach((name, future) -> future.cancel(false));
        scheduledFutures.clear();
        scheduledEvents.clear();
        lastEventExecution = null;

        EventSetting currentSeason = null;
        LocalDateTime now = LocalDateTime.now();

        for (EventSetting event: eventSettingList) {
            if (!event.hasStartDate()) continue;
            if (event.getStartDateValue().isAfter(now)) scheduleEvent(event);
            else if (event instanceof SeasonSetting &&
                    (currentSeason == null || currentSeason.getStartDateValue().isBefore(event.getStartDateValue()) ))
                currentSeason = event;
        }

        if (currentSeason == null) goBackToDefaults();
        else scheduleEvent(currentSeason);
    }

    private void scheduleEvent(@Nonnull EventSetting event) throws ReasonedException {
        if (event.hasName() && scheduledEvents.containsKey(event.getName())) throw new ReasonedException(new String[] {
                "There is already an event defined called " + event.getName(),
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

        ScheduledFuture<?> future = scheduler.schedule(event, getDelayUntilStartDate(event), TimeUnit.MILLISECONDS);
        if (event.hasName()) {
            scheduledEvents.put(event.getName(), event);
            scheduledFutures.put(event.getName(), future);
        }
    }

    private void goBackToDefaults() throws ReasonedException {
        SeasonSetting defaultEvent = new SeasonSetting();
        defaultEvent.setStartDate(LocalDateTime.now());
        scheduleEvent(defaultEvent);
    }

    private long getDelayUntilStartDate(@Nonnull EventSetting event) {
        return LocalDateTime.now().until(event.getStartDateValue(), ChronoUnit.MILLIS);
    }

    // for usage by user
    public EventSetting getEvent(@Nonnull String name) throws ReasonedException {
        Optional<EventSetting> toDelete = eventSettingList.stream().filter((x) -> x.getName().equals(name)).findAny();
        if (toDelete.isEmpty()) throw new ReasonedException(new String[] {
                "There is no event defined called " + name,
                "Es ist kein Event namens " + name + " definiert"
        });
        else return toDelete.get();
    }

    public void addEvent(@Nonnull EventSetting event) throws ReasonedException {
        if (eventSettingList.stream().anyMatch((x) -> x.getName().equals(event.getName()))) throw new ReasonedException(new String[] {
                "There is already an event defined called " + event.getName(),
                "Es ist bereits ein Event namens " + event.getName() + " definiert"
        });

        if (event.hasStartDate()) scheduleEvent(event);
        eventSettingList.add(event);
        flagEventSettingListAsUnsorted();
    }

    public void editEvent(@Nonnull EventSetting event) throws ReasonedException {
        deleteEvent(event.getName());
        addEvent(event);
    }

    public void deleteEvent(@Nonnull String name) throws ReasonedException {
        eventSettingList.remove(getEvent(name));

        if (scheduledEvents.values().stream().anyMatch((x) -> x.getName().equals(name))) {
            scheduledFutures.get(name).cancel(false);
            scheduledFutures.remove(name);
            scheduledEvents.remove(name);
        }

        //necessary, as the currently active event might have been deleted, in which case
        //the event before it has to be activated again
        reschedule();
        flagEventSettingListAsUnsorted();
    }

    @Nonnull
    public String getFormattedListOfEvents() {
        StringBuilder builder = new StringBuilder(
                eventSettingList.size() + " defined, " + scheduledEvents.size() + " scheduled"
        );
        for (EventSetting event : getEventSettingList()) {
            builder.append("\n").append(event.toString());
        }

        return builder.toString();
    }

    @Nonnull
    public String getShortFormattedListOfEvents() {
        StringBuilder builder = new StringBuilder(
                eventSettingList.size() + " defined, " + scheduledEvents.size() + " scheduled"
        );
        for (EventSetting event : getEventSettingList()) {
            builder.append(String.format("\n%s", event.shortToString()));
        }

        return builder.toString();
    }
}
