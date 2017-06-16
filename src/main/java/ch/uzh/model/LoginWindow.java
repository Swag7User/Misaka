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
import org.mindrot.jbcrypt.BCrypt;

public class LoginWindow extends Application {
    private Stage stage;
    private P2POverlay p2p;

    //change bootstrap ip and bootstrap to it
    public void changeP2P(String ip){
        p2p.shutdown();
        p2p = null;
        p2p = new P2POverlay();
        Pair<Boolean, String> result = p2p.bootstrap(ip);
        if (result.getKey() == false) {
            System.err.println("Aw shit, didn't work\n");
        } else{
            System.err.println("it's AWRIGHT\n");
        }

        System.out.println("Bootstrapped to: " + ip
                + "My IP: " + p2p.getPeerAddress().inetAddress().getHostAddress());

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        // Get parameters TODO: actually use paramaters
        String bootstrapIP = getParameters().getNamed().get("bootstrap");
        bootstrapIP = "192.168.1.15";

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginWindow.fxml"));
        AnchorPane pane = loader.load();
        LoginWindowController loginWindowController = loader.getController();
        loginWindowController.setLoginWindow(this);
        loginWindowController.getP2P();
        Scene scene = new Scene(pane);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/favicon.png")));
        stage.setTitle("Misaka - Login");
        stage.setScene(scene);
        stage.show();
        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.err.println(p2p.getPeerAddress());
        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

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
        System.out.print("byebee~~");
    }

    public Stage getStage() {
        return stage;
    }

    public P2POverlay getP2p(){
        return p2p;
    }

}