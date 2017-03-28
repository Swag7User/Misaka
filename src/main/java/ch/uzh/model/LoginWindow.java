package ch.uzh.model;

import ch.uzh.controller.CallWindowController;
import ch.uzh.controller.LoginWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginWindow extends Application {
    private Stage stage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.err.println("FXML resource: " + getClass().getResource("ch/uzh/model/LoginWindow.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("LoginWindow.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("/LoginWindow.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("/view/LoginWindow.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("/img/favicon.png"));
        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginWindow.fxml"));
        AnchorPane pane = loader.load();
        LoginWindowController loginWindowController = loader.getController();
        loginWindowController.setLoginWindow(this);
        Scene scene = new Scene(pane);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/favicon.png")));
        stage.setTitle("Misaka - Login");
        stage.setScene(scene);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

}