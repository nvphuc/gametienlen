package oo.processor;

import java.awt.Point;

import game.Game;
import game.Player;
import oo.connection.Connection;
import oo.gui.Gui;

public abstract class Processor {

	protected Gui gui;

	public Processor(Gui gui) {
		this.gui = gui;
	}

	public Gui getGui() {
		return gui;
	}

	public Game getGame() {
		return gui.getGame();
	}

	public Player getPlayer() {
		return gui.getGame().player;
	}

	public Connection getConnection() {
		return gui.getGame().connection;
	}

	public Point getGuiLocation(){
		return gui.getLocationOnScreen();
	}
}
