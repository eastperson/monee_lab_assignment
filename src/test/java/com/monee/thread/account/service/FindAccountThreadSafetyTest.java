package com.monee.thread.account.service;

import com.monee.dto.AccountDto;
import com.monee.model.Account;
import com.monee.pool.ObjectPool;
import com.monee.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

public class FindAccountThreadSafetyTest {
    private static Logger log = LoggerFactory.getLogger(CreateAccountThreadSafetyTest.class);

    public static void main(String[] args) throws SQLException {

        ObjectPool pool = ObjectPool.getInstance();
        AccountService accountService = pool.getAccountService();

        System.out.println("Test start!!");
        log.info("exists " + accountService.findByEmail("kjuioqqq@naver.com"));

        new Thread(() -> {
            log.info("Thread 1 start");
            for (int i = 0; i< 1000; i++) {
                try {

                    Account account = accountService.findByEmail("kjuioqqq@naver.com").orElseThrow(NullPointerException::new);
                    Thread.sleep(300);
                    accountService.updatePassword(account.getSeq(),"123123");
                    Thread.sleep((int)Math.random()*1000);
                    accountService.login(account.getEmail(),"123123");
                    Thread.sleep((int)Math.random()*1000);
                    accountService.updateNickname(account.getSeq(),"updatedNickname");
                    Thread.sleep((int)Math.random()*1000);

                    if(!account.getEmail().equals("kjuioqqq@naver.com")){
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
                    Account account = accountService.findByEmail("kjuioqqq@naver.com").orElseThrow(NullPointerException::new);
                    Thread.sleep((int)Math.random()*1000);
                    accountService.updatePassword(account.getSeq(),"123123");
                    Thread.sleep((int)Math.random()*1000);
                    accountService.login(account.getEmail(),"123123");
                    Thread.sleep((int)Math.random()*1000);
                    accountService.updateNickname(account.getSeq(),"updatedNickname");
                    if(!account.getEmail().equals("kjuioqqq@naver.com")){
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
                    Account account = accountService.findByEmail("kjuioqqq@naver.com").orElseThrow(NullPointerException::new);
                    Thread.sleep((int)Math.random()*1000);
                    accountService.updatePassword(account.getSeq(),"123123");
                    Thread.sleep((int)Math.random()*1000);
                    accountService.login(account.getEmail(),"123123");
                    Thread.sleep((int)Math.random()*1000);
                    accountService.updateNickname(account.getSeq(),"updatedNickname");
                    if(!account.getEmail().equals("kjuioqqq@naver.com")){
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
