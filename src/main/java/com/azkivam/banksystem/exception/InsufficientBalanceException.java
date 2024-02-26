package com.azkivam.banksystem.exception;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException() {
        super("The Account Does Not Have Enough Balance.");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
