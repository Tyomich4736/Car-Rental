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
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private EmailProperties emailProperties;

	/*
	 * @Override public void sendUserActivationMessage(User user) { String siteName
	 * = emailProperties.getDomainHost().replaceAll("http://", "");
	 * SimpleMailMessage message = new SimpleMailMessage();
	 * message.setTo(user.getEmail()); message.setFrom("CarRental@service.com");
	 * message.setSubject(siteName+" activasion link");
	 * message.setText("Hello, "+user.getFirstName()+".\n" +
	 * "We are glad to welcome you to our service "+siteName +".\n" +
	 * "To activate your account follow the link below:\n" +
	 * emailProperties.getDomainHost()+"/activate/"+user.getId()+"/"+user.
	 * getActivationCode()); mailSender.send(message); }
	 */
	
	public void sendUserActivationMessage(User user) throws MessagingException {
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
		String siteName = emailProperties.getDomainHost().replaceAll("http://", "");
		msg.setFrom("CarRental@service.com");
		InternetAddress[] toAddresses = { new InternetAddress(user.getEmail()) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(siteName+" activasion link");

		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(getMessage(user), "text/html");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// sets the multi-part as e-mail's content
		msg.setContent(multipart);

		// sends the e-mail
		Transport.send(msg);
	}
	
	private String getMessage(User user) {
		String siteName = emailProperties.getDomainHost().replaceAll("http://", "");
		return "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\">Hello, "+user.getFirstName()+"!</font></h1><br/>" +
				  "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >We are glad to welcome you to our service "+siteName +"!<br/>" +
				  "To activate your account follow this link:</div> <br/>" +
				  "<h2 align=\"center\"><a href="+emailProperties.getDomainHost()+"/activate/"+user.getActivationCode()+">Link</a></h2>";
	}
}