package com.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UnlockController implements Initializable {
    public static final String SELECT_STATION = "Please select the dock \nyou want to unlock";
    public static final String REMEMBER_CODE = "Please remember the code and use it within 5 minutes";
    public static final int CODE_LENGTH = 5;

    @FXML
    private ChoiceBox<String> stationSelection;
    @FXML
    private Label dockLabel;
    @FXML
    private ChoiceBox<String> dockSelection;
    @FXML
    private Label unlockCodeLabel;
    @FXML
    private TextField unlockCodeInput;

    private List<Dock> availableDocks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInputMaxLength(CODE_LENGTH);
        unlockCodeInput.setTextFormatter(getFormatter());
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

    private void setInputMaxLength(int maxLength) {
        unlockCodeInput.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            if (newValue.length() > maxLength) {
                unlockCodeInput.setText(oldValue);
            }
        });
    }

}
