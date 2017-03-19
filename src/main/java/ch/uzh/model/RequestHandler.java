package ch.uzh.model;


import ch.uzh.controller.MainWindowController;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.FutureRemove;
import net.tomp2p.futures.*;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;


public class RequestHandler {

	private static Logger log = LoggerFactory.getLogger("RequestHandler");
	private static MainWindowController mainWindowController;
	private final static int DEFAULTPORT = 54000;
	private final static String BOOTSTRAPNODE = "Bootstrapnode";
	private static Number160 USER_DOMAIN = Number160.createHash("user");
	private static Number160 FRIEND_DOMAIN = Number160.createHash("friend");
	private static Number160 MESSAGE_DOMAIN = Number160.createHash("message");
	private static Number160 AUDIO_DOMAIN = Number160.createHash("audio");
	private static Number160 VIDEO_DOMAIN = Number160.createHash("video");

	private static long TRY_AGAIN_TIME_WINDOW = 5000;

	public RequestHandler() {}

	public static Object handleRequest(Request request, Client node) {
		try {
			return handleRequest(request, node, null);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object handleRequest(Request request, Client node,
			BaseFutureListener genericListener) throws LineUnavailableException {
		switch (request.getType()) {
			case RECEIVE:
				try {
					return handleReceive(request, node);
				} catch (ClassNotFoundException e2) {
					e2.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				} catch (LineUnavailableException e2) {
					e2.printStackTrace();
				}
			case RETRIEVE:
				try {
					return handleRetrieve(request, node, genericListener);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			case SEND:
				try {
					return handleSend(request, node, genericListener);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			case STORE:
				try {
					return handleStore(request, node, genericListener);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			default:
				return null;
		}
	}

	private static Boolean handleSend(Request request, Client node,
			BaseFutureListener genericListener) throws ClassNotFoundException, IOException,
			InterruptedException, LineUnavailableException {
		final Node n = node;
		if (request instanceof MessageRequest) {
			final MessageRequest r = (MessageRequest) request;
			final long time = System.currentTimeMillis();

			if (r.getMessage() != null && r.getMessage().getReceiverAddress() != null) {

				OnlineStatusRequest onlineStatusRequest =
						new OnlineStatusRequest(r.getMessage().getReceiverAddress(),
								n.getPeer().peerAddress(), r.getMessage().getSenderID(),
								r.getMessage().getReceiverID(), RequestType.SEND);
				onlineStatusRequest.setStatus(RequestStatus.WAITING);

				BaseFutureListener<FutureDirect> onlineStatusListener =
						new BaseFutureListener<FutureDirect>() {

							@Override
							public void operationComplete(FutureDirect futureDirect)
									throws Exception {
								if (futureDirect != null && futureDirect.isSuccess()) {
									// Got a response, which means the recipient is online
									n.getPeer().peer()
											.sendDirect(r.getMessage().getReceiverAddress())
											.object(r.getMessage()).start();
								} else {
									long timeNow = System.currentTimeMillis();
									if (timeNow - time < (TRY_AGAIN_TIME_WINDOW)) {
										tryAgain(onlineStatusRequest, node, this);
									} else {
										// The recipient really seems to be offline -> Store the
										// message
										r.setType(RequestType.STORE);
										handleRequest(r, n);
									}
								}
							}

							@Override
							public void exceptionCaught(Throwable t) throws Exception {}
						};

				handleRequest(onlineStatusRequest, n, onlineStatusListener);
			}
			return true;
		}
		if (request instanceof BootstrapRequest) {
			BootstrapRequest r = (BootstrapRequest) request;

			InetAddress address = Inet4Address.getByName(r.getBootstrapNodeIP());
			FutureDiscover futureDiscover =
					n.getPeer().peer().discover().inetAddress(address).ports(DEFAULTPORT).start();
			futureDiscover.addListener(new BaseFutureListener<FutureDiscover>() {

				@Override
				public void operationComplete(FutureDiscover future) throws Exception {
					PeerAddress master = future.peerAddress();
					System.out.println(future.reporter());
					FutureBootstrap futureBootstrap = n.getPeer().peer().bootstrap()
							.peerAddress(master).ports(DEFAULTPORT).start();
					// Attention, the listener could be wrongly assigned to the future object
					// TODO: assure that the genericListener is a FutureBootstrap listener
					futureBootstrap.addListener(genericListener);
				}

				@Override
				public void exceptionCaught(Throwable t) throws Exception {
					log.error(t.getMessage());
				}

			});
			return true;
		}
		if (request instanceof AudioRequest) {
			final AudioRequest r = (AudioRequest) request;
			final long time = System.currentTimeMillis();

			if (r.getReceiverAddress() != null) {
				OnlineStatusRequest onlineStatusRequest =
						new OnlineStatusRequest(r.getReceiverAddress(), n.getPeer().peerAddress(),
								r.getSenderName(), r.getReceiverName(), RequestType.SEND);
				onlineStatusRequest.setStatus(RequestStatus.WAITING);

				BaseFutureListener<FutureDirect> onlineStatusListener =
						new BaseFutureListener<FutureDirect>() {

							@Override
							public void operationComplete(FutureDirect futureDirect)
									throws Exception {
								if (futureDirect != null && futureDirect.isSuccess()) {
									// Got a response, which means the recipient is online
									n.getPeer().peer().sendDirect(r.getReceiverAddress()).object(r)
											.start();
								} else {
									long timeNow = System.currentTimeMillis();
									if (timeNow - time < (TRY_AGAIN_TIME_WINDOW)) {
										tryAgain(onlineStatusRequest, node, this);
									} else {
										// The recipient really seems to be offline -> Store the
										// message
										r.setType(RequestType.STORE);
										handleRequest(r, n, genericListener);
									}
								}
							}

							@Override
							public void exceptionCaught(Throwable t) throws Exception {}
						};

				handleRequest(onlineStatusRequest, n, onlineStatusListener);
			}
			return true;
		}
		if (request instanceof VideoRequest) {
			final VideoRequest r = (VideoRequest) request;
			final long time = System.currentTimeMillis();

			if (r.getReceiverAddress() != null) {
				OnlineStatusRequest onlineStatusRequest =
						new OnlineStatusRequest(r.getReceiverAddress(), n.getPeer().peerAddress(),
								r.getSenderName(), r.getReceiverName(), RequestType.SEND);
				onlineStatusRequest.setStatus(RequestStatus.WAITING);

				BaseFutureListener<FutureDirect> onlineStatusListener =
						new BaseFutureListener<FutureDirect>() {

							@Override
							public void operationComplete(FutureDirect futureDirect)
									throws Exception {
								if (futureDirect != null && futureDirect.isSuccess()) {
									// Got a response, which means the recipient is online
									n.getPeer().peer().sendDirect(r.getReceiverAddress()).object(r)
											.start();
								} else {
									long timeNow = System.currentTimeMillis();
									if (timeNow - time < (TRY_AGAIN_TIME_WINDOW)) {
										tryAgain(onlineStatusRequest, node, this);
									} else {
										// The recipient really seems to be offline -> Store the
										// message
										r.setType(RequestType.STORE);
										handleRequest(r, n, genericListener);
									}
								}
							}

							@Override
							public void exceptionCaught(Throwable t) throws Exception {}
						};

				handleRequest(onlineStatusRequest, n, onlineStatusListener);
			}
			return true;
		}

		if (request instanceof FriendRequest) {
			final FriendRequest r = (FriendRequest) request;
			PeerAddress peerAddress = null;
			if (r.getReceiverAddress() != null) {
				peerAddress = r.getReceiverAddress();
				n.getPeer().peer().sendDirect(peerAddress).object(r).start();
			} else {
				BaseFutureListener<FutureGet> requestListener =
						new BaseFutureListener<FutureGet>() {
							@Override
							public void operationComplete(FutureGet futureGet) throws Exception {
								if (futureGet != null && futureGet.isSuccess()
										&& futureGet.data() != null) {
									UserInfo user = (UserInfo) futureGet.data().object();
									node.getPeer().peer().sendDirect(user.getPeerAddress())
											.object(r).start();
								}
							}

							@Override
							public void exceptionCaught(Throwable t) throws Exception {}
						};
				LoginHelper.retrieveUserInfo(r.getReceiverName(), node, requestListener);
			}

			return true;
		}
		if (request instanceof OnlineStatusRequest) {
			final OnlineStatusRequest r = (OnlineStatusRequest) request;
			FutureDirect future =
					n.getPeer().peer().sendDirect(r.getReceiverAddress()).object(r).start();
			if (genericListener != null) {
				// Attention, this could assign a wrong BaseFutureListener to the future object.
				// TODO: Assure the listener has type FutureDirect
				future.addListener(genericListener);
			}
		}
		return true;
	}

	private static Message handleReceive(Request request, Node node)
			throws IOException, LineUnavailableException, ClassNotFoundException {

		if (request instanceof MessageRequest) {
			MessageRequest r = (MessageRequest) request;
			Message message = r.getMessage();
			if (message instanceof AudioMessage) {
				AudioMessage audioMessage = (AudioMessage) message;
				try {
					AudioHelper
							.playAudio(EncoderUtils.byteArrayToByteBuffer(audioMessage.getData()));
				} catch (LineUnavailableException e) {
					log.error("AudioMessage could not be played: " + e);
				}
			} else if (message instanceof ChatMessage) {
				ChatMessage chatMessage = (ChatMessage) message;
				mainWindowController.chatPaneController
						.addReceivedMessage(chatMessage.getSenderID(), chatMessage.getData());
			} else if (message instanceof VideoMessage) {
				log.info("handleReceive VideoMessage");
				VideoMessage videoMessage = (VideoMessage) message;
				VideoHelper.playVideo(videoMessage.getData());
			}
			log.info(message.getReceiverID() + " received message: " + message.toString()
					+ " from: " + message.getSenderID().toString());
			return message;
		}

		if (request instanceof BootstrapRequest) {
		}

		if (request instanceof AudioRequest) {
			AudioRequest audioRequest = (AudioRequest) request;
			switch (audioRequest.getStatus()) {
				case WAITING:
					mainWindowController.audioPaneController.askAudioCall(audioRequest);
					break;
				case ACCEPTED:
					mainWindowController.audioPaneController.startAudioCall();
					break;
				case REJECTED:
					mainWindowController.audioPaneController.audioCallRejected(audioRequest);
					break;
				case ABORTED:
					mainWindowController.audioPaneController.audioCallAborted();
					break;
				default:
					break;
			}
		}

		if (request instanceof FriendRequest) {
			FriendRequest friendRequest = (FriendRequest) request;
			Friend f =
					new Friend(friendRequest.getSenderPeerAddress(), friendRequest.getSenderName());
			switch (friendRequest.getStatus()) {
				case WAITING:
					mainWindowController.askFriend(friendRequest);
					break;
				case ACCEPTED:
					mainWindowController.friendlistPaneController.friendshipAccepted(f);
					break;
				case REJECTED:
					mainWindowController.friendlistPaneController.friendshipRejected(f);
					break;
				case ABORTED:
					mainWindowController.friendlistPaneController.friendshipRejected(f);
					break;
				default:
					break;
			}
		}

		if (request instanceof VideoRequest) {
			VideoRequest videoRequest = (VideoRequest) request;
			switch (videoRequest.getStatus()) {
				case WAITING:
					mainWindowController.videoPaneController.askVideoCall(videoRequest);
					break;
				case ACCEPTED:
					mainWindowController.videoPaneController.startVideoCall();
					break;
				case REJECTED:
					mainWindowController.videoPaneController.videoCallRejected(videoRequest);
					break;
				case ABORTED:
					mainWindowController.videoPaneController.videoCallAborted();
					break;
				default:
					break;
			}
		}
		if (request instanceof OnlineStatusRequest) {
			OnlineStatusRequest r = (OnlineStatusRequest) request;
			if (r.hasChangedPeerAddress()) {
				node.getUser().getFriend(r.getSenderName()).setPeerAddress(r.getSenderAddress());
				FriendlistHelper helper = new FriendlistHelper(node);
				helper.storeFriend(node.getUser().getFriend(r.getSenderName()),
						node.getUser().getUsername());
			}
			switch (r.getStatus()) {
				case WAITING:
					OnlineStatusRequest req;
					if (!r.getReceiverName().equals(node.getUser().getUsername())) {
						// then, the peerAddress of the friend has changed and been reassigned to
						// this node
						req = new OnlineStatusRequest(r.getSenderAddress(), r.getReceiverName(),
								r.getSenderName(), RequestType.SEND);
						req.setOnlineStatus(OnlineStatus.ONLINE);
						req.setStatus(RequestStatus.REJECTED);
					} else {
						node.getUser().getFriend(r.getSenderName()).setStatus(OnlineStatus.ONLINE);
						req = new OnlineStatusRequest(r.getSenderAddress(), r.getReceiverName(),
								r.getSenderName(), RequestType.SEND);
						req.setOnlineStatus(OnlineStatus.ONLINE);
						req.setStatus(RequestStatus.ACCEPTED);
					}
					handleRequest(req, node);
					break;
				case ACCEPTED:
					if (node.getUser().getFriend(r.getSenderName()) != null) {
						node.getUser().getFriend(r.getSenderName()).setStatus(r.getOnlineStatus());
					}
					break;
				case REJECTED:
					if (node.getUser().getFriend(r.getSenderName()) != null) {
						node.getUser().getFriend(r.getSenderName()).setStatus(OnlineStatus.OFFLINE);
					}
					break;
				case ABORTED:
					if (node.getUser().getFriend(r.getSenderName()) != null) {
						node.getUser().getFriend(r.getSenderName()).setStatus(OnlineStatus.OFFLINE);
					}
					break;
				default:
					break;
			}
		}

		return null;
	}

	private static Boolean handleStore(Request request, Node node,
			BaseFutureListener genericListener)
			throws IOException, ClassNotFoundException, InterruptedException {
		if (request instanceof UserInfoRequest) {
			UserInfoRequest r = (UserInfoRequest) request;
			if (r.getStatus().equals(RequestStatus.ABORTED)) {
				final UserInfo info = r.getUserInfo();
				FutureRemove future = node.getPeer()
						.remove(Number160.createHash(info.getUserName())).domainKey(USER_DOMAIN)
						.contentKey(Number160.createHash(info.getUserName())).start();
				if (genericListener != null) {
					future.addListener(genericListener);
				}
			} else {
				final UserInfo info = r.getUserInfo();
				FuturePut future = node.getPeer().put(Number160.createHash(info.getUserName()))
						.data(Number160.createHash(info.getUserName()), new Data(info))
						.domainKey(USER_DOMAIN).start();
				if (genericListener != null) {
					future.addListener(genericListener);
				}
			}
			return true;
		}
		if (request instanceof MessageRequest) {
			MessageRequest r = (MessageRequest) request;
			if (r.getMessage() instanceof ChatMessage) {
				ChatMessage m = (ChatMessage) r.getMessage();
				FuturePut futurePut = node.getPeer().put(Number160.createHash(m.getReceiverID()))
						.data(Number160.createHash(m.getSenderID() + m.getDate().getTime()),
								new Data(m))
						.domainKey(MESSAGE_DOMAIN).start();
				if (genericListener != null) {
					futurePut.addListener(genericListener);
				}
			}

		}
		if (request instanceof AudioRequest) {
			AudioRequest r = (AudioRequest) request;
			if (r.getStatus().equals(RequestStatus.WAITING)) {
				AudioInfo audioInfo = new AudioInfo(r.getSenderName(), r.getReceiverName());
				FuturePut futurePut =
						node.getPeer().put(Number160.createHash(r.getReceiverName()))
								.data(Number160.createHash(audioInfo.getSendername()
										+ audioInfo.getReceivedOn().getTime()), new Data(audioInfo))
								.domainKey(AUDIO_DOMAIN).start();
				if (genericListener != null) {
					futurePut.addListener(genericListener);
				}
			}
		}
		if (request instanceof VideoRequest) {
			VideoRequest r = (VideoRequest) request;
			if (r.getStatus().equals(RequestStatus.WAITING)) {
				VideoInfo videoInfo = new VideoInfo(r.getSenderName(), r.getReceiverName());
				FuturePut futurePut =
						node.getPeer().put(Number160.createHash(r.getReceiverName()))
								.data(Number160.createHash(videoInfo.getSendername()
										+ videoInfo.getReceivedOn().getTime()), new Data(videoInfo))
								.domainKey(VIDEO_DOMAIN).start();
				if (genericListener != null) {
					futurePut.addListener(genericListener);
				}
			}
		}
		if (request instanceof FriendRequest) {
			// this store operation is only used if a User gets a new friend and wants to save it in
			// the dht for future use
			// the sender is the one who wants to store the info
			final FriendRequest r = (FriendRequest) request;
			if (r.getStatus().equals(RequestStatus.ABORTED)) {
				// this case is used for removing a friend
				// e.g. when a friend with waiting status has been rejected
				Friend f = new Friend(r.getReceiverAddress(), r.getReceiverName());
				node.getPeer().remove(Number160.createHash(r.getSenderName()))
						.domainKey(FRIEND_DOMAIN).contentKey(Number160.createHash(f.getName()))
						.start();
			} else {
				// this case is used for storing a friend into the dht, either when the friendship
				// has been accepted or when one of both parties is not online and has yet to answer
				// the request (or get the answer to his request)
				Friend friend = new Friend(r.getReceiverAddress(), r.getReceiverName());
				friend.setFriendshipStatus(r.getStatus().toString());

				// store Friend under locationKey username, domainKey friend_domain, contentKey
				// friendname
				FuturePut future = node.getPeer().put(Number160.createHash(r.getSenderName()))
						.data(Number160.createHash(friend.getName()), new Data(friend))
						.domainKey(FRIEND_DOMAIN).start();
				future.addListener(new BaseFutureAdapter<FuturePut>() {
					public void operationComplete(FuturePut future) throws Exception {
						if (future.isSuccess()) {
							log.info("User " + node.getUser().getUsername() + " put "
									+ friend.getName() + " into DHT as friend, with success: "
									+ future.isSuccess());
						} else {
							tryAgain(request, node, this);
						}
					}
				});
			}
		}
		if (request instanceof BootstrapRequest) {
			BootstrapRequest req = (BootstrapRequest) request;
			new User(req.getSenderName(), null, node.getPeer().peerAddress());
			FuturePut future = node.getPeer().put(Number160.createHash(BOOTSTRAPNODE))
					.data(new Data(req.getBootstrapNodeIP())).start();
			// Attention, this listener could be assigned wrongly
			// TODO: assure that only FuturePut listener gets assigned
			future.addListener(genericListener);
			log.info("BootstrapNode created from user: " + node.getUser().getUsername());
			return true;
		}
		return false;
	}

	private static Object handleRetrieve(Request request, Node node,
			BaseFutureListener genericListener)
			throws ClassNotFoundException, IOException, InterruptedException {
		if (request instanceof UserInfoRequest) {
			UserInfoRequest r = (UserInfoRequest) request;
			FutureGet futureGet = node.getPeer()
					.get(Number160.createHash(r.getUserInfo().getUserName())).domainKey(USER_DOMAIN)
					.contentKey(Number160.createHash(r.getUserInfo().getUserName())).start();
			if (genericListener != null) {
				futureGet.addListener(genericListener);
			}
		}
		if (request instanceof MessageRequest) {
			MessageRequest r = (MessageRequest) request;
			FutureGet futureGet = node.getPeer().get(Number160.createHash(r.getSenderName()))
					.domainKey(MESSAGE_DOMAIN).all().start();
			if (genericListener != null) {
				futureGet.addListener(genericListener);
			}
		}
		if (request instanceof AudioRequest) {
			AudioRequest r = (AudioRequest) request;
			FutureGet futureGet = node.getPeer().get(Number160.createHash(r.getSenderName()))
					.domainKey(AUDIO_DOMAIN).all().start();
			if (genericListener != null) {
				futureGet.addListener(genericListener);
			}
		}
		if (request instanceof VideoRequest) {
			VideoRequest r = (VideoRequest) request;
			FutureGet futureGet = node.getPeer().get(Number160.createHash(r.getSenderName()))
					.domainKey(VIDEO_DOMAIN).all().start();
			if (genericListener != null) {
				futureGet.addListener(genericListener);
			}
		}
		if (request instanceof FriendRequest) {
			FriendRequest r = (FriendRequest) request;
			FutureGet futureGet = node.getPeer().get(Number160.createHash(r.getSenderName()))
					.domainKey(FRIEND_DOMAIN).all().start();
			if (genericListener != null) {
				futureGet.addListener(genericListener);
			}
		}
		if (request instanceof BootstrapRequest) {
			FutureGet futureGet = node.getPeer().get(Number160.createHash(BOOTSTRAPNODE)).start();
			futureGet.addListener(genericListener);
		}
		return null;
	}

	public static void setMainWindowController(MainWindowController mWC) {
		mainWindowController = mWC;
	}

	public static void tryAgain(Request request, Node node, BaseFutureListener listener)
			throws LineUnavailableException, InterruptedException {
		log.debug("RequestHandler had unsuccessful Request, Try again...");
		log.debug(
				"Sender: " + request.getSenderName() + ", Receiver: " + request.getReceiverName());
		log.debug(" Request: " + request.getClass() + " Type: " + request.getType());
		Thread.sleep(500);
		handleRequest(request, node, listener);
	}

}
