package oo.gui;

import game.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import oo.gui.element.TheBai;
import oo.processor.ProcessorGuiPlay;

public class GuiPlay extends Gui {

	// panel chinh de choi
	public JPanel panelPlay;

	// username
	public JLabel lbUsername[] = new JLabel[4];

	// avartar cua cac player
	public JButton avatarsPlayer[] = new JButton[4];

	// label hien thong bao cua tung nguoi choi
	public JLabel lbMessage[] = new JLabel[4];

	// panel chua cac quan bai tren tay cua nguoi choi
	public JPanel pnHandCard[] = new JPanel[4];

	// panel chua cac quan bai tren ban nguoi choi danh ra
	public JPanel pnTableCard1, pnTableCard3, pnTableCard4, pnTableCard2;

	// Nut danh bai, bo luot, san sang
	public JButton btHitCards, btSkipTurn, btReady, btSend;

	// panel chat
	public JPanel panelChat;

	// khung chua noi dung chat
	public JTextArea txtContent;

	// khung go noi dung chat
	public JTextField txtChat;

	// Cac the bai tren tay cua 4 nguoi choi
	public TheBai handcards1[] = new TheBai[13];
	public TheBai handcards2[] = new TheBai[13];
	public TheBai handcards3[] = new TheBai[13];
	public TheBai handcards4[] = new TheBai[13];

	// Cac the bai 4 nguoi choi dat xuong
	public TheBai tablecards1[] = new TheBai[13];
	public TheBai tablecards2[] = new TheBai[13];
	public TheBai tablecards3[] = new TheBai[13];
	public TheBai tablecards4[] = new TheBai[13];

	public GuiPlay(Game game, Point location, int OrderNumber) {
		super(game, location);
		setTitle("Play Game");
		GuiId = 2;
		processor = new ProcessorGuiPlay(this, OrderNumber);

		setGui();

		setVisible(true);

		Thread thread = new Thread((ProcessorGuiPlay) processor);
		thread.start();
	}

	@Override
	public void setGui() {
		this.getContentPane().setLayout(null);

		// Thiet ke panel chinh
		panelPlay = new JPanel();
		panelPlay.setBackground(new Color(255, 255, 255));
		panelPlay.setBounds(-16, 0, 1122, 596);
		panelPlay.setLayout(null);
		this.getContentPane().add(panelPlay);

		// avartar cua nguoi choi thu 1
		avatarsPlayer[0] = new JButton("");
		avatarsPlayer[0].setBounds(400, 505, 80, 80);
		panelPlay.add(avatarsPlayer[0]);

		// avartar cua nguoi choi thu 2
		avatarsPlayer[1] = new JButton("");
		avatarsPlayer[1].setBounds(1033, 390, 80, 80);
		panelPlay.add(avatarsPlayer[1]);

		// avartar cua nguoi choi thu 3
		avatarsPlayer[2] = new JButton("");
		avatarsPlayer[2].setBounds(672, 10, 80, 80);
		panelPlay.add(avatarsPlayer[2]);

		// avartar cua nguoi choi thu 4
		avatarsPlayer[3] = new JButton("");
		avatarsPlayer[3].setBounds(28, 85, 80, 80);
		panelPlay.add(avatarsPlayer[3]);

		// panel chua cac quan bai tren tay cua nguoi choi 1
		pnHandCard[0] = new JPanel();
		pnHandCard[0].setBounds(490, 475, 300, 110);
		pnHandCard[0].setBackground(Color.WHITE);
		panelPlay.add(pnHandCard[0]);
		pnHandCard[0].setLayout(null);

		// khoi tao cac la bai tren tay nguoi choi 1 (khoi tao cac label(labai)
		// hien thi tren panel(pnHandCard1)
		initHandCard1();

		// panel chua cac quan bai tren tay cua nguoi choi 2
		pnHandCard[1] = new JPanel();
		pnHandCard[1].setBounds(1032, 100, 80, 280);
		pnHandCard[1].setBackground(Color.WHITE);
		panelPlay.add(pnHandCard[1]);
		pnHandCard[1].setLayout(null);

		// khoi tao cac la bai tren tay nguoi choi 2
		initHandCard2();

		// panel chua cac quan bai tren tay cua nguoi choi 3
		pnHandCard[2] = new JPanel();
		pnHandCard[2].setBounds(380, 8, 280, 100);
		pnHandCard[2].setBackground(Color.WHITE);
		panelPlay.add(pnHandCard[2]);
		pnHandCard[2].setLayout(null);

		// khoi tao cac la bai tren tay nguoi choi 3
		initHandCard3();

		// panel chua cac quan bai tren tay cua nguoi choi 4
		pnHandCard[3] = new JPanel();
		pnHandCard[3].setBounds(26, 200, 80, 280);
		pnHandCard[3].setBackground(Color.WHITE);
		panelPlay.add(pnHandCard[3]);
		pnHandCard[3].setLayout(null);

		// khoi tao cac la bai tren tay nguoi choi 4
		initHandCard4();

		// nut danh bai
		btHitCards = new JButton("Đánh Bài");
		btHitCards.addActionListener(this);
		btHitCards.setEnabled(false);
		btHitCards.setBounds(800, 521, 90, 25);
		panelPlay.add(btHitCards);

		// nut bo luot
		btSkipTurn = new JButton("Bỏ Lượt");
		btSkipTurn.addActionListener(this);
		btSkipTurn.setEnabled(false);
		btSkipTurn.setBounds(800, 557, 90, 25);
		panelPlay.add(btSkipTurn);

		// nut san sang
		btReady = new JButton("Sẵn Sàng");
		btReady.addActionListener(this);
		btReady.setEnabled(true);
		btReady.setBounds(800, 486, 90, 25);
		panelPlay.add(btReady);

		// usernam 1
		lbUsername[0] = new JLabel("");
		lbUsername[0].setBounds(400, 485, 80, 14);
		panelPlay.add(lbUsername[0]);

		// usernam 2
		lbUsername[1] = new JLabel("");
		lbUsername[1].setBounds(1040, 480, 80, 14);
		panelPlay.add(lbUsername[1]);

		// usernam 3
		lbUsername[2] = new JLabel("");
		lbUsername[2].setBounds(675, 95, 80, 14);
		panelPlay.add(lbUsername[2]);

		// usernam 4
		lbUsername[3] = new JLabel("");
		lbUsername[3].setBounds(30, 175, 80, 14);
		panelPlay.add(lbUsername[3]);

		// panel chat
		panelChat = new JPanel();
		panelChat.setBackground(Color.CYAN);
		panelChat.setBounds(1103, 0, 191, 622);
		panelChat.setLayout(null);
		this.getContentPane().add(panelChat);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(8, 11, 180, 534);
		panelChat.add(scrollPane);

		txtContent = new JTextArea();
		txtContent.setEditable(false);
		scrollPane.setViewportView(txtContent);

		txtChat = new JTextField();
		txtChat.addActionListener(this);
		txtChat.setBounds(8, 556, 131, 33);
		panelChat.add(txtChat);
		txtChat.setColumns(10);

		btSend = new JButton("Gửi Đi");
		btSend.addActionListener(this);
		btSend.setMargin(new Insets(0, 0, 0, 0));
		btSend.setAlignmentY(0.0f);
		btSend.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btSend.setBounds(143, 556, 45, 33);
		panelChat.add(btSend);

		// Label thong bao cua nguoi choi 1
		lbMessage[0] = new JLabel("");
		lbMessage[0].setFont(new Font("Tahoma", Font.BOLD, 12));
		lbMessage[0].setBounds(530, 430, 60, 35);
		panelPlay.add(lbMessage[0]);

		// Label thong bao cua nguoi choi 2
		lbMessage[1] = new JLabel("");
		lbMessage[1].setFont(new Font("Tahoma", Font.BOLD, 12));
		lbMessage[1].setBounds(950, 400, 60, 35);
		panelPlay.add(lbMessage[1]);

		// Label thong bao cua nguoi choi 3
		lbMessage[2] = new JLabel("");
		lbMessage[2].setFont(new Font("Tahoma", Font.BOLD, 12));
		lbMessage[2].setBounds(570, 122, 60, 35);
		panelPlay.add(lbMessage[2]);

		// Label thong bao cua nguoi choi 4
		lbMessage[3] = new JLabel("");
		lbMessage[3].setFont(new Font("Tahoma", Font.BOLD, 12));
		lbMessage[3].setBounds(120, 220, 60, 35);
		panelPlay.add(lbMessage[3]);

		// panel chua quan bai nguoi choi 1 danh ra
		pnTableCard1 = new JPanel();
		pnTableCard1.setBounds(450, 364, 350, 100);
		pnTableCard1.setBackground(Color.WHITE);
		panelPlay.add(pnTableCard1);
		pnTableCard1.setLayout(null);

		// khoi tao cac la bai do nguoi choi 1 dat xuong ban (khoi tao cac
		// label(labai) hien thi tren panel(pnTableCard1)
		initTableCard1();

		// panel chua quan bai nguoi choi 2 danh ra
		pnTableCard2 = new JPanel();
		pnTableCard2.setBounds(922, 100, 100, 350);
		pnTableCard2.setBackground(Color.WHITE);
		panelPlay.add(pnTableCard2);
		pnTableCard2.setLayout(null);

		// khoi tao cac la bai do nguoi choi 2 dat xuong ban
		initTableCard2();

		// panel chua quan bai nguoi choi 3 danh ra
		pnTableCard3 = new JPanel();
		pnTableCard3.setBounds(380, 119, 350, 100);
		pnTableCard3.setBackground(Color.WHITE);
		panelPlay.add(pnTableCard3);
		pnTableCard3.setLayout(null);

		// khoi tao cac la bai do nguoi choi 3 dat xuong ban
		initTableCard3();

		// panel chua quan bai nguoi choi 4 danh ra
		pnTableCard4 = new JPanel();
		pnTableCard4.setBounds(116, 100, 100, 350);
		pnTableCard4.setBackground(Color.WHITE);
		panelPlay.add(pnTableCard4);
		pnTableCard4.setLayout(null);

		// khoi tao cac la bai do nguoi choi 4 dat xuong ban
		initTableCard4();		
	}

	// khoi tao cac la bai tren tay nguoi choi 1
	public void initHandCard1() {
		int x = 240;
		for (int i = 0; i < 13; i++) {
			handcards1[i] = new TheBai();
			handcards1[i].setBounds(x, 10, 80, 100);
			handcards1[i].x = x;
			handcards1[i].y = 10;
			handcards1[i].width = 80;
			handcards1[i].height = 100;
			final TheBai tam = handcards1[i];
			handcards1[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (tam.isClicked == false) {
						tam.displayUp();
					} else {
						((ProcessorGuiPlay) processor).downAll();
					}
				}
			});
			pnHandCard[0].add(handcards1[i]);
			x -= 20;
		}
	}

	// khoi tao cac la bai tren tay nguoi choi 2
	public void initHandCard2() {
		int y = 240;
		for (int i = 0; i < 13; i++) {
			handcards2[i] = new TheBai();
			handcards2[i].setBounds(0, y, 80, 100);
			pnHandCard[1].add(handcards2[i]);
			y -= 20;
		}
	}

	// khoi tao cac la bai tren tay nguoi choi 3
	public void initHandCard3() {
		int x = 240;
		for (int i = 0; i < 13; i++) {
			handcards3[i] = new TheBai();
			handcards3[i].setBounds(x, 0, 80, 100);
			pnHandCard[2].add(handcards3[i]);
			x -= 20;
		}
	}

	// khoi tao cac la bai tren tay nguoi choi 4
	public void initHandCard4() {
		int y = 240;
		for (int i = 0; i < 13; i++) {
			handcards4[i] = new TheBai();
			handcards4[i].setBounds(0, y, 80, 100);
			pnHandCard[3].add(handcards4[i]);
			y -= 20;
		}
	}

	// khoi tao cac la bai nguoi choi 1 dat xuong ban
	public void initTableCard1() {
		int x = 240;
		for (int i = 0; i < 13; i++) {
			tablecards1[12 - i] = new TheBai();
			tablecards1[12 - i].setBounds(x, 0, 80, 100);
			pnTableCard1
					.add(tablecards1[12 - i]);
			pnTableCard1.setBackground(Color.BLACK);
			x -= 20;
		}
	}

	// khoi tao cac la bai nguoi choi 2 dat xuong ban
	public void initTableCard2() {
		int y = 0;
		for (int i = 0; i < 13; i++) {
			tablecards2[12 - i] = new TheBai();
			tablecards2[12 - i].setBounds(0, y, 80, 100);
			pnTableCard2
					.add(tablecards2[12 - i]);
			pnTableCard2.setBackground(Color.BLACK);
			y += 20;
		}
	}

	// khoi tao cac la bai nguoi choi 3 dat xuong ban
	public void initTableCard3() {
		int x = 240;
		for (int i = 0; i < 13; i++) {
			tablecards3[12 - i] = new TheBai();
			tablecards3[12 - i].setBounds(x, 0, 80, 100);
			pnTableCard3
					.add(tablecards3[12 - i]);
			pnTableCard3.setBackground(Color.BLACK);
			x -= 20;
		}
	}

	// khoi tao cac la bai nguoi choi 4 dat xuong ban
	public void initTableCard4() {
		int y = 0;
		for (int i = 0; i < 13; i++) {
			tablecards4[12 - i] = new TheBai();
			tablecards4[12 - i].setBounds(0, y, 80, 100);
			pnTableCard4
					.add(tablecards4[12 - i]);
			pnTableCard4.setBackground(Color.BLACK);
			y += 20;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btHitCards) {
			((ProcessorGuiPlay) processor).hitCards();
		}
		if (e.getSource() == btSkipTurn) {
			((ProcessorGuiPlay) processor).skipTurn();
		}
		if (e.getSource() == btReady) {
			((ProcessorGuiPlay) processor).ready();
		}
		if (e.getSource() == txtChat) {
			((ProcessorGuiPlay) processor).sendChat();
		}
		if (e.getSource() == btSend) {
			((ProcessorGuiPlay) processor).sendChat();
		}
	}
}
