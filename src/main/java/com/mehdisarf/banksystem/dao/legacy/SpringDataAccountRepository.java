package com.mehdisarf.banksystem.dao.legacy;

import com.mehdisarf.banksystem.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAccountRepository extends JpaRepository<BankAccount, Long> {
}
