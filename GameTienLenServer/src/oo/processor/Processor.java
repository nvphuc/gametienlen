package oo.processor;

import oo.player.Player;
import oo.player.PlayerStatus.STATUS;

public abstract class Processor extends Thread {

	protected Player player;
	
	public Processor(Player player) {
		this.player = player;
		this.start();
	}
	
	abstract public void run();

	abstract protected void handleMessage(String message);
	
}
