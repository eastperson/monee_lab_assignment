package com.monee.graphql.DataFetcher.account;

import com.monee.dao.AccountDao;
import com.monee.dao.ReplyDao;
import com.monee.model.Account;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AllAccountDataFetcher implements DataFetcher<ResultApi<List<Account>>> {

    private final AccountDao accountDao;
    private static Logger log = LoggerFactory.getLogger(AllAccountDataFetcher.class);
    
    public AllAccountDataFetcher(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @Override
    public ResultApi<List<Account>> get(DataFetchingEnvironment environment) throws Exception {
        log.info("all account data=================================");
        ResultApi<List<Account>> result = new ResultApi<>();
        result.setStatus(ResultApi.statusCode.OK);
        result.setSuccess(true);
        result.setData(accountDao.findAll());
        return result;
    }
}