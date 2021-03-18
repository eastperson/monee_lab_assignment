package com.monee.graphql.DataFetcher.account;

import com.monee.dao.AccountDao;
import com.monee.model.Account;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.Optional;

public class AccountDataFetcher implements DataFetcher<ResultApi<Account>> {

    private final AccountDao accountDao;

    public AccountDataFetcher(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @Override
    public ResultApi<Account> get(DataFetchingEnvironment environment) throws Exception {

        System.out.println("account date fetcher===========");
        System.out.println(environment.getArguments());

        String seqStr = environment.getArgument("seq");
        Long seq = Long.valueOf(seqStr);

        System.out.println("account date fetcher===========" + seq);
        Optional<Account> findById = accountDao.findById(seq);
        ResultApi<Account> result = new ResultApi<>();
        if(findById.isPresent()){
            Account account = findById.get();
            result.setSuccess(true);
            result.setStatus(ResultApi.statusCode.OK);
            result.setData(account);
            return result;
        }
        result.setSuccess(false);
        result.setStatus(ResultApi.statusCode.NOT_FOUND);
        result.setData(null);

        return result;
    }
}
