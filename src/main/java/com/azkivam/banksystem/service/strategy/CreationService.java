package com.azkivam.banksystem.service.strategy;

import com.azkivam.banksystem.entity.BankAccount;
import com.azkivam.banksystem.service.Bank;
import com.azkivam.banksystem.service.TransactionType;
import org.springframework.stereotype.Service;

@Service(TransactionType.CREATE)
public class CreationService implements TransactionService {

    private Bank bank;

    public CreationService(Bank bank) {
        this.bank = bank;
    }

    @Override
    public BankAccount apply(Long mainAccountNumber, Long destinationAccountNumber, String holderName, Double amount) {
        return bank.createAccount(holderName, amount);
    }
}
