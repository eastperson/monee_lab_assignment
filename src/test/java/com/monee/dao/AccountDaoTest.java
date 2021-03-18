package com.monee.dao;

import com.monee.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountDaoTest {

    private AccountDao accountDao;

    private static Logger log = LoggerFactory.getLogger(AccountDaoTest.class);

    @DisplayName("시퀀스 찾기 테스트")
    @Test
    void findById() throws SQLException {

        accountDao = new AccountDao();

        Optional<Account> result = accountDao.findById(1L);

        log.info(result.toString());

        if(result.isPresent()){
            Account account = result.get();
            log.info(account.toString());
            assertNotNull(account);
            assertTrue(account.getSeq().equals(1L));
        }

    }

    @DisplayName("이메일 찾기 테스트")
    @Test
    void findByEmail() throws SQLException {

        accountDao = new AccountDao();

        Optional<Account> result = accountDao.findByEmail("kjuioq@gmail.com");

        log.info(result.toString());

        if(result.isPresent()){
            Account account = result.get();
            log.info(account.toString());
            assertNotNull(account);
            assertTrue(account.getEmail().equals("kjuioq@gmail.com"));
        }

    }

    @DisplayName("계정 전체 찾기 테스트")
    @Test
    void findAll() throws SQLException {

        accountDao = new AccountDao();

        List<Account> result = accountDao.findAll();

        log.info(result.toString());

        assertNotNull(result);

    }

    @DisplayName("계정 만들기 테스트")
    @Test
    @Order(1)
    void save() throws SQLException {

        accountDao = new AccountDao();

        String email = UUID.randomUUID().toString().substring(0,20) + "@email.com";
        Account account = new Account(email,"eastperson","123123");

        log.info("account : "  + account);

        Optional<Account> result = accountDao.save(account);

        if(result.isPresent()){

            Account newAccount = result.get();

            log.info("new account : "+ newAccount);

            assertNotNull(newAccount);
            assertTrue(account.getEmail().equals(newAccount.getEmail()));
            assertTrue(account.getNickname().equals(newAccount.getNickname()));
            assertTrue(account.getPassword().equals(newAccount.getPassword()));
        }

    }

    @DisplayName("닉네임 변경 테스트")
    @Test
    void updateNickname() throws SQLException {

        accountDao = new AccountDao();

        Account account = accountDao.findAll().get(0);

        int result = 0;

        if(account != null) {
            result = accountDao.updateNickname(account.getSeq(),"변경");

        }

        Account updated = accountDao.findById(account.getSeq()).get();


        assertTrue(result>0);
        assertFalse(updated.getNickname().equals(account.getNickname()));

        accountDao.updateNickname(account.getSeq(),account.getNickname());

    }


}
