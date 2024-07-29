package com.mehdisarf.banksystem.service.strategy;

import com.mehdisarf.banksystem.entity.BankAccount;

public interface TransactionService {

    BankAccount apply(Long mainAccountNumber, Long destinationAccountNumber, String holderName, Double amount);
}
