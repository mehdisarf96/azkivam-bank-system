package com.azkivam.banksystem.service;

public enum TransactionType { // should enum has getter setter or haminjuri khube?
    CREATE("Creation"),
    WITHDRAW("Withdraw"),
    DEPOSIT("Deposit"),
    TRANSFER("Transfer"),
    BALANCE("Balance");

    public final String value;

    TransactionType(String value) {
        this.value = value;
    }
}
