package com.azkivam.banksystem.service.impl;

import com.azkivam.banksystem.entity.BankAccount;
import com.azkivam.banksystem.service.Bank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankImplTest {

    @Autowired
    private ApplicationContext applicationContext;

//    Bank bank = applicationContext.getBean(Bank.class);

    @Test
    void createAccount() {
        Bank bank = applicationContext.getBean(Bank.class);
        BankAccount mehdi = bank.createAccount("ghzvaevzvasqwqw", 5600.0);
        System.out.println(mehdi.getAccountNumber());
        System.out.println(mehdi.getBalance());
        System.out.println(mehdi.getHolderName());
    }

    @Test
    void deposit() {
        Bank bank1 = applicationContext.getBean(Bank.class);
        BankAccount bankAccount = bank1.deposit(1L, 3000.0);
        System.out.println(bankAccount.getAccountNumber());
        System.out.println(bankAccount.getBalance());
        System.out.println(bankAccount.getHolderName());
    }

    @Test
    void withdraw() {
        Bank bank1 = applicationContext.getBean(Bank.class);
        BankAccount bankAccount = bank1.withdraw(1L, 500.0);
        System.out.println(bankAccount.getAccountNumber());
        System.out.println(bankAccount.getBalance());
        System.out.println(bankAccount.getHolderName());
    }
//
    @Test
    void transferFunds() {
        Bank bank1 = applicationContext.getBean(Bank.class);
        bank1.transferFunds(1L, 3L, 1500.0);
        System.out.println(bank1.displayAccountBalance(1L));
        System.out.println(bank1.displayAccountBalance(3L));
    }
//
    @Test
    void displayAccountBalance() {
        Bank bank1 = applicationContext.getBean(Bank.class);
        Double aDouble = bank1.displayAccountBalance(1L);
        System.out.println(aDouble);
    }
}