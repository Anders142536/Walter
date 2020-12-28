package Walter;

import Walter.Settings.EventSetting;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventScheduler {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public EventScheduler() {

    }

    public void scheduleEvent(EventSetting event) {
        scheduler.schedule(event, event.getDelayUntilStartDate(), TimeUnit.MILLISECONDS);
    }

    public void rescheduleEvent(EventSetting event) {

    }

    public void reset() {

    }
}
