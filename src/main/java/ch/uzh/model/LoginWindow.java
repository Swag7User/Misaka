package ch.uzh.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginWindow extends Application {
    private Stage stage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.err.println("FXML resource: " + getClass().getResource("ch/uzh/model/LoginWindow.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("LoginWindow.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("/LoginWindow.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("/view/LoginWindow.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("/img/favicon.png"));
        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginWindow.fxml"));
        //LoginWindowController
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/favicon.png")));
        stage.setTitle("Misaka - Login");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

}