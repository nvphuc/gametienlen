package com.them;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadPlayer extends Thread {
	Server server;
	ThreadRoom room;
	String name;
	String nameplayer = "";// p2

	// phu trach ket noi
	public Socket socket;
	public DataInputStream dis;
	public DataOutputStream dos;

	// =======================================them
	boolean ready = false; // danh dau nguoi choi nay da san sang
	boolean isDanhbai = false, isBocbai = false, isAnbai = false;
	int sobairac = 0; // luu so luong bai rac da danh
	int quanbairac; // luu quan bai rac vua dc danh ra
	int kophom[] = new int[10];
	int pocker[] = new int[13]; // chua cac quan bai se dc gui ve client
	int phom[] = new int[10];
	int matranbai[][] = new int[13][4]; // chua ma tran bai cua nguoi choi de
	// tinh phom
	boolean ktmatran[] = new boolean[13]; // danh dau cac dong cam cua matranbai
	static int mangboluoc[] = { 0, 0, 0, 0 };
	int slbaidanh = 0;
	boolean win = false; // bao nguoi choi nay da thang vong choi
	int ordernumber;

	public ThreadPlayer(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		try {
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
		}
	}

	public void run() {
		try {
			String mes = "";
			//String cmd = "";
			//String msg = "";
			while (true) {
				mes = dis.readUTF();
				//cmd = mes.substring(0, mes.indexOf("@"));
				//msg = mes.substring(mes.indexOf("@") + 1);
				
				String[] mesdata = mes.split("@");
				
				switch (mesdata[0]) {

				case "Login":
					System.out.println("Login");
					String[] data = mesdata[1].split(":");
					// Kiem tra trong CSDL tai day
					this.name = data[0];
					this.server.players.add(this);
					dos.writeUTF("OK@1");//
					break;

				case "GetRooms":
					System.out.println("GetRooms");
					dos.writeUTF(getRoomNames());
					break;

				case "CreateRoom":
					System.out.println("CreateRoom");
					ThreadRoom temp = new ThreadRoom(server, mesdata[1]);
					temp.start();
					server.rooms.add(temp);
					room = temp;
					room.players.add(this);
					room.soKetNoi++;
					dos.writeUTF("OK");
					break;

				case "JoinRoom":
					System.out.println("JoinRoom");
					for (int i = 0; i < server.rooms.size(); i++) {
						if (server.rooms.get(i).roomname.equals(mesdata[1]))
							room = server.rooms.get(i);
					}
					room.players.add(this);
					ordernumber = room.soKetNoi;
					room.soKetNoi++;
					dos.writeUTF("OK");
					break;

				/*
				 * case "Msg": for (int i = 0; i < room.players.size(); i++) {
				 * ThreadPlayer temp = room.players.get(i); if (temp != this) {
				 * temp.dos.writeUTF("Msg@" + this.name + ">>" + msg); } }
				 * break;
				 */

				case "OutRoom":
					System.out.println("OutRoom");
					room.players.remove(this);
					room = null;
					dos.writeUTF("OK@none");
					break;
				/*
				 * default: socket.close(); if (room != null) {
				 * room.players.remove(this); } server.players.remove(this);
				 * break;
				 */
				}

				// phan 2

				String[] mang = mes.split(" ");

				if (mang[0].equals("name")) {
					nameplayer = mang[1];
					sendName();
					sendNumber();
					sendReady();
				}

				if (mang[0].equals("ready")) {
					// luu tin hieu san sang
					this.ready = true;
					// tang bien dem so nguoi choi da san sang
					room.soReady++;
					// bao cho tat ca client biet nguoi choi nay da san sang
					this.sendReady();
				}

				if (mang[0].equals("danhbai")) {
					sendRac(mang[1]);
				}

				if (mang[0].equals("CHAT")) {
					sendChat(mes);
				}

				if (mang[0].equals("sobaidanh")) {
					room.players.get(ordernumber).slbaidanh += Integer
							.parseInt(mang[1]);
					System.out.print("so bai danh la: "
							+ Integer.parseInt(mang[1]));
				}

				if (mang[0].equals("anbai")) {
					mangboluoc[ordernumber] = Integer.parseInt(mang[1]);
					for (int i = 0; i < 4; i++)
						System.out.print(mangboluoc[i]);
					sendAnbai();
					// khi boc an thi ko the boc bai
					this.isBocbai = true;
				}
			}
		} catch (IOException e) {
			server.players.remove(this);
		}
	}

	public String getRoomNames() {
		String names = "";
		if (server.rooms.size() > 0) {
			for (int i = 0; i < server.rooms.size() - 1; i++) {
				names += server.rooms.get(i).roomname + ":";
			}
			names += server.rooms.get(server.rooms.size() - 1).roomname;
		} else
			names += "NONE";
		System.out.println(names);//
		return names;
	}

	// p2edited
	// phuong thuc truyen name
	public void sendName() throws IOException {
		String s = "name:";
		// tao chuoi muon gui
		for (int t = 0; t < 4; t++) {
			if (t < room.players.size() && room.players.get(t) != null) {
				s += " " + room.players.get(t).nameplayer;
			} else {
				s += " *";
			}
		}
		// gui ve cac client khac
		for (int t = 0; t < room.players.size(); t++) {
			if (room.players.get(t) != null) {
				room.players.get(t).dos.writeUTF(s);
			}
		}
	}

	// thiet lap lai cac bien ve trang thai ban dau
	public void resetAll() {
		ready = false;
		isDanhbai = false;
		isBocbai = false;
		isAnbai = false;
		sobairac = 0;
		quanbairac = 0;
		room.soReady = 0;
		room.soHaphom = 0;
		resetMang();
		resetMatranbai();
		resetboluoc();
		slbaidanh = 0;
	}

	// thiet lap cac mang bai ve 0
	public void resetMang() {
		for (int i = 0; i < 10; i++) {
			pocker[i] = 0;
			phom[i] = 0;
			kophom[i] = 0;
		}
	}

	// reset matranbai
	public void resetMatranbai() {
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 4; j++) {
				this.matranbai[i][j] = 0;
			}
			this.ktmatran[i] = false;
		}
	}

	public void resetboluoc() {
		for (int i = 0; i < 4; i++) {
			mangboluoc[i] = 0;
		}
	}

	// gui tin hieu chuan bi xuong client
	public void sendChuanbi() {
		try {
			dos.writeUTF("chuanbi");
		} catch (IOException ex) {
			System.out.println("Loi khi bao chuan bi: " + ex.toString());
		}
	}

	// gui cac quan bai duoc chia ve cho client
	public void sendBai() throws IOException {
		String s = "bai:";
		for (int i = 0; i < 13; i++) {
			s += " " + this.pocker[i];
		}

		s += " @";
		for (int i = 0; i < room.players.size(); i++) {
			s += " " + room.players.get(i).win;
		}
		dos.writeUTF(s);
	}

	// dem so quan bai tren tay nguoi choi
	public int dembaiPlayer() {
		int dem = 0;
		for (int i : this.pocker) {
			if (i != 0) {
				dem++;
			}
		}
		return dem;
	}

	// ham cho phep cac thao tac danh, boc, an bai
	public void choi() {
		if (this.dembaiPlayer() > 0 && isDanhbai == false) {
			try {
				this.sendDanh();
				isDanhbai = true;
			} catch (IOException ex) {
				System.out.println("Loi danh bai bai: " + ex.toString());
			}
		}

	}

	// gui tin hieu danh bai cho client
	public void sendDanh() throws IOException {
		room.players.get(this.ordernumber % 3).dos.writeUTF("tatboluoc");
		dos.writeUTF("danhbai");
	}

	// gui so thu tu ve cho client biet
	public void sendNumber() throws IOException {
		dos.writeUTF("number:" + ordernumber);
	}

	// truyen tin hieu san sang ve tat ca cac client
	public void sendReady() throws IOException {
		String s = "ready:";

		// tao chuoi muon gui
		for (int t = 0; t < 4; t++) {
			if (t < room.players.size() && room.players.get(t) != null) {
				s += " " + room.players.get(t).ready;
			} else {
				s += " *";
			}
		}

		// gui ve cac client khac
		for (int t = 0; t < room.players.size(); t++) {
			if (room.players.get(t) != null) {
				room.players.get(t).dos.writeUTF(s);
			}
		}
	}

	// gui bai rac xuong nguoi choi
	public void sendRac(String bairac) throws IOException {

		bairac = bairac.trim();// -------------------------------------------------
		// gui quan bai rac xuong client
		String s = "rac:" + this.nameplayer + "@" + bairac;
		for (int t = 0; t < room.players.size(); t++) {
			room.players.get(t).dos.writeUTF(s);
		}
		room.players.get(this.ordernumber).dos.writeUTF("tatboluoc");
		if (room.players.get(ordernumber).slbaidanh == 13) {
			for (int i = 0; i < 4; i++) {
				if (i == ordernumber) {
					room.players.get(ordernumber).dos.writeUTF("winbai");
				} else {
					room.players.get(i).dos.writeUTF("losebai");
				}
			}
			room.soHaphom = 4;
			room.nguoiwin = ordernumber;
			return;
		}

		for (int i = 0; i < 4; i++) {
			if (mangboluoc[i] == 1)
				room.players.get(i).dos.writeUTF("tatsauboluoc");
		}
		for (int i = 1; i < 4; i++) {
			if (mangboluoc[(this.ordernumber + i) % 4] == 0) {
				room.players.get((this.ordernumber + i) % 4).dos
						.writeUTF("bocbai");
				room.players.get((this.ordernumber + i) % 4).dos
						.writeUTF("anbai");
				return;
			}
		}
	}

	// Gui msg chat cho tat ca cac client
	public synchronized void sendChat(String str) throws IOException {
		for (int i = 0; i < room.players.size(); i++) {
			if (room.players.get(i) != null) {
				room.players.get(i).dos.writeUTF(str);
			}
		}
	}

	// xu ly an bai vaf gui quan bai an ve cho client
	public void sendAnbai() throws IOException {
		// Server.player[this.ordernumber%3].dos.writeUTF("tatboluoc");
		int dem = 0;
		for (int i = 0; i < 4; i++) {
			if (mangboluoc[i] == 1)
				room.players.get(i).dos.writeUTF("tatsauboluoc");
			dem += mangboluoc[i];
		}
		if (dem < 3) {
			for (int i = 1; i < 4; i++) {
				if (mangboluoc[(this.ordernumber + i) % 4] == 0) {
					room.players.get((this.ordernumber + i) % 4).dos
							.writeUTF("bocbai");
					room.players.get((this.ordernumber + i) % 4).dos
							.writeUTF("anbai");
					return;
				}
			}
		} else {
			String s = "rac:" + this.nameplayer + "@_";
			for (int t = 0; t < room.players.size(); t++) {
				room.players.get(t).dos.writeUTF(s);
			}
			for (int i = 0; i < 4; i++) {
				if (mangboluoc[i] == 0) {
					room.players.get(i).dos.writeUTF("bocbai");
					room.players.get(i).dos.writeUTF("anbai");
					room.players.get(i).dos.writeUTF("tatboluoc");
					break;
				}
			}

			resetboluoc();
		}

	}

}
