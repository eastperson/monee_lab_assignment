package com.monee.graphql.DataFetcher.account;

import com.monee.dao.AccountDao;
import com.monee.model.Account;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

public class AllAccountDataFetcher implements DataFetcher<ResultApi<List<Account>>> {

    private final AccountDao accountDao;

    public AllAccountDataFetcher(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @Override
    public ResultApi<List<Account>> get(DataFetchingEnvironment environment) throws Exception {
        System.out.println("all account data=================================");
        ResultApi<List<Account>> result = new ResultApi<>();
        result.setStatus(ResultApi.statusCode.OK);
        result.setSuccess(true);
        result.setData(accountDao.findAll());
        return result;
    }
}