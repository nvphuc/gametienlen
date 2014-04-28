package oo.processor;


import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import oo.gui.Gui;
import oo.gui.GuiPlay;
import oo.gui.element.TheBai;

public class ProcessorGuiPlay extends Processor implements Runnable {

	boolean nhanReady;
	boolean nhanDanhBai;
	boolean nhanBoLuot;
	private String arrBai[];
	String[] othername;

	public ProcessorGuiPlay(Gui gui) {
		super(gui);
		nhanReady = false;
		nhanDanhBai = false;
		nhanBoLuot = false;
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
		} else {
			JOptionPane.showMessageDialog(getGui(),
					"Bạn phải nhập nội dung gửi ...", "Thông Báo",
					JOptionPane.ERROR_MESSAGE);
		}
		((GuiPlay) gui).txtChat.setText("");
	}

	// xu ly nut ready
	public void ready() {
		this.nhanReady = true;
		sendReady();
	}

	public void sendReady() {
		if (this.nhanReady == true) {
			this.nhanReady = false;
			((GuiPlay) gui).btReady.setEnabled(false);
			getConnection().sendMessage("Ready@NONE");
		}
	}

	// xu ly nut boluot
	public void boLuot() {
		getConnection().sendMessage("SkipTurn@" + 1);// sua
		((GuiPlay) gui).btHitCards.setEnabled(false);
		((GuiPlay) gui).btSkipTurn.setEnabled(false);
	}

	public void danhBai() {
		String cards = "";
		// kiem tra xem da co quan nao duoc chon hay chua
		for (int i = 0; i < ((GuiPlay) gui).handcards1.length; i++) {
			if (((GuiPlay) gui).handcards1[i].isClicked == true) {
				this.nhanDanhBai = true;
				if (!cards.equals("")) {
					cards += "_";
				}
				cards += ((GuiPlay) gui).handcards1[i].cardnumber;
			}
		}
		if (this.arrBai == null || this.arrBai.length == 0) {
			if (!cards.equals("")
					&& !checkBaiDanh(parseCards(cards)).equals("loi")) {
				getConnection().sendMessage(
						"sobaidanh " + parseCards(cards).length);
				getConnection().sendMessage("danhbai " + cards);
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
		if (parseCards(cards).length != this.arrBai.length) {
			if (this.arrBai.length == 1) {
				if (Integer.parseInt(this.arrBai[0]) / 4 == 13
						&& checkBaiDanh(parseCards(cards)).equals("tuquy")) {
					getConnection().sendMessage(
							"sobaidanh " + parseCards(cards).length);
					getConnection().sendMessage("danhbai " + cards);
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
					.parseInt(this.arrBai[0])
					|| !checkBaiDanh(parseCards(cards)).equals(
							checkBaiDanh(this.arrBai))) {
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
				getConnection().sendMessage("danhbai " + cards);
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

	@Override
	public void run() {
		String strChat;

		// nhan va xu ly thong diep tu server
		while (true) {
			// nhan thong diep tu server
			String mes = getConnection().receiveMessage();
			strChat = mes;

			// phan ra thong diep nhan duoc
			mes = mes.trim();
			String[] s = mes.split("@");

			// nhan ten cua nguoi choi
			if ("NamePlayers".equals(s[0])) {
				displayName(s[1]);
			}

			// nhan so thu tu cua nguoi choi
			if ("OrderNumber".equals(s[0])) {// ok
				getPlayer().ordernumber = Integer.parseInt(s[1]);
			}

			// nhan tin hieu san sang tu server
			if ("Ready".equals(s[0])) {// ok
				displyReady(s[1]);
			}

			// nhan cac quan bai
			if ("bai".equals(s[0])) {// ok
				System.out.println("Test bai : " + s[1]);
				displayBai(s[1]);
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
			if ("chuanbi".equals(s[0])) {// ok
				resetAll();
			}

			// nhan tin hieu an bai
			if ("anbai".equals(s[0])) {// ok
				displayAnbai();
			}

			if (strChat.substring(0, 4).equals("Chat")) {
				((GuiPlay) gui).txtContent.append(strChat.substring(5) + "\n");
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

	// hien thi ten nguoi choi
	public void displayName(String mes) {
		mes = mes.trim();
		String[] s = mes.split(" ");
		// mang chua ten nguoi choi voi phan tu dau tien la nguoi choi ung voi
		// clien hien tai
		String name[] = new String[4];
		// mang chua so hieu cua nguoi choi voi phan tu dau chua so hieu cua
		// client hien tai
		int number[] = new int[4];

		// tim phan tu chua ten ung voi client hien tai trong thong diep tra ve
		int danhdau = 0;
		for (int i = 0; i < 4; i++) {
			if (s[i].equals(getPlayer().username)) {
				danhdau = i;
				break;
			}
		}

		// sap xep lai thu tu cac nguoi choi de hien thi len giao dien
		for (int i = danhdau; i < s.length; i++) {
			name[i - danhdau] = s[i];
			number[i - danhdau] = i;
		}
		for (int i = 0; i <= danhdau - 1; i++) {
			name[i - danhdau + 4] = s[i];
			number[i - danhdau + 4] = i;
		}

		// luu ten tat ca nguoi choi theo vi tri cua nguoi hien tai
		this.othername = name;

		// hien thi ten nguoi choi va anh avarta len giao dien
		for (int i = 0; i < s.length; i++) {
			if (!(name[i].equals("*"))) {
				((GuiPlay) gui).lbUsername[i].setText(name[i]);
				//((GuiPlay) gui).avatarplayer[i].setIcon(bb.class
				//		.getResource("/images/" + number[i] + ".jpg")));
				Image img = Toolkit.getDefaultToolkit().getImage("images\\" + number[i] + ".jpg");		
				((GuiPlay) gui).avatarsPlayer[i].setIcon(new ImageIcon(img));
			}
		}
	}

	// hien thi cac nguoi choi san sang
	public void displyReady(String mes) {
		mes = mes.trim();
		String[] s = mes.split(" ");

		int danhdau = getPlayer().ordernumber;

		String name[] = dichMang(s, danhdau);

		boolean allReady = true;

		// hien thi ten nguoi choi len giao dien
		for (int i = 0; i < s.length; i++) {
			if (name[i].equals("true")) {
				((GuiPlay) gui).lbMessage[i].setText("Sẵn Sàng");
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

	// dich mang
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

	// hien thi cac quan bai cua nguoi choi
	public void displayBai(String mes) {
		mes = mes.trim();
		String[] s = mes.split("@");
		s[0] = s[0].trim();
		String[] bai = s[0].split(" ");
		s[1] = s[1].trim();
		String[] win = s[1].split(" ");
		int danhdau = getPlayer().ordernumber;
		win = dichMang(win, danhdau);
		showCards(sapXep(bai), win);
	}

	public String[] sapXep(String bai[]) {
		String temp = "";

		for (int i = 0; i < bai.length; i++) {
			for (int j = 0; j <= i; j++) {
				if (Integer.parseInt(bai[j]) < Integer.parseInt(bai[i])) {
					temp = bai[i];
					bai[i] = bai[j];
					bai[j] = temp;
				}
			}
		}
		return bai;
	}

	// hien tung quan bai cua nguoi choi luc moi chia
	public void showCards(String bai[], String win[]) {
		// hien thi cac quan bai cua nguoi choi hien tai
		for (int i = 0; i < 13; i++) {
			((GuiPlay) gui).handcards1[i].cardnumber = bai[i];
			((GuiPlay) gui).handcards1[i].setImage(bai[i]);
			((GuiPlay) gui).handcards2[i].setImage("100");
			((GuiPlay) gui).handcards3[i].setImage("100");
			((GuiPlay) gui).handcards4[i].setImage("100");
		}
		for (int i = 0; i < 4; i++) {
			if ("true".equals(win[i])) {
				switch (i) {
				case 0:
					((GuiPlay) gui).handcards1[9].cardnumber = bai[9];
					((GuiPlay) gui).handcards1[9].setImage(bai[9]);
					break;
				case 1:
					((GuiPlay) gui).handcards2[9].setImage("100");
					break;
				case 2:
					((GuiPlay) gui).handcards3[9].setImage("100");
					break;
				case 3:
					((GuiPlay) gui).handcards4[9].setImage("100");
					break;
				}
				break;
			}
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
		this.nhanDanhBai = false;
		((GuiPlay) gui).btHitCards.setEnabled(false);
		for (int i = 0; i < ((GuiPlay) gui).handcards1.length; i++) {
			if (((GuiPlay) gui).handcards1[i].isClicked == true) {
				((GuiPlay) gui).handcards1[i].isClicked = false;//ly do loi
				((GuiPlay) gui).handcards1[i].setVisible(false);
				((GuiPlay) gui).handcards1[i].cardnumber = null;
				((GuiPlay) gui).handcards1[i].setIcon(null);
			}
		}

		String[] s = mes.split("@");
		for (int i = 0; i < 4; i++) {
			if (s[0].equals(this.othername[i])) {
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
		this.arrBai = listRac;
		if (this.arrBai == null || this.arrBai.length == 0)
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
		this.arrBai = listRac;
		if (this.arrBai == null || this.arrBai.length == 0)
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
		this.arrBai = listRac;
		if (this.arrBai == null || this.arrBai.length == 0)
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
		this.arrBai = listRac;
		if (this.arrBai == null || this.arrBai.length == 0)
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
		nhanDanhBai = false;
		((GuiPlay) gui).btSkipTurn.setEnabled(false);
		nhanBoLuot = false;
		((GuiPlay) gui).btReady.setEnabled(true);
		nhanReady = false;

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
