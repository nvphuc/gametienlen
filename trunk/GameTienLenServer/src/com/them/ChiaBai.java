package com.them;
import java.util.ArrayList;
import java.util.Random;

public class ChiaBai {

	public int bai[] = new int[52];
	private ArrayList<Integer> quanBai = new ArrayList<Integer>();
	Random ran = new Random();

	public ChiaBai() {
		quanBai.clear();
		for (int i = 0; i < 52; i++) {
			quanBai.add(i+4);
		}
		int pos;
		System.out.print("Mang bai ngua nhien: ");
		for (int j = 0; j < 52; j++) {
			pos = ran.nextInt(quanBai.size());
			bai[j] = quanBai.get(pos);
			System.out.print(" " + quanBai.get(pos));
			quanBai.remove(pos);
		}
		// for (int i = 0; i < 52; i++) {
		// int tam = ran.nextInt(56);
		// lap:
		// while (true) {
		// for (int j = 0; j < i; j++) {
		// if (bai[j] == tam || tam < 4) {
		// tam = ran.nextInt(56);
		// continue lap;
		// }
		// }
		// break;
		// }
		// bai[i] = tam;
		// }
	}

	// chia bai cho nguoi choi
	public void chiabai(int[] pocker, boolean win, int stt) {
		int i = 0, t = 0;
		for (; t <= 51; t++) {
			if (t % 4 == stt) {
				pocker[i] = bai[t];
				i++;
			}
		}
		// if (win == true) {
		// pocker[i] = bai[t];
		// }
	}
}
