package ch.uzh.helper;

/**
 * Created by Jesus on 10.06.2017.
 */
public class EncryptedPrivateUserProfile {
    private byte[] encryptedProfile;


    public EncryptedPrivateUserProfile(byte[] encryptedProfile) {
        this.encryptedProfile = encryptedProfile;

    }

    public byte[] getEncryptedProfile() {
        return encryptedProfile;
    }

    public void setEncryptedProfile(byte[] encryptedProfile) {
        this.encryptedProfile = encryptedProfile;
    }

}
