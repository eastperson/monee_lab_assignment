package com.monee.thread.account.service;

import com.monee.dao.AccountDao;
import com.monee.dto.AccountDto;
import com.monee.model.Account;
import com.monee.model.Reply;
import com.monee.pool.ObjectPool;
import com.monee.service.AccountService;
import com.monee.service.ReplyService;
import com.monee.thread.ThreadSafetyTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class CreateAccountThreadSafetyTest {

    private static Logger log = LoggerFactory.getLogger(CreateAccountThreadSafetyTest.class);

    public static void main(String[] args) throws SQLException {

        ObjectPool pool = ObjectPool.getInstance();
        AccountService accountService = pool.getAccountService();

        AccountDto dto = new AccountDto();
        dto.setNickname("nickname : ");
        dto.setEmail(UUID.randomUUID().toString().substring(0,10));
        dto.setPassword("123123123");

        log.info("new reply : "+accountService.signup(dto));

        System.out.println("Test start!!");

        new Thread(() -> {
            log.info("Thread 1 start");
            for (int i = 0; i< 1000; i++) {
                try {
                    AccountDto dto1 = new AccountDto();
                    dto1.setNickname("nickname : ");
                    dto1.setEmail(UUID.randomUUID().toString().substring(0,10).substring(0,10));
                    dto1.setPassword("123123123");
                    dto1.setEmail(dto1.getEmail() + "Thread 1 - test " + i);
                    dto1.setNickname(dto1.getNickname() + "Thread 1 - test " + i);
                    Account account = accountService.signup(dto1);
                    Thread.sleep((int) (Math.random() * 1000));
                    if(!account.getEmail().equals(dto1.getEmail())){
                        log.info("====================================================test1 error 발생");
                        log.info("error account : " + account);
                        System.exit(0);
                        break;
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.info("====================================================test1 error 발생");
                    System.exit(0);
                    break;
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i< 1000; i++) {
                try {
                    AccountDto dto2 = new AccountDto();
                    dto2.setNickname("nickname : ");
                    dto2.setEmail(UUID.randomUUID().toString().substring(0,10));
                    dto2.setPassword("123123123");
                    dto2.setEmail(dto2.getEmail() + "Thread 2 - test " + i);
                    dto2.setNickname(dto2.getNickname() + "Thread 2 - test " + i);
                    Account account = accountService.signup(dto2);
                    Thread.sleep((int) (Math.random() * 1000));
                    if(!account.getEmail().equals(dto2.getEmail())){
                        log.info("====================================================test2 error 발생");
                        log.info("error account : " + account);
                        System.exit(0);
                        break;
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.info("====================================================test2 error 발생");
                    System.exit(0);
                    break;
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i< 1000; i++) {
                try {
                    AccountDto dto3 = new AccountDto();
                    dto3.setNickname("nickname : ");
                    dto3.setEmail(UUID.randomUUID().toString().substring(0,10));
                    dto3.setPassword("123123123");
                    dto3.setEmail(dto3.getEmail() + "Thread 3 - test " + i);
                    dto3.setNickname(dto3.getNickname() + "Thread 3 - test " + i);
                    Account account = accountService.signup(dto3);
                    Thread.sleep((int) (Math.random() * 1000));
                    if(!account.getEmail().equals(dto3.getEmail())){
                        log.info("====================================================test3 error 발생");
                        log.info("error account : " + account);
                        System.exit(0);
                        break;
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.info("====================================================test3 error 발생");
                    System.exit(0);
                    break;
                }
            }
        }).start();
    }
}
