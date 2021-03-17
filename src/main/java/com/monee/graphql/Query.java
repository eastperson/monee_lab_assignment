package com.monee.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.monee.dao.AccountDao;
import com.monee.model.Account;

import java.util.List;

public class Query implements GraphQLQueryResolver {
    private final AccountDao accountDao;

    public Query(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    public Account getAccount(Long seq){
        return accountDao.findById(seq).orElseThrow(NullPointerException::new);
    }

    public List<Account> getAccounts(){
        return accountDao.findAll();
    }
}
