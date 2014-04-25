package oo.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import game.Game;
import oo.processor.Processor;

public abstract class Gui extends JFrame implements ActionListener {

	protected int GuiId;
	protected Game game;
	protected Processor processor;
	
	public Gui(Game game, Point location) {
		this.game = game;
		this.setSize(1300, 625);
		this.setResizable(false);
		this.setLocation(location);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public int getGuiId(){
		return GuiId;
	}
	
	public Game getGame(){
		return game;
	}

	abstract public void setGui();
}
