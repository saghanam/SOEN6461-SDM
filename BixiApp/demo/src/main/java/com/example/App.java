package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}




// import javafx.application.Application;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.scene.image.Image;
// import javafx.stage.Stage;

// /**
//  *
//  * @author Ramesh Godara
//  */
// public class App extends Application {

//     public void start(Stage stage) throws Exception {

//         Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));

//         Scene scene = new Scene(root);

//         stage.setScene(scene);
//         stage.setTitle("User Login");
//         // stage.getIcons().add(new Image("/asset/icon.png"));
//         stage.show();
//     }

    

//     /**
//      * @param args the command line arguments
//      */
//     public static void main(String[] args) {
//         launch(args);

//     }
// }