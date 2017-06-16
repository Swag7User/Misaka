package ch.uzh.controller;


import ch.uzh.helper.AudioFrame;
import ch.uzh.helper.CallHandler;
import ch.uzh.helper.P2POverlay;
import ch.uzh.helper.VideoStuff;
import ch.uzh.model.FriendsListEntry;
import ch.uzh.model.MainWindow;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.futures.BaseFutureAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.logging.Level;

public class CallWindowController {

	private Logger log = LoggerFactory.getLogger(getClass());

	private MainWindow mainWindow;
	private MainWindowController mainWindowController;
	private Boolean cameraOff;
	private Boolean microphoneMuted;
	private VideoStuff videoUtils;
	private P2POverlay p2p;
	private FriendsListEntry friendsListEntry;
	private CallHandler callHandler;
	private boolean isIncomingCall;



	@FXML
	public HBox btnWrapperVideo;
	@FXML
	private Button muteMicrophoneBtn;
	@FXML
	private Button muteVideoBtn;
	@FXML
	private Label microphoneLbl;
	@FXML
	private Label cameraLbl;
	@FXML
	private HBox videoUserWrapper;
	@FXML
	private ImageView videoUser1;
	@FXML
	private ImageView meImageView;
	@FXML
	private Button hideMyselfBtn;
	@FXML
	private Button endCallBtn;


	public CallWindowController(MainWindowController mainWindowController, P2POverlay p2p, MainWindow mainWindow) {
		this.mainWindowController = mainWindowController;
		this.videoUtils = new VideoStuff();
		this.p2p = p2p;
		this.mainWindow = mainWindow;
	}

	public void setFriendsListEntry(FriendsListEntry friendsListEntry){
		this.friendsListEntry = friendsListEntry;
	}

	@FXML
	private void initialize() {
		System.err.println("CallWindowController is initializing");

		endCallBtn.setOnAction((event) -> {
					System.err.println("CLICK END call btn");
					mainWindowController.drawMsgPane();
				}
		);

		muteMicrophoneBtn.setOnAction((event) -> {
					System.err.println("CLICK PANIC! btn");
					try{
						mainWindowController.callWindowController.startVideoCall();

					} catch(Exception e){
						e.printStackTrace();
					}
				}
		);
	}

	public void startTransmitting() {
		// Create new call
		callHandler = new CallHandler(mainWindow, p2p, getFriendsListEntry());
		try {
			callHandler.start();
		} catch (LineUnavailableException ex) {
			stopTransmitting();
			System.out.println("LineUnavailableException");
		}
	}

	public void stopTransmitting() {
		if (callHandler == null) {
			return;
		}
		callHandler.stop();
		try {
			Thread.sleep(100);
		} catch (InterruptedException ex) {
			java.util.logging.Logger.getLogger(CallWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
		callHandler = null;
	}

	public void handleIncomingAudioFrame(AudioFrame msg) {
		callHandler.addAudioFrame(msg.getData());
	}

	public void startVideoHandler()
			throws ClassNotFoundException, IOException, LineUnavailableException {
		cameraOff = false;
		Image image = new Image(getClass().getResourceAsStream("/camera.png"));
		cameraLbl.setGraphic(new ImageView(image));
		muteVideoBtn.setText("Turn camera off");
		microphoneMuted = false;
		Image imageMicrophone = new Image(getClass().getResourceAsStream("/microphone.png"));
		microphoneLbl.setGraphic(new ImageView(imageMicrophone));
		muteMicrophoneBtn.setText("Mute microphone");
		hideMyselfBtn.setText("Hide myself");

//		if (audioUtils == null) {
//			audioUtils = new AudioHelper(node, node.getUser());
//		}

	//	videoUtils = new VideoStuff(node, node.getUser());

//		for (String chatPartner : mainWindowController.currentChatPartners) {
//			VideoRequest request = new VideoRequest(RequestType.SEND, RequestStatus.WAITING,
//					node.getUser().getFriend(chatPartner).getPeerAddress(), chatPartner,
//					node.getUser().getUsername());
//			RequestHandler.handleRequest(request, node, new BaseFutureAdapter<FuturePut>() {
//
//				@Override
//				public void operationComplete(FuturePut futurePut) throws Exception {
//					if (futurePut.isSuccess()) {
//						Platform.runLater(new Runnable() {
//
//							@Override
//							public void run() {
//								Image offline =
//										new Image(getClass().getResourceAsStream("/offline.png"));
//								videoUser1.setImage(offline);
//							}
//
//						});
//					}
//				}
//
//			});
//			videoUtils.addReceiver(node.getUser().getFriend(chatPartner));
//			audioUtils.addReceiver(node.getUser().getFriend(chatPartner));
//		}

	}

	public void startVideoCall() throws LineUnavailableException, IOException {
		//videoUtils.setPartnerImageView(videoUser1);
		if (!videoUtils.videoIsRunning()) {
			videoUtils.startVideo(meImageView);
		}
	}

	public FriendsListEntry getFriendsListEntry() {
		return friendsListEntry;
	}





}
