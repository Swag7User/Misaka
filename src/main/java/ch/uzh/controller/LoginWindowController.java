package ch.uzh.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;

import ch.uzh.model.Client;
import ch.uzh.model.LoginWindow;
import ch.uzh.model.MainWindow;
import ch.uzh.model.UserInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ch.uzh.helper.Password;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.futures.BaseFutureListener;
import org.mindrot.jbcrypt.BCrypt;

import javax.sound.sampled.LineUnavailableException;

/**
 * Created by jesus on 11.03.2017.
 */
public class LoginWindowController implements Observer{

    private final String StartUpNode = "startupClient";
    private LoginWindow loginWindow;
    private String username;
    private String password;
    private String insecurePassword;

    private int clientId;
    private String clientIP;


    @FXML
    private Button loginButton;
    @FXML
    private Button dbg;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        System.err.println("LoginWindowController is initializing");
        usernameField.setText("Test_user_42");
        passwordField.setText("1234qwertyuniqueshit");

        dbg.setOnAction((event) -> {
                    System.err.println("CLICK CLICK CLICK");
                    if (loginCheck() == false){
                        return;
                    }
                    else{
                        logindbg();
                    }
                }
        );

        loginButton.setOnAction((event) -> {
                    System.err.println("CLICK ");
                    if (loginCheck() == false){
                        return;
                    }
                    else{
                        int id = getId();
                        login(id, null);
                    }
                }
        );
    }

    public void setLoginWindow(LoginWindow loginWindow) {
        this.loginWindow = loginWindow;
    }

    public void alive() {
        System.err.println("LoginWindowController is here");
    }

    public void login(final int id, final String ip) {
        try {
            username = usernameField.getText();
            password = Password.hashPassword(passwordField.getText());
            System.err.println("username:" + username);
            System.err.println("hashed password:" + password);
            this.clientIP = ip;
            this.clientId = id;

            if (ip != null) {
                // start a new node and check if the user already exists with the observer
                // update() method
                new Client(getId(), ip, StartUpNode, "", false, this);
            } else {
                // ip null means bootstrap node, no user check needed
                final String ip2 = InetAddress.getLocalHost().getHostAddress();
            //    MainWindow mainWindow = new MainWindow();
            //    mainWindow.start(loginWindow.getStage(), id, ip2, username, password, true);
            }


        } catch (Exception e) {
            System.err.println("Caught Exception: " + e.getMessage());
            e.printStackTrace();
        }
//        try {
//            MainWindow mainWindow = new MainWindow();
//            mainWindow.draw(loginWindow.getStage());
//
//        } catch (Exception e) {
//            System.err.println("Caught Exception: " + e.getMessage());
//            e.printStackTrace();
//
//        }
    }


    /**
     * Login function, loads next window
     */
    public void logindbg() {
        MainWindow mainWindow = new MainWindow();
        try {
            username = usernameField.getText();
            insecurePassword = passwordField.getText();
            password = Password.hashPassword(passwordField.getText());
            System.err.println("username:" + username);
            System.err.println("hashed password:" + password);
            mainWindow.draw(loginWindow.getStage(),1,null,"debug","123", false);
        } catch (Exception e) {
            System.err.println("Caught Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * checks for valid login password
     * returns false if password is invalid
     * returns true if password is OK
     * @return bool if login is ok or not
     */
    public boolean loginCheck() {
        if (usernameField.getText().length() < 3){
            errorLabel.setText("Username too short, at least 3 characters");
            return false;
        }
        if (Password.checkPassword(passwordField.getText()) == false) {
            errorLabel.setText("Password too short, please chose at least 10 characters");
            return false;
        }
        try {
            if (Password.passwordContainsTop(passwordField.getText()) == false) {
                errorLabel.setText("Password too common, please chose another one");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Aw shit, password check borkered");
        }
        return true;
    }

    private int getId() {
        int id = ((Long) System.currentTimeMillis()).intValue();
        return id;
    }

    public void update(Observable o, Object arg) {
        Client node = (Client) arg;
        final long time = System.currentTimeMillis();
        BaseFutureListener<FutureGet> userExistsListener = new BaseFutureListener<FutureGet>() {
            @Override
            public void operationComplete(FutureGet futureGet) throws ClassNotFoundException, IOException, LineUnavailableException, InterruptedException {
                if (futureGet.isSuccess()) {
                    if (futureGet != null && futureGet.data() != null) {
                        if (futureGet.data().object() instanceof UserInfo) {
                            UserInfo user = (UserInfo) futureGet.data().object();
                            // user already registered
                            if (BCrypt.checkpw(insecurePassword, user.getPassword())) { // bcrypt pw check
                                shutdownNode(node);
                                futureGet.removeListener(this);
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        try {
                                            MainWindow mainWindow = new MainWindow();
                                            mainWindow.draw(loginWindow.getStage(),clientId, clientIP, username, password, false);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.err.println("Aw shit, login fucked up. Volvo, pls fix.");
                                        }
                                    }
                                });
                            } else { //user exists but pw is wrong
                                Platform.runLater(new Runnable() { //TODO: maybe use task instead of runLater
                                    public void run() {
                                        errorLabel.setText("Wrong userName/Password");
                                    }
                                });
                            }
                        }
                    } else {
                        // FutureGet was successful, but user does not yet exist
                        shutdownNode(node); //fuck the temporary login node, he gonna die
                        futureGet.removeListener(this);
                        Platform.runLater(new Runnable() {
                            public void run() {
                                try {
                                    MainWindow mainWindow = new MainWindow();
                                    mainWindow.draw(loginWindow.getStage(), clientId, clientIP, username, password, false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.err.println("Aw shit, login fucked up. Volvo, pls fix 2.");
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void exceptionCaught(Throwable t) throws Exception {}
        };
        // TODO: CHECK THIS OUT OMG
//        try {
//            LoginHelper.retrieveUserInfo(username, node, userExistsListener);
//        } catch (Exception e) {
//            loginNotPossibleExceptionHandler();
//        }
    }

    protected void shutdownNode(Client node) {
        if (node != null) {
            node.shutdown();
        }
    }


}


