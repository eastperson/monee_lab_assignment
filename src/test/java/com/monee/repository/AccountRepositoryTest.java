package com.monee.repository;

import com.monee.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountRepositoryTest {

    private JdbcAccountRepository jdbcAccountRepository;

    @DisplayName("시퀀스 찾기 테스트")
    @Test
    void findById() throws SQLException {

        jdbcAccountRepository = new JdbcAccountRepository();

        Optional<Account> result = jdbcAccountRepository.findById(1L);

        System.out.println(result);

        if(result.isPresent()){
            Account account = result.get();
            System.out.println(account);
            assertNotNull(account);
            assertTrue(account.getSeq().equals(1L));
        }

    }

    @DisplayName("계정 전체 찾기 테스트")
    @Test
    void findAll() throws SQLException {

        jdbcAccountRepository = new JdbcAccountRepository();

        List<Account> result = jdbcAccountRepository.findAll();

        System.out.println(result);

        assertNotNull(result);

    }

    @DisplayName("계정 만들기 테스트")
    @Test
    void save() throws SQLException {

        jdbcAccountRepository = new JdbcAccountRepository();

        String email = UUID.randomUUID().toString().substring(0,20) + "@email.com";
        Account account = new Account(email,"eastperson","123123");

        System.out.println("account : "  + account);

        Optional<Account> result = jdbcAccountRepository.save(account);

        if(result.isPresent()){

            Account newAccount = result.get();

            System.out.println("new account : "+ newAccount);

            assertNotNull(newAccount);
            assertTrue(account.getEmail().equals(newAccount.getEmail()));
            assertTrue(account.getNickname().equals(newAccount.getNickname()));
            assertTrue(account.getPassword().equals(newAccount.getPassword()));
        }

    }


}
