package application.server.managers;

import application.server.timers.ControllableTimer;
import application.server.timers.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class TimerManager {
    private static final Logger LOGGER = LogManager.getLogger("Timer Manager");
    private static final ConcurrentHashMap<String, ControllableTimer> timerMap = new ConcurrentHashMap<>();

    public static void startTimer(String identifier, TimerTask timer) {
        Timer savedTimer = new Timer();
        LOGGER.debug("Starting timers {} with duration {}", identifier, timer.getDurationInSeconds());
        savedTimer.schedule(timer, TimeUnit.SECONDS.toMillis(timer.getDurationInSeconds()));
        timerMap.put(identifier, new ControllableTimer(savedTimer, timer.isTimerCancelable()));
    }

    public static void stopTimer(String identifier) throws IllegalArgumentException {
        ControllableTimer timer = timerMap.get(identifier);
        if (timer != null) {
            timer.cancel();
            timerMap.remove(identifier);
        } else {
            throw new IllegalArgumentException("Timer not found");
        }
    }

    public static void removeTimer(String identifier) {
        LOGGER.debug("Removing timer {}", identifier);
        timerMap.remove(identifier);
    }
}
