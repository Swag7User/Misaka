package ch.uzh.misaka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginWindow extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.err.println("FXML resource: " + getClass().getResource("ch/uzh/misaka/sample.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("sample.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("/sample.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("/view/sample.fxml"));
        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Parent root = FXMLLoader.load(getClass().getResource("/view/sample.fxml"));
        primaryStage.setTitle("Misaka - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}