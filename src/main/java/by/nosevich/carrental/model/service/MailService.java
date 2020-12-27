package by.nosevich.carrental.model.service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import by.nosevich.carrental.model.entities.User;

public interface MailService {
	void sendUserActivationMessage(User user) throws MessagingException;
}