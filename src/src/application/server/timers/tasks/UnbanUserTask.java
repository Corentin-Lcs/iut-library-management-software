package application.server.timers.tasks;

import application.server.entities.Subscriber;
import application.server.timers.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UnbanUserTask extends TimerTask {
    private static final Logger LOGGER = LogManager.getLogger("Ban User Task");
    private static Optional<Long> defaultDurationInSeconds;
    private final Subscriber singleSubscriber;
    private final Optional<Long> durationInSeconds;

    public UnbanUserTask(Subscriber singleSubscriber, Long durationInSeconds) {
        this.singleSubscriber = singleSubscriber;
        this.durationInSeconds = Optional.ofNullable(durationInSeconds);
    }

    public static long getDefaultDurationInSeconds() {
        return defaultDurationInSeconds.orElseThrow(() -> new IllegalStateException("The default duration must be set"));
    }

    public static void setDefaultDurationInSeconds(Long durationInSeconds) {
        if (durationInSeconds == null) {
            throw new IllegalArgumentException("Duration must be set");
        }
        if (durationInSeconds <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        defaultDurationInSeconds = Optional.of(durationInSeconds);
    }

    @Override
    public long getDurationInSeconds() {
        return durationInSeconds.orElseThrow(() -> {
            LOGGER.error("The duration for the UnbanUserTask is not set");
            return new IllegalStateException("The duration must be set");
        });
    }

    @Override
    public void run() {
        singleSubscriber.unbanUser();
    }

    @Override
    public boolean isTimerCancelable() {
        return false;
    }

    public String getTaskIdentifier() {
        return "UnbanUserTask" + "-" + singleSubscriber.getId();
    }
}
