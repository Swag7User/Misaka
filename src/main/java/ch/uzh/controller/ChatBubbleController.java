package ch.uzh.controller;

import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatBubbleController {

    private static final Logger log = LoggerFactory.getLogger(ChatBubbleController.class);


    @FXML
    private Label senderLbl;
    @FXML
    private Label messageLbl;
    @FXML
    private Label dateTimeLbl;
    @FXML
    private AnchorPane bubbleContainerAnchorPane;
    @FXML
    private Region chatBubbleCorner;

    BackgroundPosition backgroundPositionBottomLeft =
            new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true);
    BackgroundPosition backgroundPositionBottomRight =
            new BackgroundPosition(Side.RIGHT, 0, true, Side.BOTTOM, 0, true);

    public void setMessage(String message) {
        messageLbl.setText(message);
    }

    public void setSender(String sender) {
        senderLbl.setText(sender);
    }

    public void setDateTime() {
        dateTimeLbl.setText(formatDate(new Date()));
    }

    public String formatDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today, " + timeFormatter.format(date);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR)
                && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday, " + timeFormatter.format(date);
        } else {
            DateFormat dateTimeFormatter = new SimpleDateFormat("dd.MM.yy HH:mm");
            return dateTimeFormatter.format(date);
        }
    }


}
