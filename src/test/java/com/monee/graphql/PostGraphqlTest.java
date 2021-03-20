package com.monee.graphql;

import com.monee.pool.ObjectPool;
import graphql.ExecutionResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PostGraphqlTest {

    private static Logger log = LoggerFactory.getLogger(PostGraphqlTest.class);

    @DisplayName("allPosts 테스트")
    @Test
    void findByAll() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "query{allPosts{success,status,data{seq,title,content,revwCnt,author{seq,nickname,email}}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    private ServiceGraphQLProvider getAccountServiceGraphQLProvider() throws IOException {
        return ObjectPool.getInstance().getAccountServiceGraphQLProvider();
    }

    @DisplayName("post find by id 테스트")
    @Test
    void findById() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "query{Post(seq:1){success,status,data{seq,title,content,revwCnt}}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("post find by id with account 테스트")
    @Test
    void findByIdWithAccount() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "query{Post(seq:1){success,status,data{seq,title,content,revwCnt,author{seq,nickname,email}}}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("post find by id with reply list 테스트")
    @Test
    void findByIdWithReplyList() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "query{Post(seq:1){success,status,data{seq,title,content,revwCnt,author{nickname},replyList{seq,content,author{seq,nickname,email}}}}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }



    @DisplayName("create post 테스트")
    @Test
    void createPost() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

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
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation {updatePost(seq:1,title:\"제목 수정\"){success,status,data{seq,title,content}}}";
        //String query = "mutation {createPost(title:\"title\",content:\"content\",author_seq : \"1\"){seq,title,content,revwCnt}}";


        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("update post content 테스트")
    @Test
    void update_post_content() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation {updatePost(seq:1,content:\"내용 수정\"){success,status,data{seq,title,content}}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("delete post 테스트")
    @Test
    void delete_post() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation {deletePost(seq:7){success,status,data{seq,title,content,revwCnt}}}";
        //String query = "query{allPosts{seq,title,content,revwCnt}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());

    }

    @DisplayName("like add post 테스트")
    @Test
    void post_like_add() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

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
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

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
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

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
