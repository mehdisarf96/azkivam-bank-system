package com.azkivam.banksystem.service.impl;

import com.azkivam.banksystem.dao.AccountRepository;
import com.azkivam.banksystem.entity.BankAccount;
import com.azkivam.banksystem.logger.Subject;
import com.azkivam.banksystem.service.Bank;
import com.azkivam.banksystem.service.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BankImpl extends Subject implements Bank {

    private AccountRepository accountRepository;

    public BankImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public BankAccount createAccount(String holderName, Double initialBalance) { /// many parameter or encapsulate it in a object?
        BankAccount newBankAccount = new BankAccount(holderName, initialBalance);
        BankAccount persistedAccount = accountRepository.save(newBankAccount);
        notifyObservers(
                String.valueOf(persistedAccount.getAccountNumber()),
                TransactionType.CREATE.value,
                initialBalance);
        return persistedAccount;
    }

    @Override
    public BankAccount deposit(Long accountNumber, Double amount) {
        Optional<BankAccount> theAccount = accountRepository.findById(accountNumber);
        BankAccount bankAccount = theAccount.get(); // tahqiq
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        return accountRepository.save(bankAccount);
    }

    @Override
    public BankAccount withdraw(Long accountNumber, Double amount) {
        Optional<BankAccount> theAccount = accountRepository.findById(accountNumber);
        BankAccount bankAccount = theAccount.get(); // tahqiq
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        return accountRepository.save(bankAccount);
    }

    @Override
    @Transactional
    public void transferFunds(Long accountNumber, Long destinationAccountNumber, Double amount) {
        Optional<BankAccount> theAccount = accountRepository.findById(accountNumber);
        Optional<BankAccount> destaccount = accountRepository.findById(destinationAccountNumber);
        BankAccount bankAccount = theAccount.get(); // tahqiq
        BankAccount destinationAccount = destaccount.get(); // tahqiq
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
    }

    @Override
    public Double displayAccountBalance(Long account) {
        BankAccount bankAccount = accountRepository.findById(account).get();
        return bankAccount.getBalance();
    }
}
