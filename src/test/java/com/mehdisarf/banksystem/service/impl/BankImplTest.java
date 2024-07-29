package com.mehdisarf.banksystem.service.impl;

import com.mehdisarf.banksystem.entity.BankAccount;
import com.mehdisarf.banksystem.service.Bank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankImplTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void createAccount() {
        Bank bankService = getBankService();
        BankAccount sadeghAccount = bankService.createAccount("sadegh", 5500.0);

        assertEquals(37L, sadeghAccount.getAccountNumber());
        assertEquals("sadegh", sadeghAccount.getHolderName());
        assertEquals(5500.0, sadeghAccount.getBalance());
    }

    @Test
    void deposit() {
        Bank bankService = getBankService();
        BankAccount bankAccount = bankService.deposit(36L, 830.0);

        assertEquals(1000L, bankAccount.getBalance());
        assertEquals(36L, bankAccount.getAccountNumber());
    }

    @Test
    void withdraw() {
        Bank bankService = getBankService();
        BankAccount bankAccount = bankService.withdraw(37L, 500.0);

        assertEquals(5000L, bankAccount.getBalance());
        assertEquals(37L, bankAccount.getAccountNumber());
        System.out.println(bankAccount.getAccountNumber());
        System.out.println(bankAccount.getBalance());
        System.out.println(bankAccount.getHolderName());
    }

    //
    @Test
    void transferFunds() {
        Bank bankService = getBankService();
        bankService.transferFunds(37L, 36L, 1000.0);
        Double sourceAccountBalance = bankService.displayAccountBalance(37L);
        Double destinationAccountBalance = bankService.displayAccountBalance(36L);

        assertEquals(4000L, sourceAccountBalance);
        assertEquals(2000L, destinationAccountBalance);
    }

    //
    @Test
    void displayAccountBalance() {
        Bank bankService = getBankService();
        Double balance = bankService.displayAccountBalance(37L);
        assertEquals(5000L, balance);
    }

    private Bank getBankService() {
        return applicationContext.getBean(Bank.class);
    }
}