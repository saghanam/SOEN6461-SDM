package com.example;

import database.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ReturnController implements Initializable {
    public static final String SELECT_STATION = "Please select the dock \nyou want to unlock";
    public static final String NO_AVAILABLE_DOCK_WARNING = "No available dock for the selected station\nPlease select another station";

    @FXML
    private ChoiceBox<String> stationSelection;
    @FXML
    private Label dockLabel;
    @FXML
    private ChoiceBox<String> dockSelection;
    @FXML
    private Label unlockCodeLabel;
    @FXML
    private Label unlockCodeText;
    @FXML
    private Button returnButton;

    private List<Dock> availableDocks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        availableDocks = new ArrayList<>();

        dockLabel.setText(SELECT_STATION);
        dockLabel.setWrapText(true);
        hideUnlockCodeComponents();

        stationSelection.getItems().addAll(generateStationOptions());
        stationSelection.setOnAction(this::selectStation);

        dockSelection.setOnAction(this::selectDock);

        returnButton.setOnAction(this::returnBike);
    }

    private Connection getDatabaseConnection() {
        return DbConnection.getDatabaseConnection().getConnection();
    }

    private List<String> generateStationOptions() {
        Connection connection = getDatabaseConnection();

        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT DISTINCT station_code FROM stations";
            ResultSet resultSet = statement.executeQuery(sql);

            List<String> stationCodes = new ArrayList<>();
            while (resultSet.next()) {
                stationCodes.add(resultSet.getString(1));
            }

            return stationCodes;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void selectStation(ActionEvent event) {
        returnButton.setDisable(true);
        String selectedStationCode = stationSelection.getValue();

        if (selectedStationCode == null) {
            dockSelection.setDisable(true);
            clearDockOptions();
            hideUnlockCodeComponents();
            clearUnlockCodeData();
        } else {
            boolean isPopulated = populateAvailableDocks(selectedStationCode);

            dockSelection.setDisable(!isPopulated);
            if (!isPopulated) {
                unlockCodeText.setText(NO_AVAILABLE_DOCK_WARNING);
                unlockCodeText.setVisible(true);
            }
        }
    }

    private void selectDock(ActionEvent event) {
        returnButton.setDisable(dockSelection.getValue() == null);
    }

    // To be completed
    private void returnBike(ActionEvent event) {
        String selectedDockId = dockSelection.getValue();
    }

    private boolean populateAvailableDocks(String stationCode) {
        clearDockOptions();
        hideUnlockCodeComponents();
        clearUnlockCodeData();

        List<Dock> docks = TryCode.getAvailableDocksForReturn(stationCode, getDatabaseConnection());

        if (docks != null && !docks.isEmpty()) {
            List<String> dockIds = docks.stream().map(Dock::getDock_Id).collect(Collectors.toList());
            dockSelection.getItems().addAll(dockIds);
            availableDocks.addAll(docks);
            return true;
        }

        return false;
    }

    private void clearDockOptions() {
        dockSelection.getItems().clear();
    }

    private void hideUnlockCodeComponents() {
        unlockCodeText.setVisible(false);
        unlockCodeLabel.setVisible(false);
    }

    private void clearUnlockCodeData() {
        availableDocks.clear();
    }
}