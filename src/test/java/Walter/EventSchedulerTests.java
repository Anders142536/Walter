package Walter;

import Walter.Settings.SeasonSetting;
import Walter.exceptions.ReasonedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EventSchedulerTests extends WalterTest {

    EventScheduler t;

    @BeforeEach
    public void reset() {
        t = new EventScheduler();
        EventScheduler.instance = t;
    }

    @AfterEach
    public void cancel() {
        assertDoesNotThrow(() -> t.reset());
    }

    private SeasonSetting createEventTwoDaysPast(String name) {
        LocalDateTime twoDayInPast = LocalDateTime.now().minusDays(2);
        SeasonSetting past2Event = new SeasonSetting();
        past2Event.setName(name);
        past2Event.setStartDate(twoDayInPast);

        return past2Event;
    }

    private SeasonSetting createEventOneDayPast(String name) {
        LocalDateTime oneDayInPast = LocalDateTime.now().minusDays(1);
        SeasonSetting past1Event = new SeasonSetting();
        past1Event.setName(name);
        past1Event.setStartDate(oneDayInPast);

        return past1Event;
    }

    private SeasonSetting createEventOneDayFuture(String name) {
        LocalDateTime oneDayInFuture = LocalDateTime.now().plusDays(1);
        SeasonSetting future1Event = new SeasonSetting();
        future1Event.setName(name);
        future1Event.setStartDate(oneDayInFuture);

        return future1Event;
    }

    private SeasonSetting createEventTwoDayFuture(String name) {
        LocalDateTime twoDaysInFuture = LocalDateTime.now().plusDays(2);
        SeasonSetting future2Event = new SeasonSetting();
        future2Event.setName(name);
        future2Event.setStartDate(twoDaysInFuture);

        return future2Event;
    }

    @Test
    public void correctStartup() {
        assertFalse(t.hasEvents());
        assertEquals("0 defined, 0 scheduled", t.getFormattedListOfEvents());
    }

    @Test
    public void addUndefinedEvent() {
        assertDoesNotThrow(() -> t.addEvent(new SeasonSetting()));

        assertTrue(t.hasEvents());
        assertEquals("1 defined, 0 scheduled\n" +
                "type:         Season\n" +
                "name:         Unnamed\n" +
                "start date:   Undefined\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.getFormattedListOfEvents());
    }

    @Test
    public void addUndefinedEventTwice() {
        assertDoesNotThrow(() -> t.addEvent(new SeasonSetting()));
        assertThrows(ReasonedException.class, () -> t.addEvent(new SeasonSetting()));

        assertTrue(t.hasEvents());
        assertEquals("1 defined, 0 scheduled\n" +
                "type:         Season\n" +
                "name:         Unnamed\n" +
                "start date:   Undefined\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.getFormattedListOfEvents());
    }

    @Test
    public void addFutureEvent() {
        SeasonSetting futureEvent = createEventOneDayFuture("futureEvent");
        assertDoesNotThrow(() -> t.addEvent(futureEvent));

        assertTrue(t.hasEvents());
        assertEquals("1 defined, 1 scheduled\n" +
                "type:         Season\n" +
                "name:         futureEvent\n" +
                "start date:   " + futureEvent.getStartDate() + " (Scheduled)\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.getFormattedListOfEvents());
    }

    @Test
    public void addPastEvent() {
        SeasonSetting pastEvent = createEventOneDayPast("pastEvent");

        assertDoesNotThrow(() -> t.addEvent(pastEvent));

        assertDoesNotThrow(() -> Thread.sleep(10));

        assertTrue(t.hasEvents());
        assertEquals("1 defined, 0 scheduled\n" +
                "type:         Season\n" +
                "name:         pastEvent\n" +
                "start date:   " + pastEvent.getStartDate() + " (Done)\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.getFormattedListOfEvents());
    }

    @Test
    public void addTwoFutureTwoPastEvent() {
        SeasonSetting past2Event = createEventTwoDaysPast("past2Event");
        SeasonSetting past1Event = createEventOneDayPast("past1Event");
        SeasonSetting future1Event = createEventOneDayFuture("future1Event");
        SeasonSetting future2Event = createEventTwoDayFuture("future2Event");

        assertDoesNotThrow(() -> t.addEvent(future1Event));
        assertDoesNotThrow(() -> t.addEvent(past2Event));
        assertDoesNotThrow(() -> t.addEvent(future2Event));
        assertDoesNotThrow(() -> t.addEvent(past1Event));

        assertDoesNotThrow(() -> Thread.sleep(10));

        assertTrue(t.hasEvents());
        assertEquals("4 defined, 2 scheduled\n" +
                "type:         Season\n" +
                "name:         past2Event\n" +
                "start date:   " + past2Event.getStartDate() + " (Done)\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT\n" +
                "type:         Season\n" +
                "name:         past1Event\n" +
                "start date:   " + past1Event.getStartDate() + " (Done)\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT\n" +
                "type:         Season\n" +
                "name:         future1Event\n" +
                "start date:   " + future1Event.getStartDate() + " (Scheduled)\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT\n" +
                "type:         Season\n" +
                "name:         future2Event\n" +
                "start date:   " + future2Event.getStartDate() + " (Scheduled)\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.getFormattedListOfEvents());
    }

    @Test
    public void addPastEventBeforeLastExecuted() {
        SeasonSetting past2Event = createEventTwoDaysPast("past2Event");
        SeasonSetting past1Event = createEventOneDayPast("past1Event");

        assertDoesNotThrow(() -> t.addEvent(past1Event));

        assertDoesNotThrow(() -> Thread.sleep(10));

        assertThrows(ReasonedException.class, () -> t.addEvent(past2Event));

        assertDoesNotThrow(() -> Thread.sleep(10));

        assertTrue(t.hasEvents());
        assertEquals("1 defined, 0 scheduled\n" +
                "type:         Season\n" +
                "name:         past1Event\n" +
                "start date:   " + past1Event.getStartDate() + " (Done)\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.getFormattedListOfEvents());
    }

    @Test
    public void deleteEvent() {
        assertDoesNotThrow(() -> t.addEvent(createEventTwoDayFuture("futureEvent")));
        assertDoesNotThrow(() -> t.deleteEvent("futureEvent"));

        assertFalse(t.hasEvents());
        assertEquals("0 defined, 0 scheduled", t.getFormattedListOfEvents());
    }

    @Test
    public void deleteNonExistantEventOnEmpty() {
        assertThrows(ReasonedException.class, () -> t.deleteEvent("doesNotExist"));

        assertFalse(t.hasEvents());
        assertEquals("0 defined, 0 scheduled", t.getFormattedListOfEvents());
    }

    @Test
    public void deleteNonExistantEventOnNonempty() {
        SeasonSetting futureEvent = createEventTwoDayFuture("lkasdf");
        assertDoesNotThrow(() -> t.addEvent(futureEvent));

        assertThrows(ReasonedException.class, () -> t.deleteEvent("doesNotExist"));

        assertTrue(t.hasEvents());
        assertEquals("1 defined, 1 scheduled\n" +
                "type:         Season\n" +
                "name:         lkasdf\n" +
                "start date:   " + futureEvent.getStartDate() + " (Scheduled)\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.getFormattedListOfEvents());
    }

    @Test
    public void editEventAddEverythingDelayOneDay() {
        SeasonSetting futureEvent = createEventOneDayFuture("futureEvent");

        assertDoesNotThrow(() -> t.addEvent(futureEvent));

        SeasonSetting newFutureEvent = createEventTwoDayFuture("futureEvent");
        assertDoesNotThrow(() -> newFutureEvent.setMemberColor("#000000"));
        assertDoesNotThrow(() -> newFutureEvent.setServerLogoFile("server.png"));
        assertDoesNotThrow(() -> newFutureEvent.setWalterLogoFile("walter.png"));

        assertDoesNotThrow(() -> t.editEvent(newFutureEvent));


        assertDoesNotThrow(() -> Thread.sleep(100));

        assertTrue(t.hasEvents());
        assertEquals("1 defined, 1 scheduled\n" +
                "type:         Season\n" +
                "name:         futureEvent\n" +
                "start date:   " + newFutureEvent.getStartDate() + " (Scheduled)\n" +
                "member color: #000000\n" +
                "server logo:  " + Walter.location + "events/server.png\n" +
                "walter logo:  " + Walter.location + "events/walter.png", t.getFormattedListOfEvents());
    }

    @Test
    public void editEventMoveToPast() {
        SeasonSetting futureEvent = createEventOneDayFuture("editEvent");

        assertDoesNotThrow(() -> t.addEvent(futureEvent));

        SeasonSetting pastEvent = createEventOneDayPast("editEvent");

        assertDoesNotThrow(() -> t.editEvent(pastEvent));

        assertTrue(t.hasEvents());

        assertDoesNotThrow(() -> Thread.sleep(100));

        assertEquals("1 defined, 0 scheduled\n" +
                "type:         Season\n" +
                "name:         editEvent\n" +
                "start date:   " + pastEvent.getStartDate() + " (Done)\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.getFormattedListOfEvents());
    }

    @Test
    public void editEventMoveToFuture() {
        SeasonSetting pastEvent = createEventOneDayPast("editEvent");

        assertDoesNotThrow(() -> t.addEvent(pastEvent));

        SeasonSetting futureEvent = createEventOneDayFuture("editEvent");

        assertDoesNotThrow(() -> t.editEvent(futureEvent));

        assertTrue(t.hasEvents());

        assertDoesNotThrow(() -> Thread.sleep(10));

        assertEquals("1 defined, 1 scheduled\n" +
                "type:         Season\n" +
                "name:         editEvent\n" +
                "start date:   " + futureEvent.getStartDate() + " (Scheduled)\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.getFormattedListOfEvents());
    }

    @Test
    public void editEventUnschedule() {
        SeasonSetting futureEvent = createEventOneDayFuture("unschedule");

        assertDoesNotThrow(() -> t.addEvent(futureEvent));

        SeasonSetting unscheduled = new SeasonSetting();
        unscheduled.setName("unschedule");

        assertDoesNotThrow(() -> t.editEvent(unscheduled));

        assertTrue(t.hasEvents());
        assertEquals("1 defined, 0 scheduled\n" +
                "type:         Season\n" +
                "name:         unschedule\n" +
                "start date:   " + unscheduled.getStartDate() + "\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.getFormattedListOfEvents());
    }

    @Test
    public void editNonExistantOnEmpty() {
        SeasonSetting newEvent = createEventOneDayPast("doesNotExist");

        assertThrows(ReasonedException.class, () -> t.editEvent(newEvent));

        assertFalse(t.hasEvents());
        assertEquals("0 defined, 0 scheduled", t.getFormattedListOfEvents());
    }

    @Test
    public void editNonExistantOnNonempty() {
        SeasonSetting existingEvent = createEventOneDayPast("exists");

        assertDoesNotThrow(() -> t.addEvent(existingEvent));

        SeasonSetting newEvent = createEventOneDayPast("doesNotExist");

        assertThrows(ReasonedException.class, () -> t.editEvent(newEvent));

        assertTrue(t.hasEvents());
        assertEquals("1 defined, 0 scheduled\n" +
                "type:         Season\n" +
                "name:         exists\n" +
                "start date:   " + existingEvent.getStartDate() + " (Done)\n" +
                "member color: DEFAULT\n" +
                "server logo:  DEFAULT\n" +
                "walter logo:  DEFAULT", t.getFormattedListOfEvents());


    }

    @Test
    public void getEventStateUnknown() {
        assertEquals("Unknown", t.getEventState(createEventOneDayFuture("lasd")));
    }

    @Test
    public void getEventStateScheduled() {
        SeasonSetting scheduled = createEventOneDayFuture("scheduledEvent");

        assertDoesNotThrow(() -> t.addEvent(scheduled));

        assertEquals("Scheduled", t.getEventState(scheduled));
    }

    @Test
    public void getEventStateDone() {
        SeasonSetting done = createEventOneDayPast("doneEvent");

        assertDoesNotThrow(() -> t.addEvent(done));

        assertDoesNotThrow(() -> Thread.sleep(10));

        assertEquals("Done", t.getEventState(done));
    }
}
