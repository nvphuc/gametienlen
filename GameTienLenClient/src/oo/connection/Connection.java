package oo.connection;

import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {

	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;

	public boolean connect() {
		try {
			socket = new Socket("localhost", 9999);
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean disconnect() {
		try {
			dos.close();
			dis.close();
			socket.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendMessage(String message) {
		try {
			dos.writeUTF(message);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendImage(Image image) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(image);
            oos.flush();
            return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} 
	}

	public String receiveMessage() {
		String message = "";
		try {
			message = dis.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	public Image receiveImage(){		
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Image image = (Image) ois.readObject(); 
			return image;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} 	
	}
}
