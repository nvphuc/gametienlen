package game;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import oo.connection.Connection;
import oo.gui.GuiLogin;

public class Game {

	public Connection connection;
	public Player player;

	public Game() {
		connection = new Connection();
		player = new Player();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point location = new Point((screenSize.width - 1300)/2, (screenSize.height - 625)/2);
		new GuiLogin(this, location);
	}

	public static void main(String[] args) {
		Game game = new Game();
	}

}
