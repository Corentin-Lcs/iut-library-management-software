package application.server.configs.components;

public record TimeConfigType(
    long days,
    long hours,
    long minutes,
    long seconds
) {
    private static final long DAY_HOURS = 24;
    private static final long HOUR_MINUTES = 60;
    private static final long MINUTE_SECONDS = 60;

    public TimeConfigType {
        if (days < 0) {
            throw new IllegalArgumentException("The days must be greater than or equal to 0");
        }
        if (hours < 0) {
            throw new IllegalArgumentException("The hours must be greater than or equal to 0");
        }
        if (minutes < 0) {
            throw new IllegalArgumentException("The minutes must be greater than or equal to 0");
        }
        if (seconds < 0) {
            throw new IllegalArgumentException("The seconds must be greater than or equal to 0");
        }
    }

    public long toSeconds() {
        return days * DAY_HOURS * HOUR_MINUTES * MINUTE_SECONDS +
               hours * HOUR_MINUTES * MINUTE_SECONDS +
               minutes * MINUTE_SECONDS +
               seconds;
    }
}
