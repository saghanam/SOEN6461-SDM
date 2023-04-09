package com.example;

import database.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RentController implements Initializable {
    public static final String SELECT_STATION = "Please select the dock \nyou want to unlock";
    public static final String REMEMBER_CODE = "Please remember the code and use it within 5 minutes";

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
        String selectedStationCode = stationSelection.getValue();

        if (selectedStationCode == null) {
            dockSelection.setDisable(true);
            clearDockOptions();
            hideUnlockCodeComponents();
            clearUnlockCodeData();
        } else {
            dockSelection.setDisable(false);
            populateAvailableDocks(selectedStationCode);
        }
    }

    private void selectDock(ActionEvent event) {
        String selectedDockId = dockSelection.getValue();

        Optional<Dock> selectedDock = availableDocks.stream().filter(dock -> dock.getDock_Id().equals(selectedDockId)).findFirst();
        selectedDock.ifPresent(
                dock -> {
                    Optional<UnlockCode> unlockCode = Optional.ofNullable(TryCode.issueUnlockCode(dock, getDatabaseConnection()));
                    unlockCode.ifPresent(
                            code -> {
                                unlockCodeLabel.setText(REMEMBER_CODE);
                                unlockCodeLabel.setVisible(true);

                                unlockCodeText.setText(String.valueOf(code.getUnlock_Code()));
                                unlockCodeText.setVisible(true);
                            }
                    );
                }
        );
    }

    private void populateAvailableDocks(String stationCode) {
        clearDockOptions();
        hideUnlockCodeComponents();
        clearUnlockCodeData();

        List<Dock> docks = TryCode.getAvailableDocksForRent(stationCode, getDatabaseConnection());

        if (docks != null) {
            List<String> dockIds = docks.stream().map(Dock::getDock_Id).collect(Collectors.toList());
            dockSelection.getItems().addAll(dockIds);
            availableDocks.addAll(docks);
        }
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