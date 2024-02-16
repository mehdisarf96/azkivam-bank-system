package com.azkivam.banksystem.dao;

import com.azkivam.banksystem.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<BankAccount, Long> {
}
