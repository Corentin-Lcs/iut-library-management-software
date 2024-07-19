package application.server.timers;

import java.util.Timer;

public class ControllableTimer {
    private final Timer timer;
    private final boolean cancellable;

    public ControllableTimer(Timer timer, boolean cancellable) {
        this.timer = timer;
        this.cancellable = cancellable;
    }

    public void cancel() {
        if (cancellable) {
            timer.cancel();
        } else {
            throw new IllegalStateException("Timer is not cancellable");
        }
    }
}
