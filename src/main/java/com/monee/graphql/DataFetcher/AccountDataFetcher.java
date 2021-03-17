package com.monee.graphql.DataFetcher;

import com.monee.dao.AccountDao;
import com.monee.model.Account;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class AccountDataFetcher implements DataFetcher<Account> {

    private final AccountDao accountDao;

    public AccountDataFetcher(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @Override
    public Account get(DataFetchingEnvironment environment) throws Exception {

        System.out.println("account date fetcher===========");
        System.out.println(environment.getArguments());

        String seqStr = environment.getArgument("seq");
        Long seq = Long.valueOf(seqStr);

        System.out.println("account date fetcher===========" + seq);
        return accountDao.findById(seq).orElseThrow(Exception::new);
    }
}
