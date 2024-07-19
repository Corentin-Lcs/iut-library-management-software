package application.server.configs;

import java.util.Objects;

public record EmailConfig(
    String emailHost,
    int emailPort,
    String emailUser,
    String emailPassword
) {
    private static final String EMAIL_CONFIG_FILE_PATH = "./config/email_config.json";

    public EmailConfig {
        Objects.requireNonNull(emailHost, "The email host must not be null");
        Objects.requireNonNull(emailUser, "The email user must not be null");
        Objects.requireNonNull(emailPassword, "The email password must not be null");
    }

    public static String getEmailConfigFilePath() {
        return EMAIL_CONFIG_FILE_PATH;
    }
}
