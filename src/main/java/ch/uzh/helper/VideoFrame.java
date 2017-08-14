package ch.uzh.helper;

/**
 * Created by jesus on 06.08.2017.
 */
public class VideoFrame {
    private String identifier;
    private byte[] data;

    public VideoFrame(String identifier, byte[] data){
        this.identifier = identifier;
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

}
