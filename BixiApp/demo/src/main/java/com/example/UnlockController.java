package com.example;

import database.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class UnlockController implements Initializable {
    public static final int CODE_LENGTH = 5;

    @FXML
    private TextField unlockCodeInput;
    @FXML
    private Button unlockButton;
    @FXML
    private Label warningLabel;
    @FXML
    private Label receiptLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addChangeListener();
        unlockButton.setOnAction(this::unlock);
//        unlockCodeInput.setTextFormatter(getFormatter());
//        unlockCodeInput.setOnInputMethodTextChanged(this::enterCode);
    }

    public void enterCode(InputMethodEvent inputMethodEvent) {
        System.out.println(unlockCodeInput.getText());
    }

    private TextFormatter<String> getFormatter() {
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            System.out.println(change.toString());
            if (change.getControlNewText().length() > CODE_LENGTH) {
                return null;
            } else {
                return change;
            }
        });

        return textFormatter;
    }

    private void addChangeListener() {
        unlockCodeInput.textProperty().addListener((observable, oldValue, newValue) -> {
            warningLabel.setVisible(false);
            receiptLabel.setVisible(false);
            System.out.println("New Value: " + newValue + " Old Value: " + newValue.length());
            if (newValue.length() > 5) {
                unlockCodeInput.setText(oldValue);
            } else {
                unlockButton.setDisable(newValue.length() != 5);
            }
        });
    }

    private void unlock(ActionEvent event) {
        String codeInput = unlockCodeInput.getText();
        System.out.println(codeInput);

        if (!codeInput.matches("\\d+")) {
            warningLabel.setVisible(true);
            return;
        }

        Customer currentUser = getCustomer();
        String unlockResult = TryCode.issueBike(currentUser, Integer.parseInt(codeInput), getDatabaseConnection());

        System.out.println(unlockResult);
        if (unlockResult.startsWith("Error")) {
            warningLabel.setText(unlockResult.substring(7));
            warningLabel.setVisible(true);
            return;
        }

        receiptLabel.setText(unlockResult);
        receiptLabel.setVisible(true);
    }

    private Customer getCustomer() {
        return new Customer("CVER2348", "Yong Tang", 22, "yt@gm.com", 1242222633, 0);
    }

    private Connection getDatabaseConnection() {
        return DbConnection.getDatabaseConnection().getConnection();
    }

}
