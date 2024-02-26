package com.azkivam.banksystem.exception;

public class NegativeAmountException extends RuntimeException {
    public NegativeAmountException() {
        super("Amount Cannot Be Negative!");
    }

    public NegativeAmountException(String message) {
        super(message);
    }
}
