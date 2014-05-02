package oo.player;

import java.net.Socket;

import javax.swing.ImageIcon;

import oo.connection.Connection;
import oo.player.PlayerStatus.STATUS;
import oo.processor.PlayerProcessor;
import server.Server;

public class Player {
	public Server server;
	public Connection connection;
	public String UserName;
	public ImageIcon avatar;
	public int IdPlayer;
	public GameVariables game;
	public STATUS status;

	public Player(Server server, Socket socket) {
		this.server = server;
		connection = new Connection(socket);
		UserName = "";
		avatar = null;
		IdPlayer = -1;
		game = null;
		status = STATUS.CONNECTED;
		new PlayerProcessor(this);
	}
}
