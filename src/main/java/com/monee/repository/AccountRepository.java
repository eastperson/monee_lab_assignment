package com.monee.repository;

import com.monee.model.Account;

import java.awt.print.Pageable;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById(Long seq) throws SQLException;

    List<Account> findAll() throws SQLException;

    List<Account> findAll(Pageable pageable);

    Optional<Account> save(Account account) throws SQLException;

}
