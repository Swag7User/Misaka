package ch.uzh.misaka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
        System.err.println("FXML resource: " + getClass().getResource("ch/uzh/misaka/sample.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("sample.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("/sample.fxml"));
        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
