package com.mehdisarf.banksystem.dao.hibernate.impl;

import com.mehdisarf.banksystem.dao.impl.AccountDao;
import com.mehdisarf.banksystem.entity.BankAccount;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountDaoTest {

    private AccountDao accountDao;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void create() {
        try (SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class)) {
            accountDao = new AccountDao(sessionFactory);

            BankAccount bankAccount = new BankAccount();
            bankAccount.setBalance(200.0);
            bankAccount.setHolderName("Ahmad Mahmoud");
            Long id = accountDao.create(bankAccount);
            assertNotNull(id);
            assertEquals(35, id);
        }
    }

    @Test
    @Transactional
    void update() {
        try (SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class)) {
            accountDao = new AccountDao(sessionFactory);

            BankAccount bankAccount = new BankAccount();
            bankAccount.setAccountNumber(26L);
            bankAccount.setBalance(500.0);
            bankAccount.setHolderName("reza");

            BankAccount updatedBankAccount = accountDao.update(bankAccount);

            assertEquals(26L, updatedBankAccount.getAccountNumber());
            assertEquals(500.0, updatedBankAccount.getBalance());
            assertEquals("reza", updatedBankAccount.getHolderName());
        }
    }

    @Test
    void get() {
        try (SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class)) {
            accountDao = new AccountDao(sessionFactory);
            BankAccount retrievedAccount = accountDao.get(26L);

            assertEquals(26L, retrievedAccount.getAccountNumber());
            assertEquals(500.0, retrievedAccount.getBalance());
            assertEquals("reza", retrievedAccount.getHolderName());
        }
    }

    @Test
    void delete() {
        try (SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class)) {
            accountDao = new AccountDao(sessionFactory);

            accountDao.delete(6L);

            BankAccount bankAccount = accountDao.get(6L);
            assertNull(bankAccount);
        }
    }
}