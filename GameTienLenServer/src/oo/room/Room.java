package oo.room;

import java.util.Vector;

import oo.connection.InforPlayer;
import oo.connection.InforRoom;
import oo.player.GameVariables;
import oo.player.Player;
import oo.processor.ChiaBai;
import server.Server;

public class Room extends Thread {
	public Server server;
	public String RoomName;
	public Player[] players;
	public int RoomSize;
	public Vector<Integer> winner;
	public int numberPlayers;
	public int curPlayer;
	public int prePlayer;
	public int[] preCards;
	public int numberReady = 0; // dem so nguoi choi da san sang
	public int firstPlayer = -1;
	public int[] ListSkipTurn;

	public Room(Server server, String RoomName, int RoomSize) {
		this.server = server;
		this.server.rooms.add(this);
		this.RoomName = RoomName;
		this.RoomSize = RoomSize;
		this.numberPlayers = 0;
		preCards = null;
		curPlayer = -1;
		prePlayer = -1;
		// Khoi tao danh sach player
		players = new Player[RoomSize];
		for (int i = 0; i < RoomSize; i++) {
			players[i] = null;
		}
		// khoi tao danh sach winner
		winner = new Vector<Integer>();

		// Khoi tao danh sach bo luot
		ListSkipTurn = new int[RoomSize];
		for (int i = 0; i < RoomSize; i++) {
			ListSkipTurn[i] = 0;
		}

		this.start();
	}

	public void run() {
		while (true) {
			while (numberPlayers != RoomSize) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// Neu tat ca deu ha bai, ket thuc game
			if (winner.size() == RoomSize) {
				winner = new Vector<Integer>();
				// luc bat dau luot choi reset cac gia tri cua nguoi choi
				for (int i = 0; i < RoomSize; i++) {
					players[i].game.resetVariables();
				}

				// gui tin hieu chuan bi ve cho client de thiet lap lai cac gia
				// tri duoi client
				sendMessageToAllPlayers("PrepareNewGame@NONE");

			}
			// tam dung 2s
			try {
				Thread.sleep(2000);
			} catch (InterruptedException ex) {
				System.out.println("Loi khi dem giay tam dung");
			}

			// Doi tat ca san sang
			while (numberReady < RoomSize) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// server thuc hien chia bai--> tao mot bo bai co 52 cay
			ChiaBai deck = new ChiaBai();

			if(firstPlayer == -1) {
				firstPlayer = 0;
			}

			// thuc hien chia bai cho cac nguoi choi
			for (int i = 0; i < RoomSize; i++) {
				// chia cac quan bai vao mang chua cac quan bai dc gui di
				deck.dealCards(players[i].game.pocker, i);
			}

			// server gui cac quan bai xuong client
			for (int i = 0; i < RoomSize; i++) {
				sendCards(players[i]);
			}

			// sau khi chia bai xong thi tien hanh danh bai
			startGame(players[firstPlayer]);
			

			// cho den khi tat ca nguoi choi deu het bai
			while (winner.size() != RoomSize) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// tam dung 15s
			try {
				Thread.sleep(15000);
			} catch (InterruptedException ex) {
				System.out.println("Loi khi dem giay tam dung");
			}
			
			firstPlayer = winner.get(0);
		}
	}

	// ham cho phep cac thao tac danh, boc, an bai
	public void startGame(Player player) {
		if (countCardsOfPlayer(player) > 0 && player.game.isHitCards == false) {
			sendDanh(player);
			player.game.isHitCards = true;
		}

	}

	// gui tin hieu danh bai cho client
	public void sendDanh(Player player) {
		// player.connection.sendMessage("tatboluoc"); ko hieu
		player.connection.sendMessage("NewTurn@" + player.game.OrderNumber);
	}

	public int countCardsOfPlayer(Player player) {
		int count = 0;
		for (int i : player.game.pocker) {
			if (i != 0) {
				count++;
			}
		}
		return count;
	}

	// gui cac quan bai duoc chia ve cho client
	public void sendCards(Player player) {
		// Cau truc message gui: "DealCards@card1_card2_..._card13"
		String message = "DealCards@";
		for (int i = 0; i < 13; i++) {
			if (i != 0) {
				message += "_";
			}
			message += player.game.pocker[i];
		}
		message = "DealCards@4_5_8_9_12_13_14_15_16_17_18_19_20"; // test
		player.connection.sendMessage(message);
	}

	public String getRoomName() {
		return RoomName;
	}

	public boolean addPlayer(Player player) {
		if (numberPlayers < RoomSize) {
			numberPlayers++;
			for (int index = 0; index < RoomSize; index++) {
				if (players[index] == null) {
					players[index] = player;
					players[index].game = new GameVariables(this, index);
					return true;
				}
			}
		}
		return false;
	}

	public void removePlayer(int index) {
		players[index] = null;
	}

	public boolean isAvailable() {
		return numberPlayers < RoomSize;
	}

	public int getRoomSize() {
		return RoomSize;
	}

	public Player getPlayer(int index) {
		return players[index];
	}

	/*
	 * @Ham gui message cho tat ca player trong room
	 */
	public void sendMessageToAllPlayers(String message) {
		for (int i = 0; i < RoomSize; i++) {
			if (getPlayer(i) != null) {
				getPlayer(i).connection.sendMessage(message);
			}
		}
	}

	public void sendInforRoomToAllPlayers() {
		InforPlayer[] inforPlayers = new InforPlayer[RoomSize];
		for (int i = 0; i < RoomSize; i++) {
			if (players[i] != null) {
				inforPlayers[i] = new InforPlayer(players[i].UserName,
						players[i].avatar);
			} else {
				inforPlayers[i] = null;
			}
		}
		InforRoom inforRoom = new InforRoom(inforPlayers);
		for (int i = 0; i < RoomSize; i++) {
			if (getPlayer(i) != null) {
				getPlayer(i).connection.sendMessage("InforRoom@NONE");
				getPlayer(i).connection.sendInforRoom(inforRoom);
			}
		}
	}

	public void updateGame(int orderNumber, int amount) {
		players[orderNumber].game.NumbersCardsRemain -= amount;
		
		// Kiem tra player co het bai
		if (players[orderNumber].game.NumbersCardsRemain == 0) {
			players[orderNumber].game.isOver = true;
			winner.add(orderNumber);
			sendMessageToAllPlayers("Over@" + orderNumber + ":" + winner.size());
			
			// neu tat ca deu het bai
			if(winner.size() == RoomSize - 1) {
				for(Player player : players) {
					if(player.game.isOver == false) {
						winner.add(player.game.OrderNumber);
						sendMessageToAllPlayers("Over@" + winner.get(RoomSize-1) + ":" + winner.size());
						return;
					}
				}
			} 
		}
		
		// chuyen luot cho nguoi tiep theo
		if(winner.size() != RoomSize) {
			for(int i = 1; i < RoomSize; i++) {
				if(ListSkipTurn[(orderNumber + i) % 4] == 0 && players[(orderNumber + i) % 4].game.isOver != true) {
					players[(orderNumber + i) % 4].connection.sendMessage("Turn");
					break;
				}
			}
		}
	}
}
