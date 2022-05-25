package main.java;

import java.util.ArrayList;

interface Miner {
	public Integer mine(String accName);
}

public interface Bank {
	// add a new account 'accountID' to the bank and set its balance to
	// 'initBalance'.
	// Return true if 'accountID' is successfully added.
	// Return false if 'accountID' already exists in the bank (in this case, we do
	// not modify the
	// bank and directly return false).
	public boolean addAccount(String accountID, Integer initBalance);

	// If 'accountID' is not yet added in the bank, directly return false;
	// Otherwise, increase the balance for 'accountID' by 'amount' (i.e., perform a
	// deposit to 'accountID') and return true.
	public boolean deposit(String accountID, Integer amount);

	// If ('accountID' is not yet added in the bank) then { return false }
	// Else if (the balance for 'accountID' is no less than 'amount') then { deduct
	// the balance by 'amount' and return true }
	// Else {
	// the current thread should wait for at most 'timeoutMillis' milliseconds (in
	// case some other transactions add money to 'accountID').
	// After the current thread wakes (either by a notify from another thread or
	// after timeout), it should check
	// again whether the current balance for 'accountID' is no less than 'amount'.
	// If 'accountID' has enough money after waiting, deduct the balance by 'amount'
	// and return true.
	// Otherwise, return false.
	// }
	public boolean withdraw(String accountID, Integer amount, long timeoutMillis);

	// Transfer 'amount' of money from 'srcAccount' to 'dstAccount'.
	// This involves a withdraw from 'srcAccount' (with timeout 'timeoutMillis') and
	// a deposit to 'dstAccount'.
	// Return true if the transfer succeeds. Return false if any of the involved
	// steps in transfer fails.
	public boolean transfer(String srcAccount,
			String dstAccount,
			Integer amount,
			long timeoutMillis);

	// Return the current balance for 'accountID'. If 'accountID' is not added to
	// Bank, return 0.
	public Integer getBalance(String accountID);

	public void doLottery(ArrayList<String> accounts, Miner miner);

}
