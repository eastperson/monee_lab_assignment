package com.monee.graphql;

import com.monee.pool.ObjectPool;
import graphql.ExecutionResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountGraphqlTest {

    private static Logger log = LoggerFactory.getLogger(AccountGraphqlTest.class);

    @DisplayName("AccountServiceGraphQLProvide accounts 테스트")
    @Test
    void graphqlProvider_Test() throws IOException {

        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "{allAccounts{success,status,data{seq,email,nickname,seq}}}";

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("AccountServiceGraphQLProvide account 테스트")
    @Test
    void graphqlProvider_account_test() throws IOException {

        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "query{Account(seq:1){success,status,data{seq,email,nickname,password}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("Mutation createAccount 테스트")
    @Test
    void mutation_create_account() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation {createAccount(email:\""+ UUID.randomUUID().toString().substring(0,10)+"@com.com\",nickname:\"eastperson\",password:\"123123\"){success,status,data{seq,email,nickname,password}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    @DisplayName("Mutation update nickname")
    @Test
    void mutation_update_account_nickname() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation {updateAccount(seq:1,nickname:\"eastperson2\"){success,status,data{seq,nickname,password}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    private ServiceGraphQLProvider getAccountServiceGraphQLProvider() throws IOException {
        return ObjectPool.getInstance().getAccountServiceGraphQLProvider();
    }

    @DisplayName("Mutation update password")
    @Test
    void mutation_update_account_password() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation {updateAccount(seq:1,password:\"aaabbbcccc\"){success,status,data{seq,nickname,password}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }
}
