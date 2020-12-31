package Walter;

import Walter.Settings.EventSetting;
import Walter.Settings.SeasonSetting;
import Walter.exceptions.ReasonedException;
import jdk.jfr.Event;

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

    public void flagEventSettingListAsUnsorted() {
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

    public synchronized void executionNotify(EventSetting event) {
        lastEventExecution = event.getStartDateValue();
        scheduledEvents.remove(event.getName());
        scheduledFutures.remove(event.getName());
    }

    //for loading
    public void resetAndScheduleEvents(@Nonnull Collection<EventSetting> events) throws ReasonedException {
        scheduledFutures.forEach((name, future) -> future.cancel(false));
        scheduledFutures.clear();
        scheduledEvents.clear();
        eventSettingList.clear();
        eventSettingList.addAll(events);
        lastEventExecution = null;

        EventSetting currentSeason = null;
        LocalDateTime now = LocalDateTime.now();

        for (EventSetting event: events) {
            if (!event.hasStartDate()) continue;
            if (event.getStartDateValue().isAfter(now)) scheduleEvent(event);
            else if (event instanceof SeasonSetting &&
                    (currentSeason == null || currentSeason.getStartDateValue().isBefore(event.getStartDateValue()) ))
                currentSeason = event;
        }

                                        //going back to defaults
        scheduleEvent(currentSeason == null ? new SeasonSetting() : currentSeason);
    }

    private void scheduleEvent(@Nonnull EventSetting event) throws ReasonedException {
        if (scheduledEvents.containsKey(event.getName())) throw new ReasonedException(new String[] {
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
        scheduledEvents.put(event.getName(), event);
        scheduledFutures.put(event.getName(), future);
    }

    private long getDelayUntilStartDate(@Nonnull EventSetting event) {
        return LocalDateTime.now().until(event.getStartDateValue(), ChronoUnit.MILLIS);
    }

    // for usage by user
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
        Optional<EventSetting> toDelete = eventSettingList.stream().filter((x) -> x.getName().equals(name)).findAny();
        if (toDelete.isEmpty()) throw new ReasonedException(new String[] {
                "There is no event defined called " + name,
                "Es ist kein Event namens " + name + " definiert"
            });
        else eventSettingList.remove(toDelete.get());

        if (scheduledEvents.values().stream().anyMatch((x) -> x.getName().equals(name))) {
            scheduledFutures.get(name).cancel(false);
            scheduledFutures.remove(name);
            scheduledEvents.remove(name);
        }

        //necessary, as the currently active event might have been deleted, in which case
        //the event before it has to be activated again
        resetAndScheduleEvents(eventSettingList);
        flagEventSettingListAsUnsorted();
    }

    @Nonnull
    public String getFormattedListOfScheduledEvents() {

        StringBuilder builder = new StringBuilder();
        for (EventSetting event: getEventSettingList()) {
            //TODO
        }


        return "";
    }
}
