package oo.processor;

import oo.connection.InforPlayer;
import oo.connection.InforRoom;
import oo.player.Player;
import oo.player.PlayerStatus.STATUS;
import oo.processor.TypeCards.type;
import oo.room.Room;

public class GameProcessor extends Processor {

	public GameProcessor(Player player) {
		super(player);
		getRoom().sendInforRoomToAllPlayers();
	}

	@Override
	public void run() {
		String message = "";
		while (player.status == STATUS.PLAYING_GAME) {
			message = player.connection.receiveMessage();
			handleMessage(message);
		}
	}

	@Override
	protected void handleMessage(String message) {
		System.out.println("Server Nhan : " + message);
		String HeadMessage = message.substring(0, message.indexOf("@"));
		String BodyMessage = message.substring(message.indexOf("@") + 1);
		String[] data;

		switch (HeadMessage) {
		case "Chat":
			/*
			 * @Chat trong phong game - Cau truc messsage nhan:
			 * "Chat@UserNameGui:NoiDungChat" - Cau truc message gui:
			 * "Chat@UserNameGui:NoiDungChat"
			 */
			sendChat(message);
			break;

		case "Ready":
			/*
			 * @ Message nhan: "Ready@NONE"
			 * 
			 * @ Messgae gui :
			 * "Ready@ true|false|* true|false|* true|false|* true|false|*"
			 */
			sendReady();
			break;

		case "SkipTurn":
			/*
			 * @ Message nhan: "SkipTurn"
			 * 
			 * @ Message gui :
			 */
			getRoom().ListSkipTurn[player.game.OrderNumber] = 1;
			sendSkipTurn();

			break;

		case "HitCards":
			/*
			 * @ Message nhan: "HitCards@bai1_bai2_bai3_...bain"
			 */
			handleCards(BodyMessage);
			break;

		}

	}

	public int[] parseCard(String s) {
		String[] temp = s.split("_");
		int[] cards = new int[temp.length];
		for (int i = 0; i < cards.length; i++) {
			cards[i] = Integer.parseInt(temp[i]);
		}
		return cards;
	}

	public type getType(int[] cards) {
		switch (cards.length) {
		case 1:
			return type.LE;

		case 2:
			if (isDoi(cards[0], cards[1]))
				return type.DOI;
			return type.ERROR;

		case 3:
			if (isSam(cards))
				return type.SAM;
			if (isSanh(cards))
				return type.SANH;
			return type.ERROR;

		case 4:
			if (isTuquy(cards))
				return type.TUQUY;
			if (isSanh(cards))
				return type.SANH;
			return type.ERROR;

		case 6:
			if (isBadoithong(cards))
				return type.BADOITHONG;
			if (isSanh(cards))
				return type.SANH;
			return type.ERROR;

		case 8:
			if (isBondoithong(cards))
				return type.BONDOITHONG;
			if (isSanh(cards))
				return type.SANH;
			return type.ERROR;

		default:
			if (isSanh(cards))
				return type.SANH;
			return type.ERROR;
		}
	}

	public boolean isDoi(int card1, int card2) {
		if (card1 / 4 == card2 / 4)
			return true;
		return false;
	}

	public boolean isSam(int[] cards) {
		if (cards.length == 3 && isDoi(cards[0], cards[1])
				&& isDoi(cards[0], cards[2]))
			return true;
		return false;
	}

	public boolean isTuquy(int[] cards) {
		if (cards.length == 4 && isDoi(cards[0], cards[1])
				&& isDoi(cards[2], cards[3]) && isDoi(cards[0], cards[2]))
			return true;
		return false;
	}

	public boolean isBadoithong(int[] cards) {
		if (cards.length == 6 && isDoi(cards[0], cards[1])
				&& isDoi(cards[2], cards[3]) && isDoi(cards[4], cards[5])) {
			for (int i = 0; i < 3; i++) {
				if (cards[i * 2] / 4 != cards[0] / 4- i) {
					return false;
				}
			}
			if (cards[0] / 4 == 13) {
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean isBondoithong(int[] cards) {
		if (cards.length == 8 && isDoi(cards[0], cards[1])
				&& isDoi(cards[2], cards[3]) && isDoi(cards[4], cards[5])
				&& isDoi(cards[6], cards[7])) {
			for (int i = 0; i < 4; i++) {
				if (cards[i * 2] / 4 != cards[0] / 4- i) {
					return false;
				}
			}
			if (cards[0] / 4 == 13) {
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean isSanh(int[] cards) {
		if (cards.length > 2) {
			for (int i = 0; i < cards.length; i++) {
				if (cards[i] / 4 != cards[0] / 4- i) {
					return false;
				}
			}
			if (cards[0] / 4 == 13) {
				return false;
			}
			return true;
		}
		return false;
	}

	private void acceptCards(String listCards) {
		int[] cards = parseCard(listCards);
		getRoom().preCards = cards;
		player.connection.sendMessage("ResultHitCards@OK");
		getRoom().sendMessageToAllPlayers(
				"HitCards@" + player.game.OrderNumber + ":" + listCards);
		getRoom().updateGame(player.game.OrderNumber, cards.length);
	}

	private void handleCards(String listCards) {
		int[] cards = parseCard(listCards);
		if (getRoom().preCards == null) {
			if (getType(cards) == type.ERROR) {
				player.connection.sendMessage("ResultHitCards@ERROR");
			} else {
				acceptCards(listCards);
			}
		} else {
			if (getRoom().preCards.length == cards.length) {
				if (getType(getRoom().preCards) != getType(cards)
						|| getRoom().preCards[0] > cards[0]) {
					player.connection.sendMessage("ResultHitCards@ERROR");
				} else {
					acceptCards(listCards);
				}
			} else {
				if (getType(getRoom().preCards) == type.LE
						&& getRoom().preCards[0] / 4 == 13
						&& (getType(cards) == type.TUQUY || getType(cards) == type.BADOITHONG)) {
					acceptCards(listCards);
				} else {
					if (getType(getRoom().preCards) == type.DOI
							&& getRoom().preCards[0] / 4 == 13
							&& (getType(cards) == type.BONDOITHONG)) {
						acceptCards(listCards);
					} else {
						player.connection.sendMessage("ResultHitCards@ERROR");
					}
				}
			}
		}
	}

	private void sendSkipTurn() {
		
		int count = 0;
		//dem so nguoi con trong luot
		for (int i = 0; i < getRoom().RoomSize; i++) {
			if (getRoom().ListSkipTurn[i] == 0 && !getRoom().players[i].game.isOver)
				count ++;
		}
		//neu lon hon 1 nguoi chuyen luot cho nguoi tiep theo
		if (count > 1) {
			for (int i = 1; i < getRoom().RoomSize; i++) {
				int index = (player.game.OrderNumber + i) % 4;
				if (getRoom().ListSkipTurn[index] == 0 && !getRoom().players[index].game.isOver) {
					getRoom().getPlayer(index).connection.sendMessage("Turn");
					return;
				}
			}
		}
		//neu chi con 1 nguoi thi bat dau van moi, reset lai listSkipTurn
		else {
			for(int i = 0; i < getRoom().RoomSize; i++) {
				if(getRoom().ListSkipTurn[i] == 0 && !getRoom().players[i].game.isOver) {
					getRoom().sendMessageToAllPlayers("NewTurn@" + i);
					break;
				}
			}
			getRoom().preCards = null;
			resetListSkipTurn();
		}
	}

	private void resetListSkipTurn() {
		for (int i = 0; i < getRoom().RoomSize; i++) {
			getRoom().ListSkipTurn[i] = 0;
		}
	}

	private void sendReady() {
		// luu tin hieu san sang
		player.game.isReady = true;
		// bao cho tat ca client biet nguoi choi nay da san sang
		String message = "Ready@";
		// tao chuoi muon gui
		for (int i = 0; i < getRoom().RoomSize; i++) {
			if (getRoom().getPlayer(i) != null) {
				message += " " + getRoom().getPlayer(i).game.isReady;
			} else {
				message += " *";
			}
		}
		// gui ve cac player trong phong
		getRoom().sendMessageToAllPlayers(message);

		// tang bien dem so nguoi choi da san sang
		getRoom().numberReady++;
	}

	private void sendChat(String message) {
		player.game.room.sendMessageToAllPlayers(message);
	}

	/**
	 * @Ham gui vi tri cua nguoi choi trong phong - Cau truc message gui:
	 *      "OrderNumber@OrderNumber"
	 */
	public void sendOrderNumber() {
		player.connection.sendMessage("OrderNumber@" + player.game.OrderNumber);
	}

	/**
	 * Ham gui username cua cac player trong phong room ve client
	 * 
	 * @ Cau truc message gui:
	 * "NamePlayers@ username1|* username2|* username3|* username4|8"
	 */
	public void sendUserNames() {
		String message = "NamePlayers@";
		for (int i = 0; i < getRoom().RoomSize; i++) {
			if (getRoom().getPlayer(i) != null) {
				message += " " + getRoom().getPlayer(i).UserName;
			} else {
				message += " *";
			}
		}
		getRoom().sendMessageToAllPlayers(message);
	}

	public Room getRoom() {
		return player.game.room;
	}

}
