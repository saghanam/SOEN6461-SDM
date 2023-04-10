package com.example;

import database.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;

import java.net.URL;
import java.sql.Connection;
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
        receiptLabel.setVisible(false);

        String codeInput = unlockCodeInput.getText();

        if (!codeInput.matches("\\d+")) {
            warningLabel.setVisible(true);
            return;
        }

        UserSession us = UserSession.getInstance(null, null);
        System.out.println(us.getCustomerId() + ", " + us.getUserName());
        String unlockResult = TryCode.issueBike(us.getCustomerId(), us.getUserName(),  Integer.parseInt(codeInput), getDatabaseConnection());

        if (unlockResult.startsWith("Error")) {
            warningLabel.setText(unlockResult.substring(7));
            warningLabel.setVisible(true);
            return;
        }

        showReceiptPopup(unlockResult);
    }

    private void showReceiptPopup(String body) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Receipt");
        alert.setHeaderText(null);
        alert.setContentText(body);

        alert.showAndWait();
    }

    private Connection getDatabaseConnection() {
        return DbConnection.getDatabaseConnection().getConnection();
    }

}
