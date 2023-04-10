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
    public static final String NO_AVAILABLE_DOCK_WARNING = "No available dock for the selected station. Please select another station. \n Click on extend to extend time to return bike to another station.";

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
    @FXML
    private Button extendButton;

    private int extendCount = 0;

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

        extendButton.setOnAction(this::extendBike);
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
        extendButton.setDisable(true);
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
                Warning.display(NO_AVAILABLE_DOCK_WARNING);
                if(extendCount == 0){
                    extendButton.setDisable(false);
                }
                extendCount++;
            }
        }
    }

    private void selectDock(ActionEvent event) {
        returnButton.setDisable(dockSelection.getValue() == null);
    }

    // To be completed
    private void returnBike(ActionEvent event) {
        String selectedDockId = dockSelection.getValue();
        UserSession us = UserSession.getInstance(null, null);
        
        float tripCost = TryCode.returnBike(us.getCustomerId(),selectedDockId, getDatabaseConnection());
        System.out.println(tripCost);

        PaymentPopup.show(tripCost);
        returnButton.setDisable(true);
    }

    private void extendBike(ActionEvent event){

        UserSession us = UserSession.getInstance(null, null);
        boolean extendTime = TryCode.extendTime(us.getCustomerId(),getDatabaseConnection());
        System.out.println(extendTime);
        if(extendTime){
            MessageUI.showSuccess("Success! Your task has been completed.");
            extendButton.setDisable(true);
        }else{
            MessageUI.showFailure("Sorry, an error has occurred.");
        }

    }

    private boolean populateAvailableDocks(String stationCode) {
        clearDockOptions();
        hideUnlockCodeComponents();
        clearUnlockCodeData();

        UserSession us = UserSession.getInstance(null, null);
        // int code = TryCode.returnAction(us.getCustomerId(),stationCode,getDatabaseConnection());
        // System.out.println(code);

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
