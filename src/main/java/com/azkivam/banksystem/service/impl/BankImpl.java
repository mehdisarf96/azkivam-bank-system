package com.azkivam.banksystem.service.impl;

import com.azkivam.banksystem.dao.GenericDao;
import com.azkivam.banksystem.dao.AccountRepository;
import com.azkivam.banksystem.entity.BankAccount;
import com.azkivam.banksystem.logger.Subject;
import com.azkivam.banksystem.service.Bank;
import com.azkivam.banksystem.service.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BankImpl extends Subject implements Bank {

    private AccountRepository accountRepository;

    private GenericDao<BankAccount> accountDao;

    public BankImpl(AccountRepository accountRepository, GenericDao<BankAccount> accountDao) {
        this.accountRepository = accountRepository;
        this.accountDao = accountDao;
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

//        BankAccount persistedAccount = accountDao.create(new BankAccount(holderName, initialBalance));
//        notifyObservers(
//                String.valueOf(persistedAccount.getAccountNumber()),
//                TransactionType.CREATE.value,
//                initialBalance);
//        return persistedAccount;
    }

    @Override
    public BankAccount deposit(Long accountNumber, Double amount) {
        Optional<BankAccount> theAccount = accountRepository.findById(accountNumber);
        BankAccount bankAccount = theAccount.get(); // tahqiq
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        BankAccount updatedAccount = accountRepository.save(bankAccount);
        notifyObservers(String.valueOf(updatedAccount.getAccountNumber()), TransactionType.DEPOSIT.value, amount);
        return updatedAccount;

//        BankAccount theAccount = accountDao.get(accountNumber);
//        Double currentBalance = theAccount.getBalance();
//        Double futureBalance = currentBalance + amount;
//        theAccount.setBalance(futureBalance);
//        return accountDao.update(theAccount);
    }

    @Override
    public BankAccount withdraw(Long accountNumber, Double amount) {
        Optional<BankAccount> theAccount = accountRepository.findById(accountNumber);
        BankAccount bankAccount = theAccount.get(); // tahqiq
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        BankAccount updatedAccount = accountRepository.save(bankAccount);
        notifyObservers(String.valueOf(updatedAccount.getAccountNumber()), TransactionType.WITHDRAW.value, amount);
        return updatedAccount;

        //        BankAccount theAccount = accountDao.get(accountNumber);
//        Double currentBalance = theAccount.getBalance();
//        Double futureBalance = currentBalance - amount;
//        theAccount.setBalance(futureBalance);
//        return accountDao.update(theAccount);

    }

    @Override
    @Transactional
    public void transferFunds(Long accountNumber, Long destinationAccountNumber, Double amount) {
        Optional<BankAccount> srcAccountOpt = accountRepository.findById(accountNumber);
        Optional<BankAccount> destAccountOpt = accountRepository.findById(destinationAccountNumber);
        BankAccount sourceAccount = srcAccountOpt.get(); // tahqiq
        BankAccount destinationAccount = destAccountOpt.get(); // tahqiq
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        accountRepository.saveAll(List.of(sourceAccount, destinationAccount));

        notifyObservers(String.valueOf(destinationAccountNumber), TransactionType.TRANSFER.value, amount);
    }

    @Override
    public Double displayAccountBalance(Long accountNumber) {
        BankAccount bankAccount = accountRepository.findById(accountNumber).get();
        Double balance = bankAccount.getBalance();
        notifyObservers(String.valueOf(accountNumber), TransactionType.BALANCE.value, null);
        return balance;
    }
}
