package ch.uzh.model;

import ch.uzh.controller.LoginWindowController;
import ch.uzh.helper.P2POverlay;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class LoginWindow extends Application {
    private Stage stage;
    private P2POverlay p2p;


    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        // Get parameters TODO: actually use paramaters
        String bootstrapIP = getParameters().getNamed().get("bootstrap");
        bootstrapIP = "127.0.0.1";

        p2p = new P2POverlay();

        // Try to bootstrap yay
        Pair<Boolean, String> result = p2p.bootstrap(bootstrapIP);
        if (result.getKey() == false) {
            System.err.println("Aw shit, didn't work\n");
        } else{
            System.err.println("it's AWRIGHT\n");
        }

        System.out.println("Bootstrapped to: " + bootstrapIP
                + "My IP: " + p2p.getPeerAddress().inetAddress().getHostAddress());


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

    /**
     * Application shutdown *
     */
    @Override
    public void stop() {
        shutdown();
    }

    public void shutdown() {

        // Shutdown Tom P2P stuff
        p2p.shutdown();
    }

    public Stage getStage() {
        return stage;
    }

}