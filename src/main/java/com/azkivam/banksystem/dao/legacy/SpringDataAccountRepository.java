package com.azkivam.banksystem.dao.legacy;

import com.azkivam.banksystem.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAccountRepository extends JpaRepository<BankAccount, Long> {
}
