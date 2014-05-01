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

			// nhan tin hieu danh bai
			if ("danhbai".equals(s[0])) {// ok
				((GuiPlay) gui).btHitCards.setEnabled(true);
			}

			// nhan quan bai rac
			if ("rac".equals(s[0])) {// ok
				displayRac(s[1]);
			}

			// neu nhan tin hieu boc bai
			if ("bocbai".equals(s[0])) {// ok
				// ((ProcessorGuiPlay) gui).gameGui.btnBocBai.setEnabled(true);
				nextTurn();
			}

			// nhan tin hieu chuan bi
			if ("PrepareNewGame".equals(s[0])) {// ok
				resetAll();
			}

			// nhan tin hieu an bai
			if ("anbai".equals(s[0])) {// ok
				displayAnbai();
			}

			if (strChat.substring(0, 4).equals("Chat")) {
				((GuiPlay) gui).txtContent.append(strChat.substring(5) + "\n");
			}

			if ("tatboluoc".equals(s[0])) {
				((GuiPlay) gui).btSkipTurn.setEnabled(false);
			}

			if ("tatsauboluoc".equals(s[0])) {// ok
				tatDanhSauBoLuot();
			}
			if ("winbai".equals(s[0])) {// ok
				winBai();
			}
			if ("losebai".equals(s[0])) {// ok
				loseBai();
			}
		}
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
	public void boLuot() {
		getConnection().sendMessage("SkipTurn@" + 1);
		System.out.println("send: SkipTurn@" + 1);
		((GuiPlay) gui).btHitCards.setEnabled(false);
		((GuiPlay) gui).btSkipTurn.setEnabled(false);
	}

	public void hitCards() {
		String cards = "";
		// lấy danh sách các lá bài đã chọn
		for (int i = 0; i < ((GuiPlay) gui).handcards1.length; i++) {
			if (((GuiPlay) gui).handcards1[i].isClicked == true) {
				this.pressHitCard = true;
				if (!cards.equals("")) {
					cards += "_";
				}
				cards += ((GuiPlay) gui).handcards1[i].cardnumber;
			}
		}
		
		if (this.AssaulterCards == null || this.AssaulterCards.length == 0) {
			if (!cards.equals("") && 
				!checkBaiDanh(parseCards(cards)).equals("loi")) {
				getConnection().sendMessage(
						"sobaidanh@" + parseCards(cards).length);
				getConnection().sendMessage("danhbai@" + cards);
				return;
			} else {
				Object[] options = { "OK" };
				int option = JOptionPane.showOptionDialog(gui, "Lỗi Đánh Bài!",
						"Thông Báo!", JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				if (option == 0) {
					return;
				}
			}
		}
		
		if (parseCards(cards).length != this.AssaulterCards.length) {
			if (this.AssaulterCards.length == 1) {
				if (Integer.parseInt(this.AssaulterCards[0]) / 4 == 13
						&& checkBaiDanh(parseCards(cards)).equals("tuquy")) {
					getConnection().sendMessage(
							"sobaidanh " + parseCards(cards).length);
					System.out.println("send: sobaidanh "
							+ parseCards(cards).length);
					getConnection().sendMessage("danhbai@" + cards);
				}
			} else {
				Object[] options = { "OK" };
				int option = JOptionPane.showOptionDialog(gui, "Lỗi Đánh Bài!",
						"Thông Báo!", JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				if (option == 0) {
					return;
				}
			}

		} else {
			if (Integer.parseInt(parseCards(cards)[0]) < Integer
					.parseInt(this.AssaulterCards[0])
					|| !checkBaiDanh(parseCards(cards)).equals(
							checkBaiDanh(this.AssaulterCards))) {
				Object[] options = { "OK" };
				int option = JOptionPane.showOptionDialog(gui, "Lỗi Đánh Bài!",
						"Thông Báo!", JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				if (option == 0) {
					return;
				}
			} else if (!cards.equals("")) {
				getConnection().sendMessage(
						"sobaidanh " + parseCards(cards).length);
				System.out.println("send: sobaidanh "
						+ parseCards(cards).length);
				getConnection().sendMessage("danhbai@" + cards);
			}
		}
		((GuiPlay) gui).btSkipTurn.setEnabled(false);
	}

	// chuyen doi thanh cac chuoi bai
	public String[] parseCards(String cards) {
		cards = cards.trim();
		return cards.split("_");
	}

	// kiem tra cac la bai chon danh xuong thuoc loai nao: le, doi, sanh; co hop
	// le ko
	public String checkBaiDanh(String[] cards) {
		if (cards.length == 1) {
			return "rac";
		} else if (cards.length == 2) {
			if (Integer.parseInt(cards[0]) / 4 == Integer.parseInt(cards[1]) / 4) {
				return "doi";
			} else {
				return "loi";
			}
		} else if (cards.length == 3) {
			for (int i = 0; i < 3; i++) {
				if (Integer.parseInt(cards[i]) / 4 != (Integer
						.parseInt(cards[0]) / 4) - i) {
					for (int j = 0; j < 3; j++) {
						if (Integer.parseInt(cards[j]) / 4 != (Integer
								.parseInt(cards[0]) / 4)) {
							return "loi";
						}
					}
					return "sam";
				}
			}
			if (Integer.parseInt(cards[0]) / 4 == 13) {
				return "loi";
			}
			return "sanh3";
		} else if (cards.length == 4) {
			for (int i = 0; i < 4; i++) {
				if (Integer.parseInt(cards[i]) / 4 != (Integer
						.parseInt(cards[0]) / 4) - i) {
					for (int j = 0; j < 4; j++) {
						if (Integer.parseInt(cards[j]) / 4 != (Integer
								.parseInt(cards[0]) / 4)) {
							return "loi";
						}
					}
					return "tuquy";
				}
			}
			if (Integer.parseInt(cards[0]) / 4 == 13) {
				return "loi";
			}
			return "sanh4";
		} else if (cards.length > 4) {
			for (int i = 0; i < cards.length; i++) {
				if (Integer.parseInt(cards[i]) / 4 != (Integer
						.parseInt(cards[0]) / 4) - i) {
					return "loi";
				}
			}
			if (Integer.parseInt(cards[0]) / 4 == 13) {
				return "loi";
			}
			return "sanh";
		}
		return "sai";
	}

	// hien thi cac nguoi choi san sang
	public void displyReady(String mes) {
		mes = mes.trim();	
		String[] listReady = mes.split(" ");
		
		boolean allReady = true;

		// hien thi ten nguoi choi len giao dien
		for (int i = 0; i < maxPlayer; i++) {
			if (listReady[i].equals("true")) {
				((GuiPlay) gui).lbMessage[(maxPlayer + i - OrderNumber) % maxPlayer].setText("Sẵn Sàng");
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

	public String[] dichMang(String[] s, int danhdau) {
		String name[] = new String[4];

		// sap xep lai thu tu cac nguoi choi de hien thi len giao dien
		for (int i = danhdau; i < s.length; i++) {
			name[i - danhdau] = s[i];
		}
		for (int i = 0; i <= danhdau - 1; i++) {
			name[i - danhdau + 4] = s[i];
		}

		return name;
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
		mes = mes.trim();
		String[] cards = mes.split(" ");
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
	public void displayRac(String mes) {
		mes = mes.trim();

		// neu quan bai nhan dc bi cam thi khong lam j ca
		if ("cam".equals(mes)) {
			return;
		}

		// neu quan bai rac duoc phep danh ra
		this.pressHitCard = false;
		((GuiPlay) gui).btHitCards.setEnabled(false);
		for (int i = 0; i < ((GuiPlay) gui).handcards1.length; i++) {
			if (((GuiPlay) gui).handcards1[i].isClicked == true) {
				((GuiPlay) gui).handcards1[i].isClicked = false;// ly do loi
				((GuiPlay) gui).handcards1[i].setVisible(false);
				((GuiPlay) gui).handcards1[i].cardnumber = null;
				((GuiPlay) gui).handcards1[i].setIcon(null);
			}
		}

		String[] s = mes.split(":");
		for (int i = 0; i < 4; i++) {
			if (s[0].equals(this.PlayersNames[i])) {
				switch (i) {
				case 0:
					hienRac1(s[1]);
					break;
				case 1:
					hienRac2(s[1]);
					break;
				case 2:
					hienRac3(s[1]);
					break;
				case 3:
					hienRac4(s[1]);
					break;
				}
				break;
			}
		}
	}

	// hien thi bai rac cua nguoi choi hien tai
	public void hienRac1(String rac) {
		xoaRac1();
		String[] listRac = parseCards(rac);
		this.AssaulterCards = listRac;
		if (this.AssaulterCards == null || this.AssaulterCards.length == 0)
			return;
		for (int i = 0; i < 13; i++) {
			if (((GuiPlay) gui).tablecards1[i].getIcon() == null
					&& i < listRac.length) {
				((GuiPlay) gui).tablecards1[i].setImage(listRac[i]);
			}
		}
	}

	// xoa rac trong lan tiep theo cua nguoi choi 1
	public void xoaRac1() {
		for (int i = 0; i < 13; i++) {
			((GuiPlay) gui).tablecards1[i].setIcon(null);
		}
	}

	// hien thi bai rac cua nguoi choi 2
	public void hienRac2(String rac) {
		xoaRac2();
		String[] listRac = parseCards(rac);
		this.AssaulterCards = listRac;
		if (this.AssaulterCards == null || this.AssaulterCards.length == 0)
			return;
		for (int i = 0; i < 13; i++) {
			if (((GuiPlay) gui).tablecards2[i].getIcon() == null
					&& i < listRac.length) {
				((GuiPlay) gui).tablecards2[i].setImage(listRac[i]);
			}
		}
	}

	public void xoaRac2() {
		for (int i = 0; i < 13; i++) {
			((GuiPlay) gui).tablecards2[i].setIcon(null);
		}
	}

	// hien thi bai rac cua nguoi choi 3
	public void hienRac3(String rac) {
		xoaRac3();
		String[] listRac = parseCards(rac);
		this.AssaulterCards = listRac;
		if (this.AssaulterCards == null || this.AssaulterCards.length == 0)
			return;
		for (int i = 0; i < 13; i++) {
			if (((GuiPlay) gui).tablecards3[i].getIcon() == null
					&& i < listRac.length) {
				((GuiPlay) gui).tablecards3[i].setImage(listRac[i]);
			}
		}
	}

	public void xoaRac3() {
		for (int i = 0; i < 13; i++) {
			((GuiPlay) gui).tablecards3[i].setIcon(null);
		}
	}

	// hien thi bai rac cua nguoi choi 4
	public void hienRac4(String rac) {
		xoaRac4();
		String[] listRac = parseCards(rac);
		this.AssaulterCards = listRac;
		if (this.AssaulterCards == null || this.AssaulterCards.length == 0)
			return;
		for (int i = 0; i < 13; i++) {
			if (((GuiPlay) gui).tablecards4[i].getIcon() == null
					&& i < listRac.length) {
				((GuiPlay) gui).tablecards4[i].setImage(listRac[i]);
			}
		}
	}

	public void xoaRac4() {
		for (int i = 0; i < 13; i++) {
			((GuiPlay) gui).tablecards4[i].setIcon(null);
		}
	}

	public void nextTurn() {
		((GuiPlay) gui).btHitCards.setEnabled(true);
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

	// hien thi nut an bai
	public void displayAnbai() {
		((GuiPlay) gui).btSkipTurn.setEnabled(true);
	}

	public void tatDanhSauBoLuot() {
		((GuiPlay) gui).btHitCards.setEnabled(false);
		((GuiPlay) gui).btSkipTurn.setEnabled(false);
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

	public void loseBai() {
		JOptionPane.showMessageDialog(gui, "Bạn đã thua ván này...",
				"Thông Báo", JOptionPane.ERROR_MESSAGE);
		for (int i = 0; i < ((GuiPlay) gui).handcards1.length; i++) {
			((GuiPlay) gui).handcards1[i].setVisible(true);
		}
		downAll();
	}

}
