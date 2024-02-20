package com.azkivam.banksystem.service.strategy;

import com.azkivam.banksystem.entity.BankAccount;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TransactionFactory {

    private final Map<String, TransactionService> transactionServiceMap;

    public TransactionFactory(Map<String, TransactionService> transactionServiceMap) {
        this.transactionServiceMap = transactionServiceMap;
    }

    public BankAccount execute(String transactionType, Long mainAccountNumber, Long destinationAccountNumber,
                               String holderName, Double amount) {
        TransactionService transactionService = getTransactionService(transactionType);
        return transactionService.apply(mainAccountNumber, destinationAccountNumber, holderName, amount);
    }

    private TransactionService getTransactionService(String transactionType) {
        TransactionService transactionService = transactionServiceMap.get(transactionType);
        if (transactionService == null) {
            throw new RuntimeException("Unsupported transaction type");
        }
        return transactionService;
    }
}
