package com.azkivam.banksystem.dao.impl;

import com.azkivam.banksystem.dao.GenericDao;
import com.azkivam.banksystem.entity.BankAccount;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountDao implements GenericDao<BankAccount> {

    @Override
    public BankAccount create(BankAccount account) {
        return null;
    }

    @Override
    public BankAccount update(BankAccount account) {
        return null;
    }

    @Override
    public BankAccount get(Object id) {
        return null;
    }

    @Override
    public void delete(Object id) {

    }

    @Override
    public List<BankAccount> listAll() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }
}
