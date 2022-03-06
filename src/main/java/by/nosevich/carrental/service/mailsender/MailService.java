package by.nosevich.carrental.service.mailsender;

import by.nosevich.carrental.dto.OrderDto;
import by.nosevich.carrental.dto.UserDto;

import javax.mail.MessagingException;

public interface MailService {
    void sendActivationMessage(UserDto user) throws MessagingException;

    void sendSuccessfulOrderingMessage(UserDto user, OrderDto order) throws MessagingException;

    void sendPickUpOrderMessage(UserDto user, OrderDto order) throws MessagingException;

    void sendCancelOrderMessage(UserDto user, OrderDto order) throws MessagingException;
}
