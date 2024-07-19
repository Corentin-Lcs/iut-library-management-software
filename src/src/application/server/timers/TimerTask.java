package application.server.timers;

public abstract class TimerTask extends java.util.TimerTask {
    public abstract String getTaskIdentifier();

    public abstract long getDurationInSeconds();

    public abstract boolean isTimerCancelable();
}
