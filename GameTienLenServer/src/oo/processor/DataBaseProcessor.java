package oo.processor;

import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class DataBaseProcessor {
	
	public int accessDataBase(String UserName, String Password) {
		return 1;
	}

	public String createAccount(String Username, String Pass1, String Pass2, ImageIcon avatar) {
		return "OK";
	}

	public String createAccount(String Username, String Pass1, String Pass2) {
		return "OK";
	}

	public ImageIcon getAvatar(int idPlayer) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images\\1.jpg"));
	}
}
