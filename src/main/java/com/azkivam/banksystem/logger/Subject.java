package com.azkivam.banksystem.logger;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    private final List<TransactionObserver> observers; // can spring inject the element of a list onto another list?

    public Subject() {
        observers = new ArrayList<>();
        observers.add(new TransactionObserverImpl()); // aya niaze DI bezanam ya autowired o ina?
    }

    public void addObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TransactionObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String accountNumber, String transactionType, double amount) {
        for (TransactionObserver observer : observers)
            observer.onTransaction(accountNumber, transactionType, amount);
    }
}
