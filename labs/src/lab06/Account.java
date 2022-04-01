package lab06;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Account {
	public int id;
	public int balance;

	public Account(int id, int balance) {
		this.id = id;
		this.balance = balance;
	}

	// REVIEW: Task1
	// replace the null with a lambda expression
	public static Consumer<Account> add100 = a -> a.balance += 100;

	// REVIEW: Task2
	// define checkBound using lowerBound and upperBound
	// We want checkBound to check BOTH lowerBound AND upperBound.
	public static Predicate<Account> lowerBound = a -> a.balance >= 0;
	public static Predicate<Account> upperBound = a -> a.balance <= 10000;
	public static Predicate<Account> checkBound = a -> a.balance >= 0 && a.balance <= 10000;

	interface AddMaker {
		Consumer<Account> make(int N);
	}

	// REVIEW: Task3
	// replace the null with a lambda expression
	public static AddMaker maker = (int N) -> {
		Consumer<Account> make = a -> a.balance += N;
		return make;
	};

	// You can assume that all the Account in acconts have positive balances.
	public static int getMaxAccountID(List<Account> accounts) {
		// REVIEW: Task4
		// replace the null with a lambda expression
		Account maxOne = accounts.stream().reduce(new Account(0, -100), (a1, a2) -> a1.balance > a2.balance ? a1 : a2);

		return maxOne.id;
	}

}
