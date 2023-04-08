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
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RentController implements Initializable {
    @FXML
    private ChoiceBox<String> stationSelection;

    @FXML
    private Label dockLabel;

    @FXML
    private ChoiceBox<String> dockSelection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dockLabel.setText("Please select the dock \nyou want to unlock");
        dockLabel.setWrapText(true);

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
        } else {
            dockSelection.setDisable(false);
            populateAvailableDocks(selectedStationCode);
        }
    }

    private void selectDock(ActionEvent event) {
        String selectedDockId = dockSelection.getValue();
    }

    private void populateAvailableDocks(String stationCode) {
        clearDockOptions();

        List<Dock> docks = TryCode.getAvailableDocks(stationCode, getDatabaseConnection());

        if (docks != null) {
            List<String> dockIds = docks.stream().map(Dock::getDock_Id).collect(Collectors.toList());
            dockSelection.getItems().addAll(dockIds);
        }
    }

    private void clearDockOptions() {
        dockSelection.getItems().clear();
    }
}
