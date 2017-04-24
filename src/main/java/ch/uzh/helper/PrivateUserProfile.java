/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uzh.helper;


import ch.uzh.model.FriendsListEntry;

import java.io.Serializable;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sstephan
 */
public class PrivateUserProfile implements Serializable {

    private String userID;
    private String password;
    private ArrayList<FriendsListEntry> friendsList;
    private ArrayList<FriendRequestMessage> friendRequestsList;



    public PrivateUserProfile(String _userID, String _password) {
        userID = _userID;
        password = _password;


        // Create new emtpy friendsList
        friendsList = new ArrayList<FriendsListEntry>();

        // Create new empty friendsrequest list
        friendRequestsList = new ArrayList<FriendRequestMessage>();

    }

    public PrivateUserProfile(){

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
