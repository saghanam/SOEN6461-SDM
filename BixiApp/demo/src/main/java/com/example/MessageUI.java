package com.example;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MessageUI {
    public static void showSuccess(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true); // set the alert to be always on top

        alert.showAndWait();
    }

    public static void showFailure(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Failure");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true); // set the alert to be always on top

        alert.showAndWait();
    }
}

