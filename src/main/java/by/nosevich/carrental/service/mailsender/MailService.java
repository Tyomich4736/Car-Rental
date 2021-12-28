package by.nosevich.carrental.service.mailsender;

import by.nosevich.carrental.entities.Order;
import by.nosevich.carrental.entities.User;

import javax.mail.MessagingException;

public interface MailService {
    void sendActivationMessage(User user) throws MessagingException;

    void sendSuccessfulOrderingMessage(User user, Order order) throws MessagingException;

    void sendPickUpOrderMessage(User user, Order order) throws MessagingException;

    void sendCanselOrderMessage(User user, Order order) throws MessagingException;
}
