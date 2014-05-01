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
	
	public boolean connect() {
		try {
			socket = new Socket("localhost", 8888);
			oos = new ObjectOutputStream(this.socket.getOutputStream());
			ois = new ObjectInputStream(this.socket.getInputStream());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean disconnect() {
		try {
			oos.close();
			ois.close();
			socket.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
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

	public String receiveMessage() {
		String message = "";
		try {
			message = (String) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	public ImageIcon receiveImage(){		
		try {		
			ImageIcon image = (ImageIcon) ois.readObject();
			return image;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} 	
	}
	
	public InforRoom receiveInforRoom(){		
		try {		
			InforRoom infor = (InforRoom) ois.readObject();
			return infor;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} 	
	}
	
}
