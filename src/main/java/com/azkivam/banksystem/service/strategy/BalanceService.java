package com.azkivam.banksystem.service.strategy;

import com.azkivam.banksystem.entity.BankAccount;
import com.azkivam.banksystem.service.Bank;
import com.azkivam.banksystem.service.TransactionType;
import org.springframework.stereotype.Service;

@Service(TransactionType.BALANCE)
public class BalanceService implements TransactionService {

    private Bank bank;

    public BalanceService(Bank bank) {
        this.bank = bank;
    }

    @Override
    public BankAccount apply(Long mainAccountNumber, Long destinationAccountNumber, String holderName, Double amount) {
        Double balance = bank.displayAccountBalance(mainAccountNumber);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(mainAccountNumber);
        bankAccount.setHolderName(holderName);
        bankAccount.setBalance(balance);
        return bankAccount;
    }
}
