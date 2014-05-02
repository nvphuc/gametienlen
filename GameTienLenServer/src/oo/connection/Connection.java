package oo.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;

public class Connection {

	private Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	public Connection(Socket socket) {
		this.socket = socket;
		try {
			oos = new ObjectOutputStream(this.socket.getOutputStream());
			ois = new ObjectInputStream(this.socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean sendMessage(String message) {
		try {
			oos.writeObject(message);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendImage(ImageIcon image) {
		try {
			oos.writeObject(image);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean sendInforRoom(InforRoom infor) {
		try {
			oos.writeObject(infor);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String receiveMessage() {
		String message = "";
		try {
			message = (String) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	public ImageIcon receiveImage() {
		try {
			ImageIcon image = (ImageIcon) ois.readObject();
			return image;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
