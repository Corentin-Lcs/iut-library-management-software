package application.server.managers;

import application.server.configs.EmailConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Properties;

public class EmailManager {
    private static final Logger LOGGER = LogManager.getLogger("Email Manager");
    private static Optional<String> EMAIL_HOST = Optional.empty();
    private static Optional<Integer> EMAIL_PORT = Optional.empty();
    private static Optional<String> EMAIL_USER = Optional.empty();
    private static Optional<String> EMAIL_PASSWORD = Optional.empty();

    public static void setFromConfig(EmailConfig config) {
        EMAIL_HOST = Optional.of(config.emailHost());
        EMAIL_PORT = Optional.of(config.emailPort());
        EMAIL_USER = Optional.of(config.emailUser());
        EMAIL_PASSWORD = Optional.of(config.emailPassword());
    }

    private static boolean isEmailConfigSet() {
        return EMAIL_HOST.isPresent() &&
               EMAIL_PORT.isPresent() &&
               EMAIL_USER.isPresent() &&
               EMAIL_PASSWORD.isPresent();
    }

    private static Properties getEmailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", getHost());
        properties.put("mail.smtp.port", getPort());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        return properties;
    }

    private static Authenticator getAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(getUser(), getPassword());
            }
        };
    }

    public static void sendEmail(String recipientEmail, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        if (!isEmailConfigSet()) {
            throw new IllegalStateException("Email configs not set");
        }
        Session session = Session.getInstance(getEmailProperties(), getAuthenticator());
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(getUser(), "Médiathèque DesForge"));
        message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject, "UTF-8");
        message.setText(body, "UTF-8");
        Transport.send(message);
    }

    private static String getHost() {
        return EMAIL_HOST.orElseThrow(() -> new IllegalStateException("Email host not set"));
    }

    private static int getPort() {
        return EMAIL_PORT.orElseThrow(() -> new IllegalStateException("Email port not set"));
    }

    private static String getUser() {
        return EMAIL_USER.orElseThrow(() -> new IllegalStateException("Email user not set"));
    }

    private static String getPassword() {
        return EMAIL_PASSWORD.orElseThrow(() -> new IllegalStateException("Email password not set"));
    }
}
