package ch.uzh.controller;


import ch.uzh.helper.VideoStuff;
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

public class CallWindowController {

	private Logger log = LoggerFactory.getLogger(getClass());

	private MainWindowController mainWindowController;
	private Boolean cameraOff;
	private Boolean microphoneMuted;
	private VideoStuff videoUtils;



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


	public CallWindowController(MainWindowController mainWindowController) {
		this.mainWindowController = mainWindowController;
		this.videoUtils = new VideoStuff();
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





}
