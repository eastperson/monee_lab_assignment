package com.monee.graphql.DataFetcher;

import com.monee.dao.AccountDao;
import com.monee.model.Account;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

public class AllAccountDataFetcher implements DataFetcher<List<Account>> {

    private final AccountDao accountDao;

    public AllAccountDataFetcher(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @Override
    public List<Account> get(DataFetchingEnvironment environment) throws Exception {
        return accountDao.findAll();
    }
}