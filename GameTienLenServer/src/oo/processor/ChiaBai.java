package oo.processor;
import java.util.ArrayList;
import java.util.Random;

public class ChiaBai {

	public int deck[] = new int[52];
	private ArrayList<Integer> cards = new ArrayList<Integer>();
	Random ran = new Random();

	public ChiaBai() {
		cards.clear();
		for (int i = 0; i < 52; i++) {
			cards.add(i+4);
		}
		int pos;
		for (int j = 0; j < 52; j++) {
			pos = ran.nextInt(cards.size());
			deck[j] = cards.get(pos);
			cards.remove(pos);
		}
	}

	// chia bai cho nguoi choi
	public void dealCards(int[] pocker, int stt) {
		int i = 0, t = 0;
		for (; t <= 51; t++) {
			if (t % 4 == stt) {
				pocker[i] = deck[t];
				i++;
			}
		}
	}
}
