package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
    private Map<String, ReadWriteLock> RWLocksMapByID = new HashMap<>();

    @Override
    public boolean addAccount(String accountID, Integer initBalance) {
        // REVIEW
        synchronized (this) {
            if (accountsMapByID.containsKey(accountID)) {
                return false;
            }

            accountsMapByID.put(accountID, initBalance);
            locksMapByID.put(accountID, new ReentrantLock());
            RWLocksMapByID.put(accountID, new ReentrantReadWriteLock());
            return true;
        }
    }

    @Override
    public boolean deposit(String accountID, Integer amount) {
        // REVIEW
        if (!accountsMapByID.containsKey(accountID)) {
            return false;
        }

        // final Lock wLock = locksMapByID.get(accountID);
        final Lock wLock = RWLocksMapByID.get(accountID).writeLock();
        final Condition cond = wLock.newCondition();

        // synchronized (wLock) {
        // Integer newBalance = accountsMapByID.get(accountID) + amount;
        // accountsMapByID.replace(accountID, newBalance);
        // wLock.notifyAll();
        // return true;
        // }

        wLock.lock();
        try {
            Integer newBalance = accountsMapByID.get(accountID) + amount;
            accountsMapByID.replace(accountID, newBalance);
            cond.signalAll();
            return true;

        } finally {
            wLock.unlock();
        }
    }

    @Override
    public boolean withdraw(String accountID, Integer amount, long timeoutMillis) {
        // REVIEW
        if (!accountsMapByID.containsKey(accountID)) {
            return false;
        }

        // final Lock wLock = locksMapByID.get(accountID);
        final Lock wLock = RWLocksMapByID.get(accountID).writeLock();
        final Condition cond = wLock.newCondition();

        // synchronized (wLock) {
        // Integer currBalance = accountsMapByID.get(accountID);
        // if (currBalance < amount) {
        // // wait for some time
        // try {
        // wLock.wait(timeoutMillis);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

        // // update currBalance
        // currBalance = accountsMapByID.get(accountID);
        // }

        // if (currBalance >= amount) {
        // Integer newBalance = currBalance - amount;
        // accountsMapByID.replace(accountID, newBalance);
        // wLock.notifyAll();
        // return true;

        // } else {
        // wLock.notifyAll();
        // return false;
        // }
        // }

        wLock.lock();
        try {
            Integer currBalance = accountsMapByID.get(accountID);
            if (currBalance < amount) {
                // wait for some time
                try {
                    cond.await(timeoutMillis, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // update currBalance
                currBalance = accountsMapByID.get(accountID);
            }

            if (currBalance >= amount) {
                Integer newBalance = currBalance - amount;
                accountsMapByID.replace(accountID, newBalance);
                cond.signalAll();
                return true;

            } else {
                cond.signalAll();
                return false;
            }

        } finally {
            wLock.unlock();
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
        if (!accountsMapByID.containsKey(accountID)) {
            return 0;
        }

        // final Lock rLock = locksMapByID.get(accountID);
        final Lock rLock = RWLocksMapByID.get(accountID).readLock();
        // !!! readLock does not support condition
        // final Condition cond = rLock.newCondition();

        // synchronized (rLock) {
        // Integer currBalance = accountsMapByID.get(accountID);
        // rLock.notifyAll();
        // return currBalance;
        // }

        rLock.lock();
        try {
            Integer currBalance = accountsMapByID.get(accountID);
            // cond.signalAll();
            return currBalance;

        } finally {
            rLock.unlock();
        }

    }

    // ???
    class TaskForMiner implements Runnable {
        final private String accountID;
        final private Miner miner;

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
    public void doLottery(ArrayList<String> accounts, Miner miner) {
        // REVIEW
        // ??? why this does not work
        // int numAccounts = accounts.size();
        // ExecutorService es = Executors.newFixedThreadPool(numAccounts);

        // for (String accountID : accounts) {
        // es.submit(() -> {
        // deposit(accountID, miner.mine(accountID));
        // });
        // }

        // es.shutdown();

        List<Thread> threads = new ArrayList<>();
        for (String accountID : accounts) {
            Thread t = new Thread(() -> {
                deposit(accountID, miner.mine(accountID));
            });
            t.setName(accountID + "_miner");
            threads.add(t);
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
