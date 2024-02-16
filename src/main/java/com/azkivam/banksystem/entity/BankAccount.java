package com.azkivam.banksystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ACCOUNT")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACC_NO")
    private Long accountNumber;

    @Column(name = "NAME")
    private String holderName;

    @Column(name = "BALANCE")
    private Double balance;

    public BankAccount() {
    }

    public BankAccount(String holderName, Double balance) {
        this.holderName = holderName;
        this.balance = balance;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
