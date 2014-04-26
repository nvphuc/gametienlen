package oo.processor;

import javax.swing.JOptionPane;

import oo.gui.Gui;
import oo.gui.GuiLogin;
import oo.gui.GuiRegister;
import oo.gui.GuiWaitRoom;

public class ProcessorRegister extends Processor{

	public ProcessorRegister(Gui gui) {
		super(gui);
		// TODO Auto-generated constructor stub
		
	}

	public void Register() { 
		getConnection().connect();
		String message = "Register@";
		message+=((GuiRegister) gui).tfUsername.getText()+":"
		+((GuiRegister) gui).tfPass1.getText()+":"+((GuiRegister) gui).tfConfirmPassword.getText()+":"
				+((GuiRegister) gui).tfPass2.getText();
		
		getConnection().sendMessage(message);
		message=getConnection().receiveMessage();
		String[] data = message.split("@");
		
		if (data[0].equals("OK")) {
			/* Luu ID vao player.id */
			getPlayer().id = Integer.parseInt(data[1]);
			
			/* Luu lai username tu textfield vao player.username */
			getPlayer().username = ((GuiLogin) gui).tfUsername.getText();
			
			/* Nhan avatar tu server luu vao avatar cua player */
			//getPlayer().avatar = getConnection().receiveImage();
			
			/* Mo giao dien WaitRoom va dong giao dien hien tai lai */		
			new GuiWaitRoom(((GuiLogin) gui).getGame(), getGuiLocation());	// Mo giao dien WaitRoom
			((GuiLogin) gui).dispose(); 									// Xóa giao diện Login
		} else {
			JOptionPane.showMessageDialog(getGui(),
					" Tai khoan da ton tai", "Error",
					JOptionPane.ERROR_MESSAGE);
		   }
		
	}

	public void cancel() {
		new GuiLogin(((GuiRegister) gui).getGame(), getGuiLocation());	// Mo giao dien WaitRoom
		((GuiRegister) gui).dispose(); 
		// TODO Auto-generated method stub
		
	}
	
	

}
