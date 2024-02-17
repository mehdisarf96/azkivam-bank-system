package com.azkivam.banksystem.logger;

public interface TransactionObserver {
    void onTransaction(String accountNumber, String transactionType, Double amount);
}
