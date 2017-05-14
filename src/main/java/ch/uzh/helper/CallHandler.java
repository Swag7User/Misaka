/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uzh.helper;

import com.google.gson.Gson;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import ch.uzh.model.MainWindow;
import ch.uzh.model.FriendsListEntry;
import ch.uzh.helper.AudioFrame;
import net.tomp2p.audiovideowrapper.Opus;
import net.tomp2p.audiovideowrapper.OpusWrapper;

import javax.sound.sampled.*;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CallHandler {

    static int BITRATE = 12000;
    static int BIT_DEPTH = 16;
    public static int FRAME_LENGTH = 20;
    static int FRAME_SIZE = (BITRATE * BIT_DEPTH * FRAME_LENGTH) / (1000 * 8);
    public static int MAX_PLAY_BUFFER_SIZE = 10;

    private final BlockingQueue<byte[]> incomingAudioBuffer = new LinkedBlockingQueue<>();

    private boolean running = false;
    private Thread recordThread;
    private Thread playThread;

    private TargetDataLine microphone;
    private SourceDataLine speaker;

    private IntBuffer error;
    private PointerByReference opusEncoder;
    private PointerByReference opusDecoder;

    private P2POverlay p2p;
    private FriendsListEntry friend;
    private MainWindow mainApp;

    public CallHandler(MainWindow _mainApp, P2POverlay _p2p, FriendsListEntry _friend) {
        mainApp = _mainApp;
        p2p = _p2p;
        friend = _friend;
        FRAME_SIZE = (BITRATE * BIT_DEPTH * FRAME_LENGTH) / (1000 * 8);
    }

    // Load Opus Library
    static {
        try {
            System.loadLibrary("opus");
        } catch (UnsatisfiedLinkError e1) {
            try {
                File f = Native.extractFromResourcePath("opus");
                System.load(f.getAbsolutePath());
            } catch (Exception e2) {
                e1.printStackTrace();
                e2.printStackTrace();
            }
        }
    }

    public void addAudioFrame(byte[] audioFrame) {
        try {
            incomingAudioBuffer.put(audioFrame);
        } catch (InterruptedException ex) {
            System.out.println("InterruptedException");
        }
    }
    

    public void stop() {
        running = false;
    }

    public void start() throws LineUnavailableException {
        final AudioFormat format = new AudioFormat(BITRATE, BIT_DEPTH, 1, true, true);

        // Microphone line
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("not supported");
        }
        microphone = AudioSystem.getTargetDataLine(format);
        microphone.open(format,FRAME_SIZE*4);
        microphone.start();

        // Playback line
        speaker = AudioSystem.getSourceDataLine(format);
        speaker.open(format);
        speaker.start();
        speaker.flush();
        error = IntBuffer.allocate(4);
        opusEncoder = Opus.INSTANCE.opus_encoder_create(BITRATE, 1,
                Opus.OPUS_APPLICATION_RESTRICTED_LOWDELAY, error);
        opusDecoder = Opus.INSTANCE.opus_decoder_create(BITRATE, 1, error);

        // Thread that records audio
        class RecordThread implements Runnable {

            @Override
            public void run() {
                microphone.flush();
                
                while (running) {
                    try {
                        byte dataFromMic[] = recordFromMicrophone(format);
                        if (dataFromMic != null) {
                            AudioFrame frame = new AudioFrame("AudioFrame", p2p.getPeerAddress(), mainApp.getUserID(), dataFromMic);
                            Gson gsonAudioframe = new Gson();
                            String jsonAudioframe = gsonAudioframe.toJson(frame);
                            System.err.println("Sending audioframe: " + jsonAudioframe );
                            System.err.println("to buddy: " + friend.getPeerAddress() );
                            p2p.sendNonBlocking(friend.getPeerAddress(), jsonAudioframe, true);
                        }

                    } catch (LineUnavailableException e) {
                        System.out.println("Exception in RecordThread");
                        e.printStackTrace();
                    }

                }
                microphone.close();
                System.out.println("End of recording");
            }
        }

        // Thread that plays audio
        class PlayThread implements Runnable {

            @Override
            public void run() {
                while (running) {
                    try {
                        // If queue is to long, discard to minimize delay
                        int size = incomingAudioBuffer.size();
                        if (size >= 10) {
                            incomingAudioBuffer.clear();
                            System.out.println("Deleted from Speakerbuffer " + size);
                            continue;
                        }

                        // Wait for something in the queue and grab it
                        byte packet[] = incomingAudioBuffer.take();
                        ShortBuffer decodedAudio = decode(packet);

                        // Grab up to 10 packets if available
                        int packetsDecoded = 1;
                        while (!incomingAudioBuffer.isEmpty() 
                                && decodedAudio.remaining() >= FRAME_SIZE/2
                                && packetsDecoded < 10) {
                            packet = incomingAudioBuffer.poll();
                            ShortBuffer decodedPacket = decode(packet);
                            decodedPacket.flip();
                            decodedAudio.put(decodedPacket);
                            packetsDecoded++;

                        }

                        // Play back
                        decodedAudio.flip();
                        playBack(format, decodedAudio);

                    } catch (LineUnavailableException | InterruptedException e) {
                        System.out.println("Exception in PlayThread");
                    }
                }
                speaker.close();
                System.out.println("End of playing");
            }
        }

        // Start threads
        recordThread = new Thread(new PlayThread());
        playThread = new Thread(new RecordThread());

        running = true;

        recordThread.start();
        playThread.start();

    }

    private ShortBuffer decode(byte packet[]) {
        ShortBuffer shortBuffer = ShortBuffer.allocate(FRAME_SIZE * 10);

        int decoded = Opus.INSTANCE.opus_decode(opusDecoder, packet, packet.length,
                shortBuffer, FRAME_SIZE, 0);
        shortBuffer.position(shortBuffer.position() + decoded);
        return shortBuffer;
    }

    private void playBack(AudioFormat format, ShortBuffer shortBuffer) throws LineUnavailableException {
        // Fill short array with buffer's data
        short[] shortAudioBuffer = new short[shortBuffer.remaining()];
        shortBuffer.get(shortAudioBuffer);
        
        byte[] audio = ShortToByte_Twiddle_Method(shortAudioBuffer);
        
        // If speaker is clogged, flush
        if (speaker.available() == 0) {
            speaker.flush();
            System.out.println("Flushed data from Speaker");
        }
        
        // Write to line
        speaker.write(audio, 0, audio.length);
    }

    private byte[] recordFromMicrophone(AudioFormat format)
            throws LineUnavailableException {
        byte[] data = new byte[FRAME_SIZE];
        ShortBuffer shortBuffer = ShortBuffer.allocate(FRAME_SIZE / 2);
        int numBytesRead;

        numBytesRead = microphone.read(data, 0, data.length);
        assert (numBytesRead == FRAME_SIZE);

        // Save this chunk of data.
        for (int i = 0; i < numBytesRead; i += 2) {
            int b1 = data[i + 1] & 0xff;
            int b2 = data[i] << 8;
            shortBuffer.put((short) (b1 | b2));
        }
        shortBuffer.flip();

        // Encoding
        int read;
        ByteBuffer dataBuffer = ByteBuffer.allocate(FRAME_SIZE);
        int toRead = shortBuffer.capacity();
        read = Opus.INSTANCE.opus_encode(opusEncoder, shortBuffer, FRAME_SIZE / 2, dataBuffer, toRead);
        dataBuffer.position(dataBuffer.position() + read);
        dataBuffer.flip();

        byte packet[] = new byte[read];
        dataBuffer.get(packet);
        dataBuffer.flip();

        return packet;

    }

    private byte[] ShortToByte_Twiddle_Method(final short[] input) {
        final int len = input.length;
        final byte[] buffer = new byte[len * 2];
        for (int i = 0; i < len; i++) {
            buffer[(i * 2) + 1] = (byte) (input[i]);
            buffer[(i * 2)] = (byte) (input[i] >> 8);
        }
        return buffer;
    }

    public byte[] concatArrays(byte[] a, byte[] b) {
        int length = a.length + b.length;
        byte[] result = new byte[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

}
