package com.mehdisarf.banksystem.service.strategy;

import com.mehdisarf.banksystem.entity.BankAccount;
import com.mehdisarf.banksystem.service.Bank;
import com.mehdisarf.banksystem.service.TransactionType;
import org.springframework.stereotype.Service;

@Service(TransactionType.TRANSFER)
public class TransferService implements TransactionService {

    private Bank bank;

    public TransferService(Bank bank) {
        this.bank = bank;
    }

    @Override
    public BankAccount apply(Long mainAccountNumber, Long destinationAccountNumber, String holderName, Double amount) {
        bank.transferFunds(mainAccountNumber, destinationAccountNumber, amount);
        return new BankAccount();
    }
}
