package oo.processor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import oo.gui.Gui;
import oo.gui.GuiPlay;
import oo.gui.GuiWaitRoom;
import oo.gui.element.Room;

public class ProcessorGuiWaitRoom extends Processor {

	public ProcessorGuiWaitRoom(Gui gui) {
		super(gui);
	}

	/* Ham xu ly nut tao phong */
	public void createRoom(String RoomName) {
		
		/* Gui message tao phong len server 
		 * Cau truc message: "CreateRoom@TenPhong"
		 */
		getConnection().sendMessage("CreateRoom@" + RoomName);
		
		/* Nhan message tu server 
		 * Cau truc message:
		 * "OK"
		 * "ERROR"
		 */
		String message = getConnection().receiveMessage();
		
		/* Xu ly message */
		if(message.equals("OK")){
			new GuiPlay(getGame(), getGuiLocation());
			((GuiWaitRoom) gui).dispose();
			//refreshRooms();
		}
		else{
			JOptionPane.showMessageDialog(getGui(),
					"Trung ten phong", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		// Thử gui wait
	}

	/* Ham lay danh sach phong tu Server */
	public void getRooms() {
		
		/* Gui message len server 
		 * Cau truc message:
		 * "GetRooms@NONE"
		 */
		getConnection().sendMessage("GetRooms@NONE");
		
		/* Nhan message tu server 
		 * Cau truc message:
		 * "NONE" - ko co phong nao
		 * "TenRoom1:TenRoom2: ... :TenRoomn" - danh sach ten cac phong hien co
		 */
		String message = getConnection().receiveMessage();
		
		if(!message.equals("NONE")){
			//Luu danh sach ten phong vao GuiWaitRoom.roomnames
			((GuiWaitRoom) gui).roomnames = message.split(":");
		}
		else {
			((GuiWaitRoom) gui).roomnames = new String[0];
		}
	}

	public void refreshRooms() {
		//Toa do x, y de ve cac phong len pnRooms
		int x = 50;
		int y = 10;

		/* Lay danh sach phong */
		getRooms();
		
		/* Xoa het cac phong da ve len pnRooms */
		((GuiWaitRoom) gui).pnRooms.removeAll();
		
		/* Duyet va ve cac phong luu trong roomnames[] */
		System.out.println("So phong : " + ((GuiWaitRoom) gui).roomnames.length);
		for (int i = 0; i < ((GuiWaitRoom) gui).roomnames.length; i++) {
			
			/* Khoi tao 1 phong tai toa do x,y voi ten phong la roomnames[i] */
			((GuiWaitRoom) gui).rooms[i] = new Room(x, y, ((GuiWaitRoom) gui).roomnames[i]);
			
			/* Gan su kien click chuot cho phong vua tao */
			final Room tmp = ((GuiWaitRoom) gui).rooms[i];
			tmp.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					
					/* Gui message len server 
					 * Cau truc message:
					 * "JoinRoom@TenPhong"
					 */
					getConnection().sendMessage("JoinRoom@" + tmp.getName());
					
					/* Nhan message tu server 
					 * Cau truc message:
					 * "OK"
					 * "ERROR"
					 */
					String message = getConnection().receiveMessage();
					
					/* Xu ly message nhan duoc */
					if(message.equals("OK")) {
						new GuiPlay(getGame(), getGuiLocation());
						((GuiWaitRoom) gui).dispose();
					}
					else {
						JOptionPane.showMessageDialog(getGui(),
								"Loi, Khong vao phong duoc", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					
					
				}
			});
			
			/* Ve phong len pnRooms */
			((GuiWaitRoom) gui).pnRooms.add(((GuiWaitRoom) gui).rooms[i]);
			
			/* Chinh lai toa do ve */
			if ((i + 1) % 5 != 0) { // Moi dong ve toi da 5 room
				x += 100;
			} else {
				x = 50;
				y += 100;
			}
		}
		
		/* Repaint lai pnRooms */
		((GuiWaitRoom) gui).pnRooms.repaint();

	}

}
