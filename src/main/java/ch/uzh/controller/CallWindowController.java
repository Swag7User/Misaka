package ch.uzh.controller;


import ch.uzh.helper.Utils;
import ch.uzh.helper.VideoStuff;
import ch.uzh.helper.WebCamService;
import ch.uzh.helper.WebCamView;
import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.futures.BaseFutureAdapter;
//import org.bytedeco.javacpp.opencv_videoio.VideoCapture;
import org.bytedeco.javacpp.opencv_videoio;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgproc.*;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_imgproc.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.video.Video;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;


/*import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc.*;
import org.bytedeco.javacpp.opencv_videoio.VideoCapture;*/
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;



import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;

public class CallWindowController {


	private MainWindowController mainWindowController;
	private Boolean cameraOff;
	private Boolean microphoneMuted;
	private VideoStuff videoUtils;
	private Webcam webcam;






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
	@FXML
	private BorderPane meBorderPane;

	private static volatile Thread playThread;
	private static final double SC16 = (double) 0x7FFF + 0.4999999999999999;
	private WebCamService service ;


	public CallWindowController(MainWindowController mainWindowController) {
		this.mainWindowController = mainWindowController;
		this.videoUtils = new VideoStuff();
		Webcam cam = Webcam.getWebcams().get(0);
		service = new WebCamService(cam);
	}

	@FXML
	public void init() {

		// note this is in init as it **must not** be called on the FX Application Thread:

		Webcam cam = Webcam.getWebcams().get(0);
		service = new WebCamService(cam);
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


			if (service.isRunning()) {
				service.cancel();
			} else {
				service.restart();
			}

			WebCamView view = new WebCamView(service);

			meBorderPane = new BorderPane(view.getView());

			//startCamera();
				/*	try{
						webcam = Webcam.getDefault();
						Dimension[] d = webcam.getViewSizes();

						//mainWindowController.callWindowController.startVideoCall();
						playThread = new Thread(() -> {
							try {
								//FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("C:\\cygwin64\\home\\clip.mp4");
								FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("C:\\cygwin64\\home\\clip.mp4");
								grabber.start();
								AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, true);

								DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
								SourceDataLine soundLine = (SourceDataLine) AudioSystem.getLine(info);
								soundLine.open(audioFormat);
								soundLine.start();

								OpenCVFrameConverter converter = new OpenCVFrameConverter.ToIplImage();
								Java2DFrameConverter paintConverter = new Java2DFrameConverter();

								ExecutorService executor = Executors.newSingleThreadExecutor();

								while (!Thread.interrupted()) {
									Frame frame = grabber.grab();
									if (frame == null) {
										break;
									}
									if (frame.image != null) {
										Image image = SwingFXUtils.toFXImage(paintConverter.convert(frame), null);
										Platform.runLater(() -> {
											meImageView.setImage(image);
										});
									} else if (frame.samples != null) {
										FloatBuffer channelSamplesFloatBuffer = (FloatBuffer) frame.samples[0];
										channelSamplesFloatBuffer.rewind();

										ByteBuffer outBuffer = ByteBuffer.allocate(channelSamplesFloatBuffer.capacity() * 2);

										for (int i = 0; i < channelSamplesFloatBuffer.capacity(); i++) {
											*//**
											 * FloatBuffer is converted to ByteBuffer with some
											 * magic constant SC16 (~Short.MAX_VALUE). I found
											 * it on some forum with this explanation:
											 *
											 * For 16 bit signed to float, divide by 32768. For
											 * float to 16 bit, multiply by 32768.
											 *
											 * Going from float to integer, do the initial
											 * conversion into a container bigger than the
											 * destination container so that it doesn't
											 * accidentally wrap on overs. For instance, on 16
											 * or 24 bit, you can use signed int 32.
											 *
											 * Or alternately, do the clipping on the scaled
											 * float value, before casting into integer. That
											 * way you can save the clipped float direct to a 16
											 * bit container and not have to fool with an
											 * intermediate 32 bit container.
											 *
											 * Clip the float to int results to stay in bounds.
											 * Anything lower than 0x8000 clipped to 0x8000, and
											 * anything higher than 0x7FFFF clipped to 0x7FFFF.
											 *
											 * The advantage of using a factor of 32768 is that
											 * bit patterns will stay the same after conversion.
											 * If you use 32767, the bit patterns will change.
											 * Not much change, but it just doesn't seem elegant
											 * to have them change if it can be avoided.
											 *
											 * If you want to do it as fast as possible it is
											 * just a matter of optimizing the code in whatever
											 * way seems sensible.
											 *//*
											// Could be replaced with: short val = (short) (channelSamplesFloatBuffer.get(i) * Short.MAX_VALUE);
											short val = (short) ((double) channelSamplesFloatBuffer.get(i) * SC16);
											outBuffer.putShort(val);
										}

										*//**
										 * We need this because soundLine.write ignores
										 * interruptions during writing.
										 *//*
										try {
											executor.submit(() -> {
												soundLine.write(outBuffer.array(), 0, outBuffer.capacity());
												outBuffer.clear();
											}).get();
										} catch (InterruptedException interruptedException) {
											Thread.currentThread().interrupt();
										}
									}
								}
								executor.shutdownNow();
								executor.awaitTermination(10, TimeUnit.SECONDS);
								soundLine.stop();
								grabber.stop();
								grabber.release();
								//Platform.exit();
							} catch (Exception exception) {
								exception.printStackTrace();
								System.exit(1);
							}
						});
						playThread.start();
					} catch(Exception e){
						e.printStackTrace();
					}*/
				}
		);





	}



	public void stop() throws Exception {
		playThread.interrupt();
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

	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that realizes the video capture
	private opencv_videoio.VideoCapture capture = new opencv_videoio.VideoCapture();
	// a flag to change the button behavior
	private boolean cameraActive = false;
	// the id of the camera to be used
	private static int cameraId = 0;

	/**
	 * The action triggered by pushing the button on the GUI
	 *
	 *            the push button event
	 */

	public void startCamera()
	{
		System.err.println("FUCKING SHIT BOYYYYY");
		if (!this.cameraActive)
		{
			// start the video capture
			this.capture.open(cameraId);

			// is the video stream available?
			if (this.capture.isOpened())
			{
				this.cameraActive = true;

				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {

					@Override
					public void run()
					{
						// effectively grab and process a single frame
						Mat frame = grabFrame();
						// convert and show the frame
						Image imageToShow = Utils.mat2Image(frame);
						updateImageView(meImageView, imageToShow);
					}
				};

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

				// update the button content
				//this.button.setText("Stop Camera");
			}
			else
			{
				// log the error
				System.err.println("Impossible to open the camera connection...");
			}
		}
		else
		{
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			//this.button.setText("Start Camera");

			// stop the timer
			this.stopAcquisition();
		}
	}

	/**
	 * Get a frame from the opened video stream (if any)
	 *
	 * @return the {@link Mat} to show
	 */
	private Mat grabFrame()
	{
		// init everything
		Mat frame = new Mat();

		// check if the capture is open
		if (this.capture.isOpened())
		{
			try
			{
				// read the current frame
				this.capture.read(frame);

				// if the frame is not empty, process it
				if (!frame.empty())
				{
					cvtColor(frame, frame, COLOR_BGR2GRAY);
				}

			}
			catch (Exception e)
			{
				// log the error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}

		return frame;
	}

	/**
	 * Stop the acquisition from the camera and release all the resources
	 */
	private void stopAcquisition()
	{
		if (this.timer!=null && !this.timer.isShutdown())
		{
			try
			{
				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}

		if (this.capture.isOpened())
		{
			// release the camera
			this.capture.release();
		}
	}

	/**
	 * Update the {@link ImageView} in the JavaFX main thread
	 *
	 * @param view
	 *            the {@link ImageView} to update
	 * @param image
	 *            the {@link Image} to show
	 */
	private void updateImageView(ImageView view, Image image)
	{
		Utils.onFXThread(view.imageProperty(), image);
	}

	/**
	 * On application close, stop the acquisition from the camera
	 */
	protected void setClosed()
	{
		this.stopAcquisition();
	}





}
