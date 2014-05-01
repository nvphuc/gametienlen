package oo.connection;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class InforPlayer implements Serializable {
	public String UserName;
	public ImageIcon Avatar;
	
	public InforPlayer(String UserName, ImageIcon Avatar) {
		this.UserName = UserName;
		this.Avatar = Avatar;
	}
	
}
