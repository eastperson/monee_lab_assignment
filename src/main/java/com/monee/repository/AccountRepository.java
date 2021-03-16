package com.monee.repository;

import com.monee.model.Account;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById(Long id);

    List<Account> findAll();

    List<Account> findAll(Pageable pageable);

}
