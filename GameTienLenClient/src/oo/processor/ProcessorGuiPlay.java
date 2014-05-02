package oo.processor;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import oo.connection.InforPlayer;
import oo.connection.InforRoom;
import oo.gui.Gui;
import oo.gui.GuiPlay;
import oo.gui.element.TheBai;

public class ProcessorGuiPlay extends Processor implements Runnable {

	private boolean pressReady;
	private boolean pressHitCard;
	private boolean pressSkipTurn;
	private String AssaulterCards[];
	private int OrderNumber;
	private int maxPlayer;

	String[] PlayersNames;//

	public ProcessorGuiPlay(Gui gui, int OrderNumber) {
		super(gui);
		this.OrderNumber = OrderNumber;
		pressReady = false;
		pressHitCard = false;
		pressSkipTurn = false;

	}

	@Override
	public void run() {

		String strChat;

		// nhan va xu ly thong diep tu server
		while (true) {
			// nhan thong diep tu server
			String mes = getConnection().receiveMessage();
			System.out.println("receive: " + mes);
			strChat = mes;

			// phan ra thong diep nhan duoc
			mes = mes.trim();
			String[] s = mes.split("@");

			if ("InforRoom".equals(s[0])) {
				InforRoom inforRoom = getConnection().receiveInforRoom();
				this.maxPlayer = inforRoom.inforPlayers.length;
				displayRoom(inforRoom);
			}

			// nhan tin hieu san sang tu server
			if ("Ready".equals(s[0])) {// ok
				displyReady(s[1]);
			}

			// nhan cac quan bai
			if ("DealCards".equals(s[0])) {// ok
				showCards(s[1]);
			}

			if ("NewTurn".equals(s[0])) {// ok
				clearTableCards();
				if(Integer.parseInt(s[1]) == OrderNumber)
					((GuiPlay) gui).btHitCards.setEnabled(true);
				
			}

			// nhan quan bai rac
			if ("HitCards".equals(s[0])) {// ok
				showHitCards(s[1]);
			}
			
			if ("ResultHitCards".equals(s[0])) {// ok
				if (!s[1].equals("OK")) {
					JOptionPane.showMessageDialog(gui, "Bạn không thể đánh",
							"Lỗi đánh bài", JOptionPane.INFORMATION_MESSAGE);
				}
			}

			// neu nhan tin hieu boc bai
			if ("Turn".equals(s[0])) {// ok
				((GuiPlay) gui).btHitCards.setEnabled(true);
				((GuiPlay) gui).btSkipTurn.setEnabled(true);
			}

			// nhan tin hieu chuan bi
			if ("PrepareNewGame".equals(s[0])) {// ok
				resetAll();
			}

			if ("Chat".equals(s[0])) {
				((GuiPlay) gui).txtContent.append(strChat.substring(5) + "\n");
			}

			if ("Over".equals(s[0])) {// ok
				showOver(s[0]);
			}
		}
	}

	public void winBai() {
		JOptionPane.showMessageDialog(gui,
				"Bạn đã chiến thắng ở ván này...", "Thông Báo",
				JOptionPane.ERROR_MESSAGE);
		for (int i = 0; i < ((GuiPlay) gui).handcards1.length; i++) {
			((GuiPlay) gui).handcards1[i].setVisible(true);
		}
		downAll();
	}
	
	private void showOver(String s) {
		String[] data = s.split(":");
		
		if ((4 + Integer.parseInt(data[0]) - OrderNumber) % 4 == 0) {
			((GuiPlay) gui).lbMessage[0].setText("Về thứ " + Integer.parseInt(data[1]));
			JOptionPane.showMessageDialog(gui,
					"Về thứ " + Integer.parseInt(data[1]), "Thông Báo",
					JOptionPane.INFORMATION_MESSAGE);
		}
		((GuiPlay) gui).lbMessage[(4 + Integer.parseInt(data[0]) - OrderNumber) % 4].setText("Về thứ " + Integer.parseInt(data[1]));	
	}

	private void displayRoom(InforRoom inforRoom) {
		for (int i = 0; i < inforRoom.inforPlayers.length; i++) {
			InforPlayer inforPlayer = inforRoom.inforPlayers[i];
			if (inforPlayer != null) {
				((GuiPlay) gui).lbUsername[(maxPlayer + i - OrderNumber)
						% maxPlayer].setText(inforPlayer.UserName);
				((GuiPlay) gui).avatarsPlayer[(maxPlayer + i - OrderNumber)
						% maxPlayer].setIcon(inforPlayer.Avatar);
			}
		}
	}

	// bo chon, ha cac quan bai xuong
	public void downAll() {
		for (int i = 0; i < ((GuiPlay) gui).handcards1.length; i++) {
			((GuiPlay) gui).handcards1[i].setBounds();
		}
	}

	// gui message chat
	public void sendChat() {
		if (!((GuiPlay) gui).txtChat.getText().equals("")) {
			String msgChat = "Chat@" + getPlayer().username + ": "
					+ ((GuiPlay) gui).txtChat.getText();
			getConnection().sendMessage(msgChat);
			System.out.println("send: " + msgChat);
		} else {
			JOptionPane.showMessageDialog(getGui(),
					"Bạn phải nhập nội dung gửi ...", "Thông Báo",
					JOptionPane.ERROR_MESSAGE);
		}
		((GuiPlay) gui).txtChat.setText("");
	}

	// xu ly nut ready
	public void ready() {
		this.pressReady = true;
		sendReady();
	}

	public void sendReady() {
		if (this.pressReady == true) {
			this.pressReady = false;
			((GuiPlay) gui).btReady.setEnabled(false);
			getConnection().sendMessage("Ready@NONE");
		}
	}

	// xu ly nut boluot
	public void skipTurn() {
		getConnection().sendMessage("SkipTurn@NONE");
		((GuiPlay) gui).btHitCards.setEnabled(false);
		((GuiPlay) gui).btSkipTurn.setEnabled(false);
	}

	public void hitCards() {
		String cards = "";
		// lấy danh sách các lá bài đã chọn: "bai1_bai2_..._bain"

		for (int i = 0; i < ((GuiPlay) gui).handcards1.length; i++) {
			if (((GuiPlay) gui).handcards1[i].isClicked == true) {
				this.pressHitCard = true;
				if (!cards.equals("")) {
					cards += "_";
				}
				cards += ((GuiPlay) gui).handcards1[i].cardnumber;
			}
		}

		if (cards.equals("")) {
			JOptionPane.showMessageDialog(gui, "Hãy chọn bài", "Lỗi đánh bài",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		getConnection().sendMessage("HitCards@" + cards);
	}

	// chuyen doi thanh cac chuoi bai
	public String[] parseCards(String cards) {
		cards = cards.trim();
		return cards.split("_");
	}

	// hien thi cac nguoi choi san sang
	public void displyReady(String mes) {
		mes = mes.trim();
		String[] listReady = mes.split(" ");

		boolean allReady = true;

		// hien thi ten nguoi choi len giao dien
		for (int i = 0; i < maxPlayer; i++) {
			if (listReady[i].equals("true")) {
				((GuiPlay) gui).lbMessage[(maxPlayer + i - OrderNumber)
						% maxPlayer].setText("Sẵn Sàng");
			} else {
				allReady = false;
			}
		}

		// khi 4 nguoi choi deu san sang thi xoa chu San sang
		if (allReady) {
			for (int i = 0; i < 4; i++) {
				((GuiPlay) gui).lbMessage[i].setText("");
			}
		}
	}

	public String[] sortCards(String[] cards) {
		String temp = "";
		for (int i = 0; i < cards.length; i++) {
			for (int j = 0; j <= i; j++) {
				if (Integer.parseInt(cards[j]) < Integer.parseInt(cards[i])) {
					temp = cards[i];
					cards[i] = cards[j];
					cards[j] = temp;
				}
			}
		}
		return cards;
	}

	// hien tung quan bai cua nguoi choi luc moi chia
	public void showCards(String mes) {
		String[] cards = parseCards(mes);
		sortCards(cards);
		for (int i = 0; i < 13; i++) {
			((GuiPlay) gui).handcards1[i].cardnumber = cards[i];
			((GuiPlay) gui).handcards1[i].setImage(cards[i]);
			((GuiPlay) gui).handcards2[i].setImage("100");
			((GuiPlay) gui).handcards3[i].setImage("100");
			((GuiPlay) gui).handcards4[i].setImage("100");
		}
	}

	// hien thi quan bai rac
	public void showHitCards(String mes) {

		String[] data = mes.split(":");
		// don het cac quan bai da danh xuong cua nguoi choi truoc do
		clearTableCards();

		switch ((4 + Integer.parseInt(data[0]) - OrderNumber) % 4) {

		case 0:
			// khong hien thi cac quan bai da chon tren tay nguoi choi
			for (int i = 0; i < ((GuiPlay) gui).handcards1.length; i++) {
				if (((GuiPlay) gui).handcards1[i].isClicked == true) {
					((GuiPlay) gui).handcards1[i].isClicked = false;
					((GuiPlay) gui).handcards1[i].setVisible(false);
					((GuiPlay) gui).handcards1[i].cardnumber = null;
					((GuiPlay) gui).handcards1[i].setIcon(null);
				}
			}
			showTableCards(0, data[1]);
			break;

		case 1:
			showTableCards(1, data[1]);
			break;

		case 2:
			showTableCards(2, data[1]);
			break;

		case 3:
			showTableCards(3, data[1]);
			break;
		}
		
		((GuiPlay) gui).btHitCards.setEnabled(false);
		((GuiPlay) gui).btSkipTurn.setEnabled(false);
	}

	private void clearTableCards() {
		for (int i = 0; i < 13; i++) {
			((GuiPlay) gui).tablecards1[i].setIcon(null);
			((GuiPlay) gui).tablecards2[i].setIcon(null);
			((GuiPlay) gui).tablecards3[i].setIcon(null);
			((GuiPlay) gui).tablecards4[i].setIcon(null);
		}
	}

	private void showTableCards(int location, String listCards) {
		String[] cards = parseCards(listCards);
		
		switch(location) {
		case 0:
			for (int i = 0; i < 13; i++) {
				if (((GuiPlay) gui).tablecards1[i].getIcon() == null
						&& i < cards.length) {
					((GuiPlay) gui).tablecards1[i].setImage(cards[i]);
				}
			}
			break;
		
		case 1:
			for (int i = 0; i < 13; i++) {
				if (((GuiPlay) gui).tablecards2[i].getIcon() == null
						&& i < cards.length) {
					((GuiPlay) gui).tablecards2[i].setImage(cards[i]);
				}
			}
			break;
			
		case 2:
			for (int i = 0; i < 13; i++) {
				if (((GuiPlay) gui).tablecards3[i].getIcon() == null
						&& i < cards.length) {
					((GuiPlay) gui).tablecards3[i].setImage(cards[i]);
				}
			}
			break;
			
		case 3:
			for (int i = 0; i < 13; i++) {
				if (((GuiPlay) gui).tablecards4[i].getIcon() == null
						&& i < cards.length) {
					((GuiPlay) gui).tablecards4[i].setImage(cards[i]);
				}
			}
			break;
		}	
	}

	// reset cac bien giao dien
	public void resetAll() {
		// cac the bai cua nguoi choi hien tai
		for (int i = 0; i < ((GuiPlay) gui).handcards1.length; i++) {
			((GuiPlay) gui).handcards1[i].setIcon(null);
			((GuiPlay) gui).handcards2[i].setIcon(null);
			((GuiPlay) gui).handcards3[i].setIcon(null);
			((GuiPlay) gui).handcards4[i].setIcon(null);
		}

		// cac nut dieu khien
		((GuiPlay) gui).btHitCards.setEnabled(false);
		pressHitCard = false;
		((GuiPlay) gui).btSkipTurn.setEnabled(false);
		pressSkipTurn = false;
		((GuiPlay) gui).btReady.setEnabled(true);
		pressReady = false;

		// cac the bai rac cua nguoi choi
		for (int i = 0; i < ((GuiPlay) gui).tablecards1.length; i++) {
			((GuiPlay) gui).tablecards1[i].setIcon(null);
			((GuiPlay) gui).tablecards2[i].setIcon(null);
			((GuiPlay) gui).tablecards3[i].setIcon(null);
			((GuiPlay) gui).tablecards4[i].setIcon(null);
		}

	}

}
