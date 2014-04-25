package com.them;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
	public Vector<ThreadPlayer> players = new Vector<ThreadPlayer>();
	public Vector<ThreadRoom> rooms = new Vector<ThreadRoom>();

	public Server() {
		ServerSocket server;
		Socket connections;
		try {
			server = new ServerSocket(9999);
			while (true) {
				connections = server.accept();
				new ThreadPlayer(this, connections).start();
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