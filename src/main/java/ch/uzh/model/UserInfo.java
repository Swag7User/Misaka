package ch.uzh.model;

import net.tomp2p.peers.PeerAddress;

import java.io.Serializable;

public class UserInfo implements Serializable {

  private static final long serialVersionUID = 4101419299876676035L;
  private PeerAddress peerAddress;
  private String userName;
  private String password;

  public UserInfo() {
    peerAddress = null;
    userName = "";
    password = "";
  }

  public UserInfo(PeerAddress address, String name, String pw) {
    setPeerAddress(address);
    setUserName(name);
    setPassword(pw);
  }

  public PeerAddress getPeerAddress() {
    return peerAddress;
  }

  public void setPeerAddress(PeerAddress peerAddress) {
    this.peerAddress = peerAddress;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
