package oo.gui;

import game.Game;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import oo.processor.ProcessorRegister;

public class GuiRegister extends Gui {
	
	public static final long serialVersionUID=1L;
	public JPanel pn1;
	public JLabel lbUsername,lbPass1,lbConfirmPassword,lbPass2,lbAvatar;
	public TextField tfUsername,tfPass1,tfConfirmPassword,tfPass2;
	public Button btdangky, bthuy,btAvatar;
	public JScrollPane scrollPane;
	public ImageIcon icon;
	

	@Override
	public void setGui() {
		  //icon=new ImageIcon("image/avatar6.jpg");
		pn1 = new JPanel(){
			public void paintComponent(Graphics g){
			Dimension d=getSize();
			g.drawImage(((ImageIcon) icon).getImage(), 0, 0, d.width, d.height, null);
			setOpaque(false);
			super.paintComponent(g);
			}
			};
		pn1.setLayout(null);
		
		btAvatar=new Button("Avatar");
		btAvatar.setBounds(1100,50,150,150);
		lbAvatar = new JLabel("GAME TIEN LEN MIEN NAM");
		lbAvatar.setBounds(1100, 210, 150, 30);

		lbUsername = new JLabel("Username");
		lbUsername.setBounds(100, 50,100,50);
		tfUsername=new TextField();
		tfUsername.setBounds(250,50, 300,30);
		
		lbPass1=new JLabel("Password");
		lbPass1.setBounds(100,130, 100, 50);
		tfPass1=new TextField();
		tfPass1.setBounds(250, 130, 300,30);
		lbConfirmPassword=new JLabel("Confirm Password");
		lbConfirmPassword.setBounds(100,210,100,50);
		tfConfirmPassword=new TextField();
		tfConfirmPassword.setBounds(250, 210, 300, 30);
		
		lbPass2=new JLabel("Password2");
		lbPass2.setBounds(100,290,100,50);
		tfPass2=new TextField();
		tfPass2.setBounds(250,290,300,30);
		
		btdangky=new Button("Dangky");
		btdangky.addActionListener(this);
		btdangky.setBounds(349, 340, 50, 30);
		bthuy=new Button("huy");
		bthuy.addActionListener(this);
		bthuy.setBounds(401, 340, 50, 30);
		
		
		pn1.add(lbUsername);
		pn1.add(tfUsername);
		pn1.add(lbPass1);
		pn1.add(tfPass1);
		pn1.add(lbConfirmPassword);
		pn1.add(tfConfirmPassword);
		pn1.add(lbPass2);
		pn1.add(tfPass2);
		pn1.add(lbAvatar);
		pn1.add(btAvatar);
		
		pn1.add(btdangky);
		pn1.add(bthuy);
		add(pn1);
		
		
	}
	public GuiRegister(Game game, Point location) {
		super(game, location);
		setTitle("Register");
		setGui();

		processor = new ProcessorRegister(this);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btdangky)
		{
			((ProcessorRegister) processor).Register();
		}
		if(e.getSource()==bthuy)
		{
			((ProcessorRegister) processor).cancel();
		}

	}

}
