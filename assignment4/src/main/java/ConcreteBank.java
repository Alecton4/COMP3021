package main.java;

import java.util.ArrayList;

public class ConcreteBank implements Bank {

    @Override
    public boolean addAccount(String accountID, Integer initBalance) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deposit(String accountID, Integer amount) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean withdraw(String accountID, Integer amount, long timeoutMillis) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean transfer(String srcAccount, String dstAccount, Integer amount, long timeoutMillis) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Integer getBalance(String accountID) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void doLottery(ArrayList<String> accounts, Miner miner) {
        // TODO Auto-generated method stub

    }
    // TODO
}
