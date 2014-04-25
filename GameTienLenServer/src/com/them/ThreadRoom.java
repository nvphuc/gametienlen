package com.them;

import java.io.IOException;
import java.util.Vector;

public class ThreadRoom extends Thread {
	public Server server;
	public Vector<ThreadPlayer> players;
	public String roomname;

	//=============================
	public int soHaphom = 0;
	public int soReady = 0;  //dem so nguoi choi da san sang
	public int soKetNoi = 0; //dem so luong ket noi dc gui den server	
	public int nguoidautien=0;
	public int nguoiwin=0;
	
	public ThreadRoom(Server server, String name) {
		this.server = server;
		roomname = name;
		players = new Vector<ThreadPlayer>();
	}

	
	
	
	public void run() {
		while (true) {
			while(soKetNoi != 4){
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (soHaphom == 4) {
				// luc bat dau luot choi reset cac gia tri cua nguoi choi
				for (int i = 0; i < players.size(); i++) {
					players.get(i).resetAll();
				}

				// gui tin hieu chuan bi ve cho client de thiet lap lai cac gia
				// tri duoi client
				for (int i = 0; i < players.size(); i++) {
					players.get(i).sendChuanbi();
				}
			}
			// tam dung 2s
			try {
				Thread.sleep(2000);
			} catch (InterruptedException ex) {
				System.out.println("Loi khi dem giay tam dung");
			}

			while (soReady < 4) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// server thuc hien chia bai--> tao mot bo bai co 52 cay
			ChiaBai bai = new ChiaBai();

			// cho mot nguoi win luc bat dau tro choi
			int dem = 0;
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).win == true) {
					dem++;
				}
			}
			if (dem == 0) {
				players.get(0).win = true;
				nguoidautien = 0;
			}

			// thuc hien chia bai cho cac nguoi choi
			for (int i = 0; i < players.size(); i++) {
				// chia cac quan bai vao mang chua cac quan bai dc gui di
				bai.chiabai(players.get(i).pocker, players.get(i).win, i);
			}

			// server gui cac quan bai xuong client
			for (int i = 0; i < players.size(); i++) {
				try {
					players.get(i).sendBai();
				} catch (IOException ex) {
				}
			}

			// sau khi chia bai xong thi tien hanh danh bai
			// neu so quan bai trong noc bai van con thi van tiep tuc choi
			int ktNguoiConChoi = 0;
			// duyet tung nguoi choi
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).win == true) {
					ktNguoiConChoi = players.get(i).dembaiPlayer();
					// if(ktNguoiConChoi == 0)
					players.get(i).choi();
					break;
				}
			}
			// System.out.print("");

			// cho den khi tat ca nguoi choi deu ha phom
			while (soHaphom != 4) {
				System.out.print("");
			}

			// tam dung 15s
			try {
				Thread.sleep(15000);
			} catch (InterruptedException ex) {
				System.out.println("Loi khi dem giay tam dung");
			}

			// so sanh diem de xac dinh nguoi choi win o vong ke tiep
			// reset cac win ve false
			for (int i = 0; i < players.size(); i++) {
				players.get(i).win = false;
			}

			players.get(nguoiwin).win = true;
			nguoidautien = nguoiwin;
		}
	}

}
