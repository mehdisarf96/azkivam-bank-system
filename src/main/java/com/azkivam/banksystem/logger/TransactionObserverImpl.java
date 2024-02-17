package com.azkivam.banksystem.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionObserverImpl implements TransactionObserver {

    private static final Logger logger = LoggerFactory.getLogger(TransactionObserverImpl.class.getSimpleName());

    @Override
    public void onTransaction(String accountNumber, String transactionType, Double amount) {
        logger.info("Transaction Type:{} -- Account Number:{} -- Amount:{}",
                transactionType,
                accountNumber,
                amount);
    }
}
