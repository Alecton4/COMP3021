package midterm;

import java.util.ArrayList;
import java.util.List;

public class CoinBank {

	public static double walletBtcWorth(List<Coin> wallet) {
		// REVIEW
		double sum = 0;
		for (Coin coin : wallet) {
			if (coin instanceof BitCoin) {
				sum += coin.getAsUSD();
			}
		}
		return sum;
	}

	public static void main(String[] args) {
		ArrayList<Coin> wallet = new ArrayList<Coin>();

		Coin btc1 = new BitCoin(10.5);
		Coin btc2 = new BitCoin(7.3);

		System.out.println(btc1);
		System.out.println(btc2);

		Coin btc3 = new BitCoin(3.3);

		wallet.add(btc1);
		wallet.add(btc2);
		wallet.add(btc3);

		System.out.println("Bitcoins in the wallet worth " + walletBtcWorth(wallet) + " USD");
	}
}
