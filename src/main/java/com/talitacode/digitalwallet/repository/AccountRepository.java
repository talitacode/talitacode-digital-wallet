package com.talitacode.digitalwallet.repository;

import com.talitacode.digitalwallet.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {}
