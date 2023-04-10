package com.example;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PaymentPopup {
    public static void show(float tripCost) {
        // Create a new Stage for the popup
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Payment");
        stage.setMinWidth(350);

        // Create the UI elements for the popup
        String message = String.format("Please confirm your payment of $%.2f", tripCost);
        Label messageLabel = new Label(message);

        Button confirmButton = new Button("Confirm");

        // Set the event handler for the Pay button
        confirmButton.setOnAction(event -> {
            // Perform payment processing logic here
            // ...
            // Close the popup
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Payment Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Your payment has been processed successfully. Your bike has been returned.");
            alert.showAndWait();

            stage.close();

        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(messageLabel, confirmButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
