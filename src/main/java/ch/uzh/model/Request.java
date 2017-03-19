package ch.uzh.model;

import net.tomp2p.peers.PeerAddress;

import java.io.Serializable;

public abstract class Request implements Serializable {
  private static final long serialVersionUID = 3325627567053761196L;
  private RequestType type;
  private RequestStatus status;
  private PeerAddress receiverAddress;
  private String receiverName;
  private String senderName;

  public Request(RequestType type) {
    setType(type);
    setStatus(RequestStatus.WAITING);
  }

  public Request(RequestType type, RequestStatus status) {
    this(type);
    setStatus(status);
  }

  public Request(RequestType type, PeerAddress receiverAddress, String receiverName,
                 String senderName) {
    this(type);
    setReceiverAddress(receiverAddress);
    setReceiverName(receiverName);
    setSenderName(senderName);
  }

  public Request(RequestType type, RequestStatus status, PeerAddress receiverAddress,
                 String receiverName, String senderName) {
    this(type, receiverAddress, receiverName, senderName);
    setStatus(status);
  }

  public Request() {
    type = null;
    status = null;
  }

  public RequestType getType() {
    return type;
  }

  public void setType(RequestType type) {
    this.type = type;
  }

  public RequestStatus getStatus() {
    return status;
  }

  public void setStatus(RequestStatus status) {
    this.status = status;
  }

  public PeerAddress getReceiverAddress() {
    return receiverAddress;
  }

  public void setReceiverAddress(PeerAddress receiverAddress) {
    this.receiverAddress = receiverAddress;
  }

  public String getReceiverName() {
    return receiverName;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

}
