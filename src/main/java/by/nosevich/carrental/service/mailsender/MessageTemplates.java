package by.nosevich.carrental.service.mailsender;

import java.util.Locale;

public class MessageTemplates {
    public static final String ACTIVATION_MESSAGE =
            "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\">Hello, %s!" +
                    "</font></h1><br/><div align=\"center\"><font face=\"Helvetica\" size=\"3\" >" +
                    "We are glad to welcome you to our rental service!<br/>" +
                    "To activate your account follow this link:</font></div> <br/>" +
                    "<h2 align=\"center\"><a href=%s/activate/%s>Link</a></h2>";

    public static final String ACTIVATION_MESSAGE_RU =
            "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\">Приветствуем, %s!" +
                    "</font></h1><br/><div align=\"center\"><font face=\"Helvetica\" size=\"3\" >" +
                    "Мы рады привествовать вас в нашем сервисе по прокату авто!<br/>" +
                    "Для активации вашего аккаунта перейдите по ссылке ниже:</font></div> <br/>" +
                    "<h2 align=\"center\"><a href=%s/activate/%s>Ссылка</a></h2>";

    public static final String SUCCESSFUL_ORDERING_MESSAGE =
            "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\"> Order " +
                    "%d was successfully placed!</font></h1><br/>" +
                    "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >The order for car " +
                    "%s has been successfully placed! We are waiting for you " +
                    "%s in our service. If you do not show up, your order will be canceled.<br/>";

    public static final String SUCCESSFUL_ORDERING_MESSAGE_RU =
            "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\"> Заказ " +
                    "%d был успешно оформлен!</font></h1><br/>" +
                    "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >Заказ на автомобиль " +
                    "%s Был успешно оформлен! Мы ждем вас " +
                    "%s в нашем сервисе. Если вы не появитесь ваш заказ будет отменен.<br/>";

    public static final String PICK_UP_ORDER_MESSAGE =
            "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\"> " +
                    "Pick up %s today!</font></h1><br/>" +
                    "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >Pick up the order for car " +
                    "%s.<br> If you do not show up until the end of the day, your order will be canceled.<br/>";

    public static final String CANCEL_ORDER_MESSAGE =
            "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\">" +
                    "Car %s order has been canceled!</font></h1><br/>" +
                    "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >" +
                    "The reason is your failure to pick up the car on time.<br/>";

    public static final String CANCEL_ORDER_MESSAGE_RU =
            "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\">" +
                    "Заказ на %s был отменен!</font></h1><br/>" +
                    "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >" +
                    "Причиной является то, что вы не появились в нашем сервисе вовремя.<br/>";

    private static final String RU_LANG = "ru";

    public static String getActivationMessage(Locale locale) {
        if (locale.getLanguage().equals(RU_LANG)) {
            return ACTIVATION_MESSAGE_RU;
        }
        return ACTIVATION_MESSAGE;
    }

    public static String getSuccessfulOrderingMessage(Locale locale) {
        if (locale.getLanguage().equals(RU_LANG)) {
            return SUCCESSFUL_ORDERING_MESSAGE_RU;
        }
        return SUCCESSFUL_ORDERING_MESSAGE;
    }

    public static String getCancelOrderMessage(Locale locale) {
        if (locale.getLanguage().equals(RU_LANG)) {
            return CANCEL_ORDER_MESSAGE_RU;
        }
        return CANCEL_ORDER_MESSAGE;
    }
}
