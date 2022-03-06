package by.nosevich.carrental.service.mailsender;

import by.nosevich.carrental.dto.OrderDto;
import by.nosevich.carrental.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {

    public static final String HTTP_URL_PREFIX = "http://";
    public static final String ACTIVATION_LINK_SUBJECT = "%s activation link!";
    public static final String SUCCESSFUL_ORDERING_SUBJECT = "%s, successful ordering!";
    public static final String PICK_UP_ORDER_SUBJECT = "Pick up %s";
    public static final String CANCEL_ORDER_SUBJECT = "Order on %s has been canceled";
    public static final String CAR_RENTAL_INTERNET_ADDRESS = "CarRental@service.com";
    public static final String CONTENT_TYPE_TEXT_HTML = "text/html";

    @Value("${email.server.domainHost}")
    private String emailDomainHost;
    @Value("${email.server.host}")
    private String emailHost;
    @Value("${email.server.port}")
    private String emailPort;
    @Value("${email.server.username}")
    private String emailUsername;
    @Value("${email.server.password}")
    private String emailPassword;

    @Override
    public void sendActivationMessage(UserDto user) throws MessagingException {
        String siteName = emailDomainHost.replaceAll(HTTP_URL_PREFIX, "");
        String subject = String.format(ACTIVATION_LINK_SUBJECT, siteName);
        String message = String.format(MessageTemplates.ACTIVATION_MESSAGE, user.getFirstName(), emailDomainHost,
                                       user.getActivationCode());
        sendMessage(user, message, subject);
    }


    @Override
    public void sendSuccessfulOrderingMessage(UserDto user, OrderDto order) throws MessagingException {
        String subject = String.format(SUCCESSFUL_ORDERING_SUBJECT, order.getCar().getName());
        String message =
                String.format(MessageTemplates.SUCCESSFUL_ORDERING_MESSAGE, order.getId(), order.getCar().getName(),
                              order.getBeginDate());
        sendMessage(user, message, subject);
    }

    @Override
    public void sendPickUpOrderMessage(UserDto user, OrderDto order) throws MessagingException {
        String orderCarName = order.getCar().getName();
        String subject = String.format(PICK_UP_ORDER_SUBJECT, orderCarName);
        String message = String.format(MessageTemplates.PICK_UP_ORDER_MESSAGE, orderCarName, orderCarName);
        sendMessage(user, message, subject);
    }

    @Override
    public void sendCancelOrderMessage(UserDto user, OrderDto order) throws MessagingException {
        String orderCarName = order.getCar().getName();
        String subject = String.format(CANCEL_ORDER_SUBJECT, orderCarName);
        String message = String.format(MessageTemplates.CANCEL_ORDER_MESSAGE, orderCarName);
        sendMessage(user, message, subject);
    }

    private void sendMessage(UserDto user, String message, String subject) throws MessagingException {
        Properties properties = getMailProperties();
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPassword);
            }
        };
        Session session = Session.getInstance(properties, auth);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(CAR_RENTAL_INTERNET_ADDRESS));
        InternetAddress[] toAddresses = {new InternetAddress(user.getEmail())};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, CONTENT_TYPE_TEXT_HTML);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);

        Transport.send(msg);
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", emailHost);
        properties.put("mail.smtp.port", emailPort);
        properties.put("mail.smtp.auth", Boolean.TRUE.toString());
        properties.put("mail.debug", Boolean.FALSE.toString());
        properties.put("mail.smtp.starttls.enable", Boolean.TRUE.toString());
        properties.put("mail.user", emailUsername);
        properties.put("mail.password", emailPassword);
        return properties;
    }

}
