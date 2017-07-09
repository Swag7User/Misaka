package ch.uzh.helper;


import ch.uzh.model.FriendsListEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PrivateUserProfile implements Serializable {

    private String userID;
    private String password;
    private byte[] privateKeySerialized;
    private ArrayList<FriendsListEntry> friendsList;
    private ArrayList<FriendRequestMessage> friendRequestsList;


    public PrivateUserProfile(String _userID, String _password, byte[] _privateKeySerialized) {
        userID = _userID;
        password = _password;
        privateKeySerialized = _privateKeySerialized;


        // Create new emtpy friendsList
        friendsList = new ArrayList<FriendsListEntry>();

        // Create new empty friendsrequest list
        friendRequestsList = new ArrayList<FriendRequestMessage>();

    }

    public PrivateUserProfile() {

    }


    /**
     * @return the userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the keyPair
     */

    public boolean isFriendsWith(String s) {
        for (FriendsListEntry e : friendsList) {
            if (e.getUserID().equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the friendsList
     */
    public List<FriendsListEntry> getFriendsList() {
        return friendsList;
    }

    /**
     * @return the friendRequestsList
     */
    public List<FriendRequestMessage> getFriendRequestsList() {
        return friendRequestsList;
    }


}
