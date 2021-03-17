package com.monee.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.monee.dao.AccountDao;
import com.monee.model.Account;

import java.util.List;
import java.util.Optional;

public class AccountResolver implements GraphQLResolver<Account> {

    private final AccountDao accountDao;

    public AccountResolver(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public List<Account> getAccounts(Long seq){
        return accountDao.findAll();
    }

    public Optional<Account> getAccount(Long seq){
        return accountDao.findById(seq);
    }

}
