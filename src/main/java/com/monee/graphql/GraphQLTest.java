package com.monee.graphql;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.monee.dao.AccountDao;
import com.monee.graphql.DataFetcher.AccountDataFetcher;
import com.monee.graphql.DataFetcher.AllAccountDataFetcher;

import java.io.IOException;

public class GraphQLTest {
    public static void main(String[] args) throws IOException {

        AccountDao accountDao = new AccountDao();
        AccountDataFetcher accountDataFetcher = new AccountDataFetcher(accountDao);
        AllAccountDataFetcher allAccountDataFetcher = new AllAccountDataFetcher(accountDao);
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = new AccountServiceGraphQLProvider(accountDao,accountDataFetcher,allAccountDataFetcher);
        accountServiceGraphQLProvider.loadSchema();
    }
}
