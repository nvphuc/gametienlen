package game;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Player {
	public int id;
	public String username;	
	public ImageIcon avatar ;

	public Player(){
		username = "";
		avatar = null; 	
	}
}
