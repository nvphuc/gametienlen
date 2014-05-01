package oo.gui;

import game.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import oo.gui.element.Room;
import oo.processor.ProcessorGuiWaitRoom;

public class GuiWaitRoom extends Gui {
	
	public Room[] rooms = new Room[100];
	public JButton btCreateRoom;
	public JPanel pnPlayer, pnRooms;
	public JLabel lbAvatar, lbUsername;
	public String[] roomnames = new String[20];

	public GuiWaitRoom(Game game, Point location) {
		super(game, location);
		setTitle("WaitRoom");
		GuiId = 1;
		processor = new ProcessorGuiWaitRoom(this);	

		setGui();
		setVisible(true);
	}
	
	@Override
	public void setGui() {
		setLayout(new BorderLayout());
		
		pnPlayer = new JPanel(new FlowLayout());
		
		lbAvatar = new JLabel(processor.getPlayer().avatar);
		pnPlayer.add(lbAvatar);
		
		lbUsername = new JLabel(processor.getPlayer().username);
		pnPlayer.add(lbUsername);
		
		pnRooms = new JPanel();
		pnRooms.setLayout(null);
		
		btCreateRoom = new JButton("Tạo Phòng");
		btCreateRoom.addActionListener(this);
		
		pnRooms.setBackground(Color.blue);
		
		add(pnPlayer, BorderLayout.NORTH);
		add(pnRooms, BorderLayout.CENTER);
		add(btCreateRoom, BorderLayout.SOUTH);
		
		((ProcessorGuiWaitRoom) processor).refreshRooms();
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {	
		((ProcessorGuiWaitRoom) processor).createRoom();	
	}

}
