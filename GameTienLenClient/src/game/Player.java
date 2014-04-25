package game;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Player {
	public int id;
	public String username;	
	public Image avatar ;
	private String room;		//ten phong choi dang tham gia
	public int ordernumber;			//Vi tri ghe ngoi trong phong choi
	
	public Player(){
		username = new String();
		avatar = Toolkit.getDefaultToolkit().getImage("images\\cell.png");
		//URL url = getClass().getResource("cell.png");  
		//avatar = Toolkit.getDefaultToolkit().getImage(url);
		room = new String();
	}
}
