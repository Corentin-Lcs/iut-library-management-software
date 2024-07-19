package application.server.timers.tasks;

import application.server.entities.Subscriber;
import application.server.managers.TimerManager;
import application.server.timers.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class BorrowTask extends TimerTask {
    private static final Logger LOGGER = LogManager.getLogger("Borrow Task");
    private static Optional<Long> defaultDurationInSeconds;
    private final Subscriber singleSubscriber;
    private final Optional<Long> durationInSeconds;

    public BorrowTask(Subscriber singleSubscriber, Long durationInSeconds) {
        if (durationInSeconds != null && durationInSeconds <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        this.singleSubscriber = singleSubscriber;
        this.durationInSeconds = Optional.ofNullable(durationInSeconds);
    }

    public BorrowTask(Subscriber singleSubscriber) {
        this(singleSubscriber, getDefaultDurationInSeconds());
    }

    public static long getDefaultDurationInSeconds() {
        return defaultDurationInSeconds.orElseThrow(() -> new IllegalStateException("The default duration must be set"));
    }

    public static void setDefaultDurationInSeconds(Long durationInSeconds, Long maxLateReturnTimeInSeconds) {
        if (durationInSeconds == null) {
            throw new IllegalArgumentException("Duration must be set");
        }
        if (maxLateReturnTimeInSeconds == null) {
            throw new IllegalArgumentException("Max late return time must be set");
        }
        if (durationInSeconds <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        defaultDurationInSeconds = Optional.of(durationInSeconds + maxLateReturnTimeInSeconds);
    }

    @Override
    public long getDurationInSeconds() {
        return durationInSeconds.orElseThrow(() -> {
            LOGGER.error("The duration for the BorrowTask is not set");
            return new IllegalStateException("The duration must be set");
        });
    }

    @Override
    public void run() {
        try {
            singleSubscriber.banUser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            TimerManager.removeTimer(getTaskIdentifier());
        }
    }

    @Override
    public boolean isTimerCancelable() {
        return true;
    }

    public String getTaskIdentifier() {
        return "BorrowTask" + "-" + singleSubscriber.getId();
    }
}
