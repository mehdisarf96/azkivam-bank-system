package com.mehdisarf.banksystem.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ACCOUNT")
public class BankAccount implements Serializable { // for 2nd level p-cntxt

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACC_NO")
    private Long accountNumber;

    @Column(name = "NAME")
    private String holderName;

    @Column(name = "BALANCE")
    private Double balance;

    @Version
    @Column(name = "optlock", columnDefinition = "bigint DEFAULT 0", nullable = false)
    private long version = 0L;

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

    public long getVersion() {
        return version;
    }

    protected void setVersion(long version) {
        this.version = version;
    }


    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber=" + accountNumber +
                ", holderName='" + holderName + '\'' +
                ", balance=" + balance +
                ", version=" + version +
                '}';
    }
}
