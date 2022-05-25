package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcreteBank implements Bank {

    // public static Map<String, Integer> accountsMapByID;
    // public static Map<String, ReadWriteLock> locksMapByID;

    // static {
    // accountsMapByID = new HashMap<>();
    // locksMapByID = new HashMap<>();
    // }

    private Map<String, Integer> accountsMapByID = new HashMap<>();
    // private Map<String, Integer> accountsMapByID = new ConcurrentHashMap<>();
    private Map<String, Lock> locksMapByID = new HashMap<>();
    // private Map<String, Lock> locksMapByID = new ConcurrentHashMap<>();
    // private Map<String, ReadWriteLock> locksMapByID = new HashMap<>();

    // REVIEW
    class TaskForMiner implements Runnable {
        private String accountID;
        private Miner miner;

        public TaskForMiner(String accountID, Miner miner) {
            this.accountID = accountID;
            this.miner = miner;
        }

        @Override
        public void run() {
            deposit(this.accountID, this.miner.mine(this.accountID));
        }
    }

    @Override
    public boolean addAccount(String accountID, Integer initBalance) {
        // REVIEW
        synchronized (this) {
            if (accountsMapByID.containsKey(accountID)) {
                return false;
            }

            accountsMapByID.put(accountID, initBalance);
            locksMapByID.put(accountID, new ReentrantLock());
            return true;
        }
    }

    @Override
    public boolean deposit(String accountID, Integer amount) {
        // REVIEW
        if (!accountsMapByID.containsKey(accountID)) {
            return false;
        }

        Lock wLock = locksMapByID.get(accountID);

        synchronized (wLock) {
            Integer newBalance = accountsMapByID.get(accountID) + amount;
            accountsMapByID.replace(accountID, newBalance);
            wLock.notifyAll();
            return true;
        }
    }

    @Override
    public boolean withdraw(String accountID, Integer amount, long timeoutMillis) {
        // REVIEW
        if (!accountsMapByID.containsKey(accountID)) {
            return false;
        }

        Lock wLock = locksMapByID.get(accountID);

        synchronized (wLock) {
            Integer currBalance = accountsMapByID.get(accountID);
            if (currBalance < amount) {
                // wait for some time
                try {
                    wLock.wait(timeoutMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // update currBalance
                currBalance = accountsMapByID.get(accountID);
            }

            if (currBalance >= amount) {
                Integer newBalance = currBalance - amount;
                accountsMapByID.replace(accountID, newBalance);
                wLock.notifyAll();
                return true;

            } else {
                wLock.notifyAll();
                return false;
            }
        }
    }

    @Override
    public boolean transfer(String srcAccount, String dstAccount, Integer amount, long timeoutMillis) {
        // REVIEW
        // first we withdraw from srcAcc
        if (!withdraw(srcAccount, amount, timeoutMillis)) {
            return false;
        }

        // then we deposit to dstAcc
        if (!deposit(dstAccount, amount)) {
            // try to put money back to srcAcc
            deposit(srcAccount, amount);
            return false;
        }

        return true;
    }

    @Override
    public Integer getBalance(String accountID) {
        // REVIEW
        Integer currBalance = 0;
        if (!accountsMapByID.containsKey(accountID)) {
            return currBalance;
        }

        Lock rLock = locksMapByID.get(accountID);

        synchronized (rLock) {
            currBalance = accountsMapByID.get(accountID);
            rLock.notifyAll();
        }

        return currBalance;
    }

    @Override
    public void doLottery(ArrayList<String> accounts, Miner miner) {
        // REVIEW
        // int numAccounts = accounts.size();
        // ExecutorService es = Executors.newFixedThreadPool(numAccounts);

        // for (String accountID : accounts) {
        //     es.submit(new Thread(() -> {
        //         deposit(accountID, miner.mine(accountID));
        //     }));
        // }

        // es.shutdown();

        List<Thread> threads = new ArrayList<>();
        for (String accountID : accounts) {
            threads.add(new Thread(() -> {
                deposit(accountID, miner.mine(accountID));
            }));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
