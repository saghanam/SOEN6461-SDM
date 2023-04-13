package com.example;

import database.DbConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author Saghana Mahesh Sarma
 */
public class LoginController implements Initializable {

    private final Connection con;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Button loginButton;

    Window window;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public LoginController() {
        DbConnection dbc = DbConnection.getDatabaseConnection();
        con = dbc.getConnection();
        System.out.println(con);
    }

    @FXML
    private void login() throws Exception {

        if (this.isValidated()) {

            Customer result;

            try {
                result = TripController.login(username.getText(),password.getText(),con);
                System.out.println(result);

                if (result != null) {
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.close();

                    Parent root = FXMLLoader.load(getClass().getResource("MainPanelView.fxml"));
                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.setTitle("Admin Panel");
                    stage.getIcons().add(ResourcesManager.getTitleIcon());
                    stage.show();

                } else {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                            "Invalid username and password.");
                    username.requestFocus();
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    private boolean isValidated() {

        window = loginButton.getScene().getWindow();
        if (username.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Username text field cannot be blank.");
            username.requestFocus();
        } else if (username.getText().length() < 5 || username.getText().length() > 25) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Username text field cannot be less than 5 and greator than 25 characters.");
            username.requestFocus();
        } else if (password.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Password text field cannot be blank.");
            password.requestFocus();
        } else if (password.getText().length() < 5 || password.getText().length() > 25) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Password text field cannot be less than 5 and greator than 25 characters.");
            password.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    @FXML
    private void showRegisterStage() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("RegisterView.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("User Registration");
        stage.getIcons().add(ResourcesManager.getTitleIcon());
        stage.show();
    }
}