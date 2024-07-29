package com.mehdisarf.banksystem.service;

import com.mehdisarf.banksystem.entity.BankAccount;

public interface Bank {

    BankAccount createAccount(String holderName, Double initialBalance); // bayad be user yek accountNumber bargardune

    BankAccount deposit(Long accountNumber, Double amount);

    BankAccount withdraw(Long account, Double amount);

    void transferFunds(Long sourceAccount, Long destinationAccountNumber, Double amount);

    Double displayAccountBalance(Long account);
}
