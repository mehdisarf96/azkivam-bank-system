package com.azkivam.banksystem.dao.impl;

import com.azkivam.banksystem.dao.GenericDao;
import com.azkivam.banksystem.entity.BankAccount;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateAccountRepository implements GenericDao<BankAccount, Long> {

    private SessionFactory sessionFactory;

    @Autowired
    public HibernateAccountRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    @Override
    public Long create(BankAccount account) {
        try (Session session = sessionFactory.openSession()) {
            return (Long) session.save(account);
        }
    }

    @Override
    public BankAccount update(BankAccount account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            BankAccount bankAccount = session.get(BankAccount.class, account.getAccountNumber());
            bankAccount.setBalance(account.getBalance());
            bankAccount.setHolderName(account.getHolderName());
            session.update(bankAccount);
            transaction.commit();
            return bankAccount;
        }
    }

    @Override
    public BankAccount get(Long id) {
        try (Session session = sessionFactory.openSession()) {
            BankAccount bankAccount = session.get(BankAccount.class, id);
            session.close();
            return bankAccount;
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            BankAccount bankAccount = session.get(BankAccount.class, id);
            session.delete(bankAccount);
            transaction.commit();
        }
    }
}
