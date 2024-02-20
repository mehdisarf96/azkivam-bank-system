package com.azkivam.banksystem.service.strategy;

import com.azkivam.banksystem.entity.BankAccount;

public interface TransactionService {

    BankAccount apply(Long mainAccountNumber, Long destinationAccountNumber, String holderName, Double amount);
}
