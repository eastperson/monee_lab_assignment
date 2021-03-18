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
import com.monee.service.AccountService;
import com.monee.service.PostService;
import com.monee.service.ReplyService;
import com.monee.utill.PasswordEncoderTest;
import graphql.ExecutionResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ReplyGraphqlTest {

    private static Logger log = LoggerFactory.getLogger(ReplyGraphqlTest.class);

    @DisplayName("find by seq 댓글 테스트")
    @Test
    void test() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "query{Reply(seq:1){success,status,data{seq,content,author{seq,email,nickname,password}}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    @DisplayName("find all 댓글 테스트")
    @Test
    void reply_find_all() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "{allReplys{success,status,data{seq,content,author{seq,email,nickname}}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    @DisplayName("find by post id 댓글 테스트")
    @Test
    void reply_find_by_postSeq() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "{allReplys(post_seq:\"1\"){success,status,data{seq,content,author{seq,email,nickname}}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    @DisplayName("create reply 댓글 테스트")
    @Test
    void reply_create() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation{createReply(author_seq:\"1\",post_seq:\"1\",content:\"댓글 내용\"){success,status,data{seq,content,author{seq,email,nickname}}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    @DisplayName("update reply 댓글 테스트")
    @Test
    void reply_update() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation{updateReply(seq:1,content:\"댓글 수정\"){success,status,data{seq,content,author{seq,email,nickname}}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    @DisplayName("delete reply 댓글 테스트")
    @Test
    void reply_delete() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation{deleteReply(seq:30){success,status,data{seq,content,author{seq,email,nickname}}}}";

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
}
