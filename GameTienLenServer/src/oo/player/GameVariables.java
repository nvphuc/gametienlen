package oo.player;

import oo.room.Room;

public class GameVariables {
	public boolean isReady;
	public boolean isHitCards;
	public boolean isSkipTurn;
	public boolean isOver;
	public int OrderNumber;
	public int NumbersCardsRemain;
	public Room room;
	public int pocker[] = new int[13];
	
	public GameVariables(Room room, int OrderNumber) {
		this.room = room;
		this.OrderNumber = OrderNumber;
		resetVariables();
	}
	
	public void resetVariables() {
		isReady = false;
		isHitCards = false;
		isSkipTurn = false;
		isOver = false;
		NumbersCardsRemain = 13;
	}
}
