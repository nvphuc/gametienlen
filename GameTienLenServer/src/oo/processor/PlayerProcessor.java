package oo.processor;

import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

import oo.player.GameVariables;
import oo.player.Player;
import oo.player.PlayerStatus.STATUS;
import oo.room.Room;

public class PlayerProcessor extends Processor {

	public PlayerProcessor(Player player) {
		super(player);
	}

	@Override
	public void run() {
		String message = "";
		while (player.status == STATUS.CONNECTED) {
			message = player.connection.receiveMessage();
			System.out.println("Messnhan : " + message);
			handleMessage(message);
		}
	}

	@Override
	protected void handleMessage(String message) {
		String HeadMessage = message.substring(0, message.indexOf("@"));
		String BodyMessage = message.substring(message.indexOf("@") + 1);
		String[] data;
		DataBaseProcessor database = new DataBaseProcessor();
		switch (HeadMessage) {

		case "Login":
			/*
			 * @Nguoi choi dang nhap vao he thong - Cau truc messsage nhan:
			 * "Login@Username:password" - Cau truc message tra ve: "OK@id" :
			 * dang nhap thanh cong va tra ve avatar "ERROR" : dang nhap that
			 * bai
			 */
			data = BodyMessage.split(":");
			player.IdPlayer = database.accessDataBase(data[0], data[1]);
			if (player.IdPlayer > 0) {
				player.UserName = data[0];
				player.connection.sendMessage("OK@" + player.IdPlayer);
				player.avatar = database.getAvatar(player.IdPlayer);
				player.connection.sendImage(player.avatar);
			} else
				player.connection.sendMessage("ERROR");
			break;

		case "Register":
			/*
			 * @Nguoi choi dang ki tai khoan - Cau truc messsage nhan:
			 * "Register@Username:Passs1:Pass2:true|false" - true co gui avatar,
			 * false ko gui avatar - Cau truc message gui: "OK" "ERROR"
			 */
			data = BodyMessage.split(":");
			if (data[3].equals("true")) {
				ImageIcon avatar = player.connection.receiveImage();
				String result = database.createAccount(data[0], data[1],
						data[2], avatar);
				player.connection.sendMessage(result);
			} else {
				String result = database.createAccount(data[0], data[1],
						data[2]);
				player.connection.sendMessage(result);
			}
			break;

		case "GetRooms":
			/*
			 * @Lay danh sach phong choi hien co - Cau truc messsage nhan:
			 * "Register@NONE" - Cau truc message gui: "NONE" : khong co phong
			 * nao "RoomName1:RoomName2:..." : danh sach cac phong
			 */
			player.connection.sendMessage(getRoomNamesOnServer());
			break;

		case "CreateRoom":
			/*
			 * @Tao phong choi - Cau truc messsage nhan:
			 * "CreateRoom@RoomName:MaxPlayer" - Cau truc message gui: "OK@OrderNumber" :
			 * tao phong thanh cong "ERROR" : trung ten phong
			 */
			data = BodyMessage.split(":");
			boolean check = true;
			for (Room tmpRoom : getAllRoomsOnServer()) {
				if (data[0].equals(tmpRoom.getRoomName())) {
					check = false;
					break;
				}
			}
			if (check) {
				Room room = new Room(player.server, data[0],
						Integer.parseInt(data[1]));
				room.addPlayer(player);
				player.status = STATUS.PLAYING_GAME;
				player.connection.sendMessage("OK@" + player.game.OrderNumber);
				new GameProcessor(player);
			} else
				player.connection.sendMessage("ERROR");
			break;

		case "JoinRoom":
			/*
			 * @Tham gia vao phong - Cau truc messsage nhan: "JoinRoom@RoomName"
			 * - Cau truc message gui: "OK@OrderNumber" : thanh cong "ERROR" : phong da day
			 * hoac khong ton tai
			 */
			Room room = getRoomOnServer(BodyMessage);
			if (room != null) {
				if (room.addPlayer(player)) {
					player.status = STATUS.PLAYING_GAME;
					player.connection.sendMessage("OK@" + player.game.OrderNumber);
					new GameProcessor(player);
				}
			} else {
				player.connection.sendMessage("ERROR");
			}
			break;

		default:
			player.status = STATUS.DISCONNECT;
			player.server.players.remove(player);
		}
	}

	public String getRoomNamesOnServer() {
		String names = "";
		int NumberRooms = getAllRoomsOnServer().size();
		if (NumberRooms > 0) {
			for (int i = 0; i < NumberRooms - 1; i++) {
				Room room = getRoomOnServer(i);
				names += room.getRoomName() + ":";
			}
			names += getRoomOnServer(NumberRooms - 1).getRoomName();
		} else
			names += "NONE";
		return names;
	}

	public Vector<Room> getAllRoomsOnServer() {
		return player.server.rooms;
	}

	public Room getRoomOnServer(int index) {
		return player.server.rooms.get(index);
	}

	public Room getRoomOnServer(String RoomName) {
		for (Room room : getAllRoomsOnServer()) {
			if (RoomName.equals(room.getRoomName())) {
				if (room.isAvailable())
					return room;
				else
					break;
			}
		}
		return null;
	}
}
