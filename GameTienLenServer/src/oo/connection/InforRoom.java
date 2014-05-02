package oo.connection;

import java.io.Serializable;

import oo.room.Room;

public class InforRoom implements Serializable {
	public InforPlayer[] inforPlayers;
	
	public InforRoom(InforPlayer[] inforPlayers) {
		this.inforPlayers = inforPlayers;		
	}
}
