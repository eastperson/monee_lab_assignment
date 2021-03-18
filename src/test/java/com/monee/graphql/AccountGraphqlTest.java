package com.monee.graphql;

import com.monee.dao.AccountDao;
import com.monee.dao.LikeDao;
import com.monee.dao.PostDao;
import com.monee.dao.ReplyDao;
import com.monee.graphql.DataFetcher.account.AccountDataFetcher;
import com.monee.graphql.DataFetcher.account.AllAccountDataFetcher;
import com.monee.graphql.DataFetcher.account.CreateAccountDataFetcher;
import com.monee.graphql.DataFetcher.account.UpdateAccountDataFetcher;
import com.monee.graphql.DataFetcher.post.AllPostDataFetcher;
import com.monee.graphql.DataFetcher.post.CreatePostDataFetcher;
import com.monee.graphql.DataFetcher.post.DeletePostDataFetcher;
import com.monee.graphql.DataFetcher.post.LikePostDataFetcher;
import com.monee.graphql.DataFetcher.post.PostDataFetcher;
import com.monee.graphql.DataFetcher.post.UpdatePostDataFetcher;
import com.monee.graphql.DataFetcher.reply.AllReplyDataFetcher;
import com.monee.graphql.DataFetcher.reply.CreateReplyDataFetcher;
import com.monee.graphql.DataFetcher.reply.DeleteReplyDataFetcher;
import com.monee.graphql.DataFetcher.reply.ReplyDataFetcher;
import com.monee.graphql.DataFetcher.reply.UpdateReplyDataFetcher;
import com.monee.model.Account;
import com.monee.model.Reply;
import com.monee.service.AccountService;
import com.monee.service.PostService;
import com.monee.service.ReplyService;
import graphql.ExecutionResult;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountGraphqlTest {

    private static Logger log = LoggerFactory.getLogger(AccountGraphqlTest.class);

    @DisplayName("AccountServiceGraphQLProvide accounts 테스트")
    @Test
    void graphqlProvider_Test() throws IOException {

        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "{allAccounts{success,status,data{seq,email,nickname,seq}}}";

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("AccountServiceGraphQLProvide account 테스트")
    @Test
    void graphqlProvider_account_test() throws IOException {

        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        //String query = "query{Account(seq:1){seq,nickname}}";
        String query = "query{Account(seq:1){success,status,data{seq,email,nickname,password}}}";
        //query = "{allAccounts{email,nickname,seq}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("Query getAccounts 클래스")
    @Test
    void query_accounts(){
        AccountDao accountDao = new AccountDao();
        Query query = new Query(accountDao);
        List<Account> accounts = query.getAccounts();

        assertNotNull(accounts);
        log.info(accounts.toString());
    }

    @DisplayName("Query getAccount 클래스")
    @Test
    void query_account(){
        AccountDao accountDao = new AccountDao();
        Query query = new Query(accountDao);
        Account account = query.getAccount(1L);

        assertNotNull(account);
        log.info(account.toString());
    }

    @DisplayName("Mutation createAccount 테스트")
    @Test
    void mutation_create_account() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation {createAccount(email:\""+ UUID.randomUUID().toString().substring(0,10)+"@com.com\",nickname:\"eastperson\",password:\"123123\"){success,status,data{seq,email,nickname,password}}}";
        //query = "mutation{allAccounts{email,nickname,seq}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    @DisplayName("Mutation update nickname")
    @Test
    void mutation_update_account_nickname() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation {updateAccount(seq:1,nickname:\"eastperson2\"){success,status,data{seq,nickname,password}}}";
        //query = "mutation{allAccounts{email,nickname,seq}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    private AccountServiceGraphQLProvider getAccountServiceGraphQLProvider() throws IOException {
        AccountDao accountDao = new AccountDao();
        AccountDataFetcher accountDataFetcher = new AccountDataFetcher(accountDao);
        AllAccountDataFetcher allAccountDataFetcher = new AllAccountDataFetcher(accountDao);
        AccountService accountService = new AccountService(accountDao);
        CreateAccountDataFetcher createAccountDataFetcher = new CreateAccountDataFetcher(accountService);
        UpdateAccountDataFetcher updateAccountDataFetcher = new UpdateAccountDataFetcher(accountService);

        PostDao postDao = new PostDao();
        LikeDao likeDao = new LikeDao();
        PostService postService = new PostService(postDao,likeDao);
        PostDataFetcher postDataFetcher = new PostDataFetcher(postService);
        AllPostDataFetcher allPostDataFetcher = new AllPostDataFetcher(postService);
        CreatePostDataFetcher createPostDataFetcher = new CreatePostDataFetcher(postService);
        UpdatePostDataFetcher updatePostDataFetcher = new UpdatePostDataFetcher(postService);
        DeletePostDataFetcher deletePostDataFetcher = new DeletePostDataFetcher(postService);
        LikePostDataFetcher likePostDataFetcher = new LikePostDataFetcher(postService);

        ReplyDao replyDao = new ReplyDao(accountDao, postDao);
        ReplyService replyService = new ReplyService(replyDao);
        AllReplyDataFetcher allReplyDataFetcher = new AllReplyDataFetcher(replyService);
        CreateReplyDataFetcher createReplyDataFetcher = new CreateReplyDataFetcher(replyService);
        DeleteReplyDataFetcher deleteReplyDataFetcher = new DeleteReplyDataFetcher(replyService);
        ReplyDataFetcher replyDataFetcher = new ReplyDataFetcher(replyService);
        UpdateReplyDataFetcher updateReplyDataFetcher = new UpdateReplyDataFetcher(replyService);

        AccountServiceGraphQLProvider accountServiceGraphQLProvider = new AccountServiceGraphQLProvider(accountDao, allAccountDataFetcher, accountDataFetcher, createAccountDataFetcher, updateAccountDataFetcher,
                postDataFetcher, allPostDataFetcher, createPostDataFetcher, updatePostDataFetcher, deletePostDataFetcher
                , allReplyDataFetcher, createReplyDataFetcher, deleteReplyDataFetcher, replyDataFetcher, updateReplyDataFetcher,likePostDataFetcher);
        accountServiceGraphQLProvider.loadSchema();
        return accountServiceGraphQLProvider;
    }

    @DisplayName("Mutation update password")
    @Test
    void mutation_update_account_password() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation {updateAccount(seq:1,password:\"aaabbbcccc\"){success,status,data{seq,nickname,password}}}";
        //query = "mutation{allAccounts{email,nickname,seq}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }
}
