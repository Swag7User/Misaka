package ch.uzh.model;

import ch.uzh.controller.LoginWindowController;
import ch.uzh.helper.P2POverlay;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginWindow extends Application {

    private static final Logger log = LoggerFactory.getLogger(LoginWindow.class);

    private Stage stage;
    private P2POverlay p2p;

    //change bootstrap ip and bootstrap to it
    public void changeP2P(String ip) {
        p2p.shutdown();
        p2p = null;
        p2p = new P2POverlay();
        Pair<Boolean, String> result = p2p.bootstrap(ip);
        if (result.getKey() == false) {
            log.info("Aw shit, didn't work\n");
        } else {
            log.info("it's AWRIGHT\n");
        }

        log.info("Bootstrapped to: " + ip
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
            log.info("Aw shit, didn't work\n");
        } else {
            log.info("it's AWRIGHT\n");
        }

        log.info("Bootstrapped to: " + bootstrapIP
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
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        log.info(p2p.getPeerAddress().toString());
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

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
        log.info("byebee~~");
    }

    public Stage getStage() {
        return stage;
    }

    public P2POverlay getP2p() {
        return p2p;
    }

}