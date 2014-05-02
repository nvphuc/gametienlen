package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;



import oo.player.Player;
import oo.room.Room;

public class Server {

	public Vector<Player> players;
	public Vector<Room> rooms;
	
	public Server() {
		players = new Vector<Player>();
		rooms = new Vector<Room>();
		ServerSocket server;
		try {
			server = new ServerSocket(8888);
			while (true) {
				Socket socket = server.accept();
				players.add(new Player(this, socket));
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public static void main(String[] args) {
		System.out.println("Start");
		new Server();
	}
}