package application.server.factories;

import application.server.configs.EmailConfig;
import application.server.managers.ConfigurationManager;
import application.server.managers.EmailManager;

public class EmailFactory {
    public static void setupEmail() {
        EmailConfig emailConfig = ConfigurationManager.getConfig(EmailConfig.class, EmailConfig.getEmailConfigFilePath());
        EmailManager.setFromConfig(emailConfig);
    }
}
