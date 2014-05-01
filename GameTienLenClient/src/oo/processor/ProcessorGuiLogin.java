package oo.processor;

import javax.swing.JOptionPane;

import oo.gui.Gui;
import oo.gui.GuiRegister;
import oo.gui.GuiWaitRoom;
import oo.gui.GuiLogin;

public class ProcessorGuiLogin extends Processor {

	public ProcessorGuiLogin(Gui gui) {
		super(gui);
	}

	/* Ham xu ly button Login */
	public void login() {
		if (!((GuiLogin) gui).tfUsername.getText().equals("")
				&& !((GuiLogin) gui).tfPassword.getText().equals("")) {
			/* ket noi den server */
			getConnection().connect();

			/* Tao message gui username va pass len server 
			 * Cau truc message "Login@USERNAME:PASSWORD" 
			 */
			String message = "Login@" + ((GuiLogin) gui).tfUsername.getText() + ":" + ((GuiLogin) gui).tfPassword.getText();

			/* Gui message len server */
			getConnection().sendMessage(message);

			/* Nhan message tra ve cua server
			 * Cau truc message: 
			 * "OK@ID" 	- Dang nhap thanh cong, ID la so nguyen trong CSDL
			 * "ERROR"	- Dang nhap that bai
			 */
			message = getConnection().receiveMessage();
			
			/* Tach message ra lam hai mang */
			String[] data = message.split("@");
			
			if (data[0].equals("OK")) {
				/* Luu ID vao player.id */
				getPlayer().id = Integer.parseInt(data[1]);
				
				/* Luu lai username tu textfield vao player.username */
				getPlayer().username = ((GuiLogin) gui).tfUsername.getText();
				
				/* Nhan avatar tu server luu vao avatar cua player */
				getPlayer().avatar = getConnection().receiveImage();
				
				/* Mo giao dien WaitRoom va dong giao dien hien tai lai */		
				new GuiWaitRoom(((GuiLogin) gui).getGame(), getGuiLocation());	// Mo giao dien WaitRoom
				((GuiLogin) gui).dispose(); 									// Xóa giao diện Login
			} else {
				JOptionPane.showMessageDialog(getGui(),
						"Sai Username hoặc Password", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			
		} else
			JOptionPane.showMessageDialog(getGui(),
					"Phải điền đầy đủ Username và Password", "Error",
					JOptionPane.ERROR_MESSAGE);
	}

	/* Ham xu ly button Register */
	public void register() {
		new GuiRegister(((GuiLogin) gui).getGame(), getGuiLocation());	// Mo giao dien Register
		((GuiLogin) gui).dispose(); 									// Xóa giao diện login
	}
}
