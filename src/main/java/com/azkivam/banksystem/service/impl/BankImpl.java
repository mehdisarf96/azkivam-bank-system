package com.azkivam.banksystem.service.impl;

import com.azkivam.banksystem.entity.BankAccount;
import com.azkivam.banksystem.logger.Subject;
import com.azkivam.banksystem.dao.GenericDao;
import com.azkivam.banksystem.service.Bank;
import com.azkivam.banksystem.service.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankImpl extends Subject implements Bank {

    private GenericDao<BankAccount, Long> accountDao;

    private final Object creationLock = new Object();
    private final Object depositLock = new Object();
    private final Object withdrawLock = new Object();
    private final Object transferLock = new Object();
    private final Object balanceLock = new Object();

    public BankImpl(GenericDao<BankAccount, Long> accountDao) {
        this.accountDao = accountDao;
    }


    @Override
    @Transactional
    public BankAccount createAccount(String holderName, Double initialBalance) { /// many parameter or encapsulate it in a object?
        synchronized (creationLock) {
            Long id = accountDao.create(new BankAccount(holderName, initialBalance));
            notifyObservers(String.valueOf(id), TransactionType.CREATE, initialBalance);
            return accountDao.get(id);
        }
    }

    @Override
    @Transactional
    public BankAccount deposit(Long accountNumber, Double amount) {
        synchronized (depositLock) {
            BankAccount theAccount = accountDao.get(accountNumber);
            Double currentBalance = theAccount.getBalance();
            Double futureBalance = currentBalance + amount;
            theAccount.setBalance(futureBalance);
            BankAccount updatedAccount = accountDao.update(theAccount);
            notifyObservers(String.valueOf(updatedAccount.getAccountNumber()), TransactionType.DEPOSIT, amount);
            return updatedAccount;
        }
    }

    @Override
    @Transactional
    public BankAccount withdraw(Long accountNumber, Double amount) {
        synchronized (withdrawLock) {
            BankAccount bankAccount = accountDao.get(accountNumber);
            Double currentBalance = bankAccount.getBalance();
            Double futureBalance = currentBalance - amount;
            bankAccount.setBalance(futureBalance);
            BankAccount updatedAccount = accountDao.update(bankAccount);
            notifyObservers(String.valueOf(updatedAccount.getAccountNumber()), TransactionType.WITHDRAW, amount);
            return updatedAccount;
        }
    }

    @Override
    @Transactional
    public void transferFunds(Long sourceAccountNumber, Long destinationAccountNumber, Double amount) {
        synchronized (transferLock) {
            BankAccount sourceAccount = accountDao.get(sourceAccountNumber);
            BankAccount destinationAccount = accountDao.get(destinationAccountNumber);
            sourceAccount.setBalance(sourceAccount.getBalance() - amount);
            destinationAccount.setBalance(destinationAccount.getBalance() + amount);
            accountDao.update(sourceAccount);
            accountDao.update(destinationAccount);
            notifyObservers(String.valueOf(destinationAccountNumber), TransactionType.TRANSFER, amount);
        }
    }

    @Override
    @Transactional
    public Double displayAccountBalance(Long accountNumber) {
        synchronized (balanceLock) {
            BankAccount bankAccount = accountDao.get(accountNumber);
            Double balance = bankAccount.getBalance();
            notifyObservers(String.valueOf(accountNumber), TransactionType.BALANCE, balance);
            return balance;
        }
    }
}
