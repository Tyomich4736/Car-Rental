package by.nosevich.carrental.model.service;

import javax.mail.MessagingException;

import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.User;

public interface MailService {
	void sendActivationMessage(User user) throws MessagingException;
	void sendSuccessfulOrderingMessage(User user, Order order) throws MessagingException;
	void sendPickUpOrderMessage(User user, Order order) throws MessagingException;
	void sendCanselOrderMessage(User user, Order order) throws MessagingException;
}
