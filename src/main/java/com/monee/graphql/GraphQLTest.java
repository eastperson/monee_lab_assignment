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

import java.io.IOException;

public class GraphQLTest {
    public static void main(String[] args) throws IOException {

        AccountDao accountDao = new AccountDao();
        AccountDataFetcher accountDataFetcher = new AccountDataFetcher(accountDao);
        AllAccountDataFetcher allAccountDataFetcher = new AllAccountDataFetcher(accountDao);
        AccountService accountService = new AccountService(accountDao);
        CreateAccountDataFetcher createAccountDataFetcher = new CreateAccountDataFetcher(accountService);
        UpdateAccountDataFetcher updateAccountDataFetcher = new UpdateAccountDataFetcher(accountService);

        PostDao postDao = new PostDao(accountDao);
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

    }
}
