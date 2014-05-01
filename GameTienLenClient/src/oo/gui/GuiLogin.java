package oo.gui;

import game.Game;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import oo.processor.ProcessorGuiLogin;

public class GuiLogin extends Gui {

	public JLabel lbUsername, lbPassword;		//giu im
	public JTextField tfUsername, tfPassword;	//giu im
	public JButton btLogin, btRegister;			//giu im
	JPanel panel;

	public GuiLogin(Game game, Point location) {

		super(game, location);
		setTitle("Login");
		GuiId = 0;
		processor = new ProcessorGuiLogin(this);			
		setGui();
	}

	@Override
	public void setGui() {
		JPanel panel = new JPanel();
		panel.setLayout(null);

		lbUsername = new JLabel("User Name");
		lbUsername.setBounds(0, 0, 100, 50);
		panel.add(lbUsername);

		lbPassword = new JLabel("Password");
		lbPassword.setBounds(0, 70, 100, 50);
		panel.add(lbPassword);

		tfUsername = new JTextField();
		tfUsername.setBounds(150, 0, 150, 50);
		panel.add(tfUsername);

		tfPassword = new JTextField();
		tfPassword.setBounds(150, 70, 150, 50);
		panel.add(tfPassword);

		btLogin = new JButton("Login");
		btLogin.setBounds(0, 200, 100, 50);
		btLogin.addActionListener(this);
		panel.add(btLogin);

		btRegister = new JButton("Register");
		btRegister.setBounds(150, 200, 100, 50);
		btRegister.addActionListener(this);
		panel.add(btRegister);

		add(panel);

		setVisible(true);	
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == btLogin) {
			((ProcessorGuiLogin) processor).login();
		}
		
		if (e.getSource() == btRegister) {
			((ProcessorGuiLogin) processor).register();
		}
		
	}
}