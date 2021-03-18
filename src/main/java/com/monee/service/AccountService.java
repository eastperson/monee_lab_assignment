package com.monee.service;

import com.monee.dao.AccountDao;
import com.monee.dto.AccountDto;
import com.monee.model.Account;

import java.sql.SQLException;
import java.util.Optional;

public class AccountService {

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    private AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    public Account signup(AccountDto accountDto) throws SQLException {
        Account account = new Account(accountDto.getEmail(),accountDto.getNickname(),accountDto.getPassword());

        System.out.println("account service singup : " + account);
        try{
            Optional<Account> newAccount = accountDao.save(account);
            if(newAccount.isPresent()){
                return newAccount.get();
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Account updateNickname(Long seq, String nickname) throws SQLException {
        int result = accountDao.updateNickname(seq,nickname);
        if(result < 1) return null;

        return accountDao.findById(seq).orElseThrow(NullPointerException::new);
    }

    public Account updatePassword(Long seq, String password) {
        int result = accountDao.updatePassword(seq,password);
        if(result < 1) return null;

        return accountDao.findById(seq).orElseThrow(NullPointerException::new);
    }
}
