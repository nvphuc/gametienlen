package oo.gui.element;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Room extends JPanel{
	private JButton room;
	private JLabel lbname;
	private String name;
	
	public Room(int x, int y, String name){
		//this.id = id;
		this.name = name;
		
		this.setLayout(null);
		this.setBounds(x, y, 50, 50);
		
		room = new JButton();
		room.setBounds(0, 0, 50, 30);
		this.add(room);
		
		lbname = new JLabel(name);
		lbname.setBounds(0, 30, 50, 20);
		this.add(lbname);
	}
	
	public String getName(){
		return name;
	}
}
