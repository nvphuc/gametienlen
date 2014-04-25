package oo.gui.element;

import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class TheBai extends JLabel {
	public String cardnumber;
	public int x, y, width, height;
	public boolean isClicked = false;

	// ve quan bai o vi tri moi
	public void displayUp() {
		this.isClicked = true;
		this.setBounds(x, y - 10, width, height);
	}

	// ve lai quan bai tai vi tri cu
	public void setBounds() {
		this.isClicked = false;
		this.setBounds(x, y, width, height);
	}

	// hien thi hinh cua the bai nay
	public void setImage(String cardnumber) {
		this.cardnumber = cardnumber;
		this.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images\\cards\\" + cardnumber + ".png")));
		
		//avatar = Toolkit.getDefaultToolkit().getImage("images\\cell.png");
		
		//this.setIcon(new ImageIcon("/images/cards/"+ cardnumber + ".png"));
	}
}
