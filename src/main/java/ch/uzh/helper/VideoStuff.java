package ch.uzh.helper;


import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamLockException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import net.tomp2p.audiovideowrapper.H264Wrapper;
import net.tomp2p.audiovideowrapper.VideoData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoStuff {
	private static Logger log = LoggerFactory.getLogger(VideoStuff.class);

	private boolean running;
	private boolean mute;
	private User sender;

	private Webcam webcam;
	private VideoData frameVideo;
	private ImageView IMG;
	private static ImageView partnerImageView;
	private static boolean isPlaying = false;


	public void startVideo(ImageView imageView) throws LineUnavailableException, IOException {
		running = true;
		mute = false;
		IMG = imageView;

		webcam = Webcam.getDefault();
		Dimension[] d = webcam.getViewSizes();
		webcam.setViewSize(d[d.length - 1]);
		frameVideo = H264Wrapper.decodeAndPlay(IMG);

		try {
			H264Wrapper.recordAndEncode(webcam, frameVideo);


			List<byte[]> byteBufferList = new ArrayList<byte[]>();
			while (running) {
				log.debug(IMG + "");
				log.debug(IMG != null ? IMG.getImage() + "" : "");
				if (IMG != null && IMG.getImage() != null) {
					javafx.scene.image.Image image = IMG.getImage();
					BufferedImage bImage = new BufferedImage((int) image.getWidth(),
							(int) image.getHeight(), BufferedImage.TYPE_INT_ARGB);
					SwingFXUtils.fromFXImage(IMG.getImage(), bImage);
					ByteArrayOutputStream s = new ByteArrayOutputStream();
					ImageIO.write(bImage, "png", s);
					byte[] res = s.toByteArray();
					s.close();
					byteBufferList.add(res);
					if (byteBufferList.size() >= 10) {
	//					sendVideoData(byteBufferList);
						byteBufferList = new ArrayList<byte[]>();
					}
				}
			}
		} catch (WebcamLockException e) {
			log.warn("Webcam HD WebCam 0 has already been locked");
		}
	}

	public static void playVideo(List<byte[]> byteArray) throws IOException {
		log.info("Play Video");
		log.info(byteArray.size() + "");

		while (isPlaying == true) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException ex) {
			}
		}
		isPlaying = true;
		for (byte[] image : byteArray) {
			ByteArrayInputStream bais = new ByteArrayInputStream(image);
			BufferedImage bf = ImageIO.read(bais);

			WritableImage wr = null;
			if (bf != null) {
				wr = new WritableImage(bf.getWidth(), bf.getHeight());
				PixelWriter pw = wr.getPixelWriter();
				for (int x = 0; x < bf.getWidth(); x++) {
					for (int y = 0; y < bf.getHeight(); y++) {
						pw.setArgb(x, y, bf.getRGB(x, y));
					}
				}
			}
			partnerImageView.setImage(wr);
			try {
				Thread.sleep(9);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			if (byteArray.get(byteArray.size() - 1) == image) {
				isPlaying = false;
			}
		}
	}

	public void endVideo() throws ClassNotFoundException, IOException, LineUnavailableException {
		if (running) {
			running = false;
//			for (Friend receiver : receiverList) {
//				VideoRequest request = new VideoRequest(RequestType.SEND, RequestStatus.ABORTED,
//						receiver.getPeerAddress(), receiver.getName(), sender.getUsername());
//				RequestHandler.handleRequest(request, node);
//			}
		}
	}

	public boolean videoIsRunning() {
		return running;
	}

	public void mute() {
		mute = true;
	}

	public void unmute() {
		mute = false;
	}


	public void setPartnerImageView(ImageView partnerImageView) {
		this.partnerImageView = partnerImageView;
	}


}
