package by.nosevich.carrental.model.service.impl;

import java.util.Properties;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.nosevich.carrental.config.properties.EmailProperties;
import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private EmailProperties emailProperties;

	@Override
	public void sendActivationMessage(User user) throws MessagingException {
		sendMessage(user, getActivationMessage(user), getActivasionSubject());
	}
	private String getActivationMessage(User user) {
		String siteName = emailProperties.getDomainHost().replaceAll("http://", "");
		return "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\">Hello, "
				+ user.getFirstName() + "!</font></h1><br/>"
				+ "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >We are glad to welcome you to our service "
				+ siteName + "!<br/>" + "To activate your account follow this link:</div> <br/>"
				+ "<h2 align=\"center\"><a href=" + emailProperties.getDomainHost() + "/activate/"
				+ user.getActivationCode() + ">Link</a></h2>";
	}
	private String getActivasionSubject() {
		String siteName = emailProperties.getDomainHost().replaceAll("http://", "");
		return siteName + " activasion link";
	}

	
	@Override
	public void sendSuccessfulOrderingMessage(User user, Order order) throws MessagingException {
		sendMessage(user, getSuccessfulOrderingMessage(order), getSuccessfulOrderingSubject(order));

	}
	private String getSuccessfulOrderingSubject(Order order) {
		return order.getCar().getName() + ", successful ordering!";
	}
	private String getSuccessfulOrderingMessage(Order order) {
		return "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\"> Order " + order.getId()
				+ " successful ordering!</font></h1><br/>"
				+ "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >The order for car "
				+ order.getCar().getName() + " has been successfully placed!We are waiting for you"
				+ order.getBeginDate() + " in our service. If you do not show up, your order will be canceled.<br/>";
	}

	
	@Override
	public void sendPickUpOrderMessage(User user, Order order) throws MessagingException {
		sendMessage(user, getPickUpOrderMessage(order), getPickUpOrderSubject(order));
	}
	private String getPickUpOrderSubject(Order order) {
		return "Pick up " + order.getCar().getName();
	}
	private String getPickUpOrderMessage(Order order) {
		return "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\"> Pick up "
				+ order.getCar().getName() + " today!</font></h1><br/>"
				+ "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >Pick up the order for car "
				+ order.getCar().getName()+".<br>"
				+ "If you do not show up until the end of the day, your order will be canceled.<br/>";
	}

	
	@Override
	public void sendCanselOrderMessage(User user, Order order) throws MessagingException {
		sendMessage(user, getCanselOrderMessage(order), getCanselOrderSubject(order));
	}
	private String getCanselOrderSubject(Order order) {
		return "Car " + order.getCar().getName() + " order has been canceled";
	}
	private String getCanselOrderMessage(Order order) {
		return "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\"> Car "
				+ order.getCar().getName() + " order has been canceled!</font></h1><br/>"
				+ "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >"
				+ "The reason is your failure to pick up the car on time.<br/>";
	}
	
	
	private void sendMessage(User user, String message, String subject) throws MessagingException {
		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", emailProperties.getHost());
		properties.put("mail.smtp.port", emailProperties.getPort());
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.debug", "false");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.user", emailProperties.getUsername());
		properties.put("mail.password", emailProperties.getPassword());

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailProperties.getUsername(), emailProperties.getPassword());
			}
		};
		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		MimeMessage msg = new MimeMessage(session);

		msg.setFrom("CarRental@service.com");
		InternetAddress[] toAddresses = { new InternetAddress(user.getEmail()) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);

		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// sets the multi-part as e-mail's content
		msg.setContent(multipart);

		// sends the e-mail
		Transport.send(msg);
	}

}
