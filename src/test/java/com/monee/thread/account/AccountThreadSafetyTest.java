package com.monee.thread.account;

import com.monee.dto.AccountDto;
import com.monee.errors.ThreadException;
import com.monee.model.Account;
import com.monee.pool.ObjectPool;
import com.monee.service.AccountService;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.UUID;

public class AccountThreadSafetyTest {
    private static Logger log = LoggerFactory.getLogger(AccountThreadSafetyTest.class);

    public static void main(String[] args) throws SQLException {

        ObjectPool pool = ObjectPool.getInstance();
        AccountService accountService = pool.getAccountService();

        System.out.println("Test start!!");
        log.info("exists " + accountService.findByEmail("kjuioqqq@naver.com"));

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
                    if(!account.getEmail().equals(dto1.getEmail())){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    account = accountService.findByEmail("kjuioqqq@naver.com").orElseThrow(NullPointerException::new);
                    Thread.sleep(300);
                    account = accountService.updatePassword(account.getSeq(),"123123");
                    if(account == null || !BCrypt.checkpw("123123",account.getPassword())){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!accountService.login(account.getEmail(),"123123")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    account = accountService.updateNickname(account.getSeq(),"updatedNickname");
                    if(account == null || !account.getNickname().equals("updatedNickname")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!account.getEmail().equals("kjuioqqq@naver.com")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.error("====================================================test1 error 발생");
                    log.error(throwables.getMessage());
                    System.exit(0);
                    break;
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i< 1000; i++) {
                try {
                    AccountDto dto1 = new AccountDto();
                    dto1.setNickname("nickname : ");
                    dto1.setEmail(UUID.randomUUID().toString().substring(0,10).substring(0,10));
                    dto1.setPassword("123123123");
                    dto1.setEmail(dto1.getEmail() + "Thread 1 - test " + i);
                    dto1.setNickname(dto1.getNickname() + "Thread 1 - test " + i);
                    Account account = accountService.signup(dto1);
                    if(!account.getEmail().equals(dto1.getEmail())){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    account = accountService.findByEmail("kjuioqqq@naver.com").orElseThrow(NullPointerException::new);
                    Thread.sleep(300);
                    account = accountService.updatePassword(account.getSeq(),"123123");
                    if(account == null || !BCrypt.checkpw("123123",account.getPassword())){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!accountService.login(account.getEmail(),"123123")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    account = accountService.updateNickname(account.getSeq(),"updatedNickname");
                    if(account == null || !account.getNickname().equals("updatedNickname")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!account.getEmail().equals("kjuioqqq@naver.com")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.error("====================================================test2 error 발생");
                    log.error(throwables.getMessage());
                    System.exit(0);
                    break;
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i< 1000; i++) {
                try {
                    AccountDto dto1 = new AccountDto();
                    dto1.setNickname("nickname : ");
                    dto1.setEmail(UUID.randomUUID().toString().substring(0,10).substring(0,10));
                    dto1.setPassword("123123123");
                    dto1.setEmail(dto1.getEmail() + "Thread 1 - test " + i);
                    dto1.setNickname(dto1.getNickname() + "Thread 1 - test " + i);
                    Account account = accountService.signup(dto1);
                    if(!account.getEmail().equals(dto1.getEmail())){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    account = accountService.findByEmail("kjuioqqq@naver.com").orElseThrow(NullPointerException::new);
                    Thread.sleep(300);
                    account = accountService.updatePassword(account.getSeq(),"123123");
                    if(account == null || !BCrypt.checkpw("123123",account.getPassword())){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!accountService.login(account.getEmail(),"123123")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    account = accountService.updateNickname(account.getSeq(),"updatedNickname");
                    if(account == null || !account.getNickname().equals("updatedNickname")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!account.getEmail().equals("kjuioqqq@naver.com")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.error("====================================================test3 error 발생");
                    log.error(throwables.getMessage());
                    System.exit(0);
                    break;
                }
            }
        }).start();
    }
}
