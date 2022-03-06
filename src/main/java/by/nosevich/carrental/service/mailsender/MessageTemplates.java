package by.nosevich.carrental.service.mailsender;

public class MessageTemplates {
    public static final String ACTIVATION_MESSAGE =
            "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\">Hello, %s!" +
                    "</font></h1><br/><div align=\"center\"><font face=\"Helvetica\" size=\"3\" >" +
                    "We are glad to welcome you to our rental service!<br/>" +
                    "To activate your account follow this link:</font></div> <br/>" +
                    "<h2 align=\"center\"><a href=%s/activate/%s>Link</a></h2>";

    public static final String SUCCESSFUL_ORDERING_MESSAGE =
            "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\"> Order " +
                    "%d was successfully placed!</font></h1><br/>" +
                    "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >The order for car " +
                    "%s has been successfully placed!We are waiting for you " +
                    "%s in our service. If you do not show up, your order will be canceled.<br/>";

    public static final String PICK_UP_ORDER_MESSAGE =
            "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\"> " +
                    "Pick up %s today!</font></h1><br/>" +
                    "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >Pick up the order for car " +
                    "%s.<br> If you do not show up until the end of the day, your order will be canceled.<br/>";

    public static final String CANCEL_ORDER_MESSAGE =
            "<h1 align=\"center\"><font color=\"DeepSkyBlue\" face=\"Helvetica\" size=\"5\">" +
                    " Car %s order has been canceled!</font></h1><br/>" +
                    "<div align=\"center\"><font face=\"Helvetica\" size=\"3\" >" +
                    "The reason is your failure to pick up the car on time.<br/>";
}
