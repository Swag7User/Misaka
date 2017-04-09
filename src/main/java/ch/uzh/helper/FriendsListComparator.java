/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.uzh.helper;

import ch.uzh.model.FriendsListEntry;

import java.util.Comparator;

/**
 *
 * @author sstephan
 */
public class FriendsListComparator implements Comparator<FriendsListEntry> {

    @Override
    public int compare(FriendsListEntry o1, FriendsListEntry o2) {
        int returnVal = 0;
        if (o1.isOnline() && !o2.isOnline()) {
            return -1;
        } else if (!o1.isOnline() && o2.isOnline()) {
            return 1;
        } else {
            return o1.getUserID().compareTo(o2.getUserID());
        }
    }
    
}
