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
import graphql.ExecutionResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class PostGraphqlTest {

    private static Logger log = LoggerFactory.getLogger(PostGraphqlTest.class);

    @DisplayName("allPosts 테스트")
    @Test
    void findByAll() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "query{allPosts{success,status,data{seq,title,content,revwCnt,author{seq,nickname,email}}}}";

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

    @DisplayName("post find by id 테스트")
    @Test
    void findById() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "query{Post(seq:1){success,status,data{seq,title,content,revwCnt}}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("post find by id 테스트")
    @Test
    void findByIdWithAccount() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "query{Post(seq:1){success,status,data{seq,title,content,revwCnt,author{seq,nickname,email}}}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("post find by id with reply list 테스트")
    @Test
    void findByIdWithReplyList() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "query{Post(seq:1){success,status,data{seq,title,content,revwCnt,author{nickname},replyList{seq,content,author{seq,nickname,email}}}}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }



    @DisplayName("create post 테스트")
    @Test
    void createPost() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        //String query = "query{Post(seq:1){seq,title,content,revwCnt}}";
        String query = "mutation {createPost(title:\"title\",content:\"content\",author_seq : \"1\"){success,status,data{seq,title,content,revwCnt}}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("update post title 테스트")
    @Test
    void update_post_title() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation {updatePost(seq:1,title:\"제목 수정\"){success,status,data{seq,title,content}}}";
        //String query = "mutation {createPost(title:\"title\",content:\"content\",author_seq : \"1\"){seq,title,content,revwCnt}}";


        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("update post content 테스트")
    @Test
    void update_post_content() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation {updatePost(seq:1,content:\"내용 수정\"){success,status,data{seq,title,content}}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("delete post 테스트")
    @Test
    void delete_post() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation {deletePost(seq:7){success,status,data{seq,title,content,revwCnt}}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("like add post 테스트")
    @Test
    void post_like_add() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        //String query = "query{Post(seq:1){seq,title,content,revwCnt}}";
        String query = "mutation {addLikePost(account_seq:\"1\",post_seq:\"1\",isAdd:true){success,status}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("like remove post 테스트")
    @Test
    void post_like_remove() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        //String query = "query{Post(seq:1){seq,title,content,revwCnt}}";
        String query = "mutation {addLikePost(account_seq:\"1\",post_seq:\"1\",isAdd:false){success,status}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("is liked post 테스트")
    @Test
    void post_is_liked() throws IOException {
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        //String query = "query{Post(seq:1){seq,title,content,revwCnt}}";
        String query = "mutation {addLikePost(account_seq:\"1\",post_seq:\"2\"){success,status}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

//    @DisplayName("post find by id with author 테스트")
//    @Test
//    void findByIdWithAuthor() throws IOException {
//        AccountDao accountDao = new AccountDao();
//        AccountDataFetcher accountDataFetcher = new AccountDataFetcher(accountDao);
//        AllAccountDataFetcher allAccountDataFetcher = new AllAccountDataFetcher(accountDao);
//        AccountService accountService = new AccountService(accountDao);
//        CreateAccountDataFetcher createAccountDataFetcher = new CreateAccountDataFetcher(accountService);
//        UpdateAccountDataFetcher updateAccountDataFetcher = new UpdateAccountDataFetcher(accountService);
//
//        PostDao postDao = new PostDao();
//        PostService postService = new PostService(postDao);
//        PostDataFetcher postDataFetcher = new PostDataFetcher(postService);
//        AllPostDataFetcher allPostDataFetcher = new AllPostDataFetcher(postService);
//        CreatePostDataFetcher createPostDataFetcher = new CreatePostDataFetcher(postService);
//        UpdatePostDataFetcher updatePostDataFetcher = new UpdatePostDataFetcher(postService);
//        DeletePostDataFetcher deletePostDataFetcher = new DeletePostDataFetcher(postService);
//
//        AccountServiceGraphQLProvider accountServiceGraphQLProvider = new AccountServiceGraphQLProvider(accountDao,accountDataFetcher,allAccountDataFetcher,createAccountDataFetcher, updateAccountDataFetcher,
//                postDataFetcher,allPostDataFetcher,createPostDataFetcher,updatePostDataFetcher,deletePostDataFetcher);
//        accountServiceGraphQLProvider.loadSchema();
//
//        String query = "query{Post(seq:1){seq,title,content,author,revwCnt}}";
//        //String query = "query{allPosts{seq,title,content,revwCnt}}";
//
//        log.info(query);
//
//        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
//        log.info(result.getData().toString());
//
//    }
}
