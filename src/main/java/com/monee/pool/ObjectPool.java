package com.monee.pool;

import com.google.gson.Gson;
import com.monee.Filter.LoginFilter;
import com.monee.controller.handler.CustomHttpHandler;
import com.monee.graphql.ServiceGraphQLProvider;
import com.monee.trash.ControllerHandler;
import com.monee.trash.RestControllerHandler;
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

public class ObjectPool {

    private static ObjectPool objectPool = null;
    private AccountDao accountDao;
    private PostDao postDao;
    private ReplyDao replyDao;
    private LikeDao likeDao;
    private AccountService accountService;
    private PostService postService;
    private ReplyService replyService;
    private ControllerHandler controllerHandler;
    private RestControllerHandler restControllerHandler;
    private AccountDataFetcher accountDataFetcher;
    private AllAccountDataFetcher allAccountDataFetcher;
    private CreateAccountDataFetcher createAccountDataFetcher;
    private UpdateAccountDataFetcher updateAccountDataFetcher;
    private AllPostDataFetcher allPostDataFetcher;
    private CreatePostDataFetcher createPostDataFetcher;
    private DeletePostDataFetcher deletePostDataFetcher;
    private LikePostDataFetcher likePostDataFetcher;
    private PostDataFetcher postDataFetcher;
    private UpdatePostDataFetcher updatePostDataFetcher;
    private AllReplyDataFetcher allReplyDataFetcher;
    private CreateReplyDataFetcher createReplyDataFetcher;
    private DeleteReplyDataFetcher deleteReplyDataFetcher;
    private ReplyDataFetcher replyDataFetcher;
    private UpdateReplyDataFetcher updateReplyDataFetcher;
    private LoginFilter loginFilter;
    private CustomHttpHandler customHttpHandler;
    private ServiceGraphQLProvider accountServiceGraphQLProvider;
    private Gson gson;

    public static ObjectPool getInstance(){
        if(objectPool == null) {
            synchronized (ObjectPool.class){
                System.out.println("  ______ _____  _       _____  ________      __\n" +
                        " |  ____|  __ \\( )     |  __ \\|  ____\\ \\    / /\n" +
                        " | |__  | |__) |/ ___  | |  | | |__   \\ \\  / /\n" +
                        " |  __| |  ___/  / __| | |  | |  __|   \\ \\/ /\n" +
                        " | |____| |      \\__ \\ | |__| | |____   \\  /\n" +
                        " |______|_|      |___/ |_____/|______|   \\/");
                objectPool = new ObjectPool();
            }
        }
        return objectPool;
    }

    public ObjectPool(){
        this.accountDao = new AccountDao();
        this.postDao = new PostDao(accountDao);
        this.replyDao = new ReplyDao(accountDao,postDao);

        this.likeDao = new LikeDao();
        this.accountService = new AccountService(accountDao);
        this.postService = new PostService(postDao,likeDao);
        this.replyService = new ReplyService(replyDao);

        this.controllerHandler = new ControllerHandler();
        this.restControllerHandler = new RestControllerHandler();

        this.accountDataFetcher = new AccountDataFetcher(accountDao);
        this.allAccountDataFetcher = new AllAccountDataFetcher(accountDao);
        this.createAccountDataFetcher = new CreateAccountDataFetcher(accountService);
        this.updateAccountDataFetcher = new UpdateAccountDataFetcher(accountService);

        this.allPostDataFetcher = new AllPostDataFetcher(postService);
        this.createPostDataFetcher = new CreatePostDataFetcher(postService);
        this.deletePostDataFetcher = new DeletePostDataFetcher(postService);
        this.updatePostDataFetcher = new UpdatePostDataFetcher(postService);
        this.likePostDataFetcher = new LikePostDataFetcher(postService);
        this.postDataFetcher = new PostDataFetcher(postService);

        this.allReplyDataFetcher = new AllReplyDataFetcher(replyService);
        this.createReplyDataFetcher = new CreateReplyDataFetcher(replyService);
        this.deleteReplyDataFetcher = new DeleteReplyDataFetcher(replyService);
        this.replyDataFetcher = new ReplyDataFetcher(replyService);
        this.updateReplyDataFetcher = new UpdateReplyDataFetcher(replyService);
        this.loginFilter = new LoginFilter();
        this.customHttpHandler = new CustomHttpHandler();
        this.accountServiceGraphQLProvider = new ServiceGraphQLProvider(getAccountDao(),getAllAccountDataFetcher()
                ,getAccountDataFetcher(),getCreateAccountDataFetcher(),getUpdateAccountDataFetcher(),getPostDataFetcher(),getAllPostDataFetcher()
                ,getCreatePostDataFetcher(),getUpdatePostDataFetcher(),getDeletePostDataFetcher(),getAllReplyDataFetcher(),getCreateReplyDataFetcher()
                ,getDeleteReplyDataFetcher(),getReplyDataFetcher(),getUpdateReplyDataFetcher(),getLikePostDataFetcher());
        try {
            this.accountServiceGraphQLProvider.loadSchema();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.gson = new Gson();
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public CustomHttpHandler getCustomHttpHandler() {
        return customHttpHandler;
    }

    public ServiceGraphQLProvider getAccountServiceGraphQLProvider() {
        return accountServiceGraphQLProvider;
    }

    public void setAccountServiceGraphQLProvider(ServiceGraphQLProvider accountServiceGraphQLProvider) {
        this.accountServiceGraphQLProvider = accountServiceGraphQLProvider;
    }

    public void setCustomHttpHandler(CustomHttpHandler customHttpHandler) {
        this.customHttpHandler = customHttpHandler;
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public PostDao getPostDao() {
        return postDao;
    }

    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }

    public ReplyDao getReplyDao() {
        return replyDao;
    }

    public void setReplyDao(ReplyDao replyDao) {
        this.replyDao = replyDao;
    }

    public LikeDao getLikeDao() {
        return likeDao;
    }

    public void setLikeDao(LikeDao likeDao) {
        this.likeDao = likeDao;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public PostService getPostService() {
        return postService;
    }

    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    public ReplyService getReplyService() {
        return replyService;
    }

    public void setReplyService(ReplyService replyService) {
        this.replyService = replyService;
    }

    public ControllerHandler getControllerHandler() {
        return controllerHandler;
    }

    public void setControllerHandler(ControllerHandler controllerHandler) {
        this.controllerHandler = controllerHandler;
    }

    public RestControllerHandler getRestControllerHandler() {
        return restControllerHandler;
    }

    public void setRestControllerHandler(RestControllerHandler restControllerHandler) {
        this.restControllerHandler = restControllerHandler;
    }

    public AccountDataFetcher getAccountDataFetcher() {
        return accountDataFetcher;
    }

    public void setAccountDataFetcher(AccountDataFetcher accountDataFetcher) {
        this.accountDataFetcher = accountDataFetcher;
    }

    public AllAccountDataFetcher getAllAccountDataFetcher() {
        return allAccountDataFetcher;
    }

    public void setAllAccountDataFetcher(AllAccountDataFetcher allAccountDataFetcher) {
        this.allAccountDataFetcher = allAccountDataFetcher;
    }

    public CreateAccountDataFetcher getCreateAccountDataFetcher() {
        return createAccountDataFetcher;
    }

    public void setCreateAccountDataFetcher(CreateAccountDataFetcher createAccountDataFetcher) {
        this.createAccountDataFetcher = createAccountDataFetcher;
    }

    public UpdateAccountDataFetcher getUpdateAccountDataFetcher() {
        return updateAccountDataFetcher;
    }

    public void setUpdateAccountDataFetcher(UpdateAccountDataFetcher updateAccountDataFetcher) {
        this.updateAccountDataFetcher = updateAccountDataFetcher;
    }

    public AllPostDataFetcher getAllPostDataFetcher() {
        return allPostDataFetcher;
    }

    public void setAllPostDataFetcher(AllPostDataFetcher allPostDataFetcher) {
        this.allPostDataFetcher = allPostDataFetcher;
    }

    public CreatePostDataFetcher getCreatePostDataFetcher() {
        return createPostDataFetcher;
    }

    public void setCreatePostDataFetcher(CreatePostDataFetcher createPostDataFetcher) {
        this.createPostDataFetcher = createPostDataFetcher;
    }

    public DeletePostDataFetcher getDeletePostDataFetcher() {
        return deletePostDataFetcher;
    }

    public void setDeletePostDataFetcher(DeletePostDataFetcher deletePostDataFetcher) {
        this.deletePostDataFetcher = deletePostDataFetcher;
    }

    public LikePostDataFetcher getLikePostDataFetcher() {
        return likePostDataFetcher;
    }

    public void setLikePostDataFetcher(LikePostDataFetcher likePostDataFetcher) {
        this.likePostDataFetcher = likePostDataFetcher;
    }

    public PostDataFetcher getPostDataFetcher() {
        return postDataFetcher;
    }

    public void setPostDataFetcher(PostDataFetcher postDataFetcher) {
        this.postDataFetcher = postDataFetcher;
    }

    public UpdatePostDataFetcher getUpdatePostDataFetcher() {
        return updatePostDataFetcher;
    }

    public void setUpdatePostDataFetcher(UpdatePostDataFetcher updatePostDataFetcher) {
        this.updatePostDataFetcher = updatePostDataFetcher;
    }

    public AllReplyDataFetcher getAllReplyDataFetcher() {
        return allReplyDataFetcher;
    }

    public void setAllReplyDataFetcher(AllReplyDataFetcher allReplyDataFetcher) {
        this.allReplyDataFetcher = allReplyDataFetcher;
    }

    public CreateReplyDataFetcher getCreateReplyDataFetcher() {
        return createReplyDataFetcher;
    }

    public void setCreateReplyDataFetcher(CreateReplyDataFetcher createReplyDataFetcher) {
        this.createReplyDataFetcher = createReplyDataFetcher;
    }

    public DeleteReplyDataFetcher getDeleteReplyDataFetcher() {
        return deleteReplyDataFetcher;
    }

    public void setDeleteReplyDataFetcher(DeleteReplyDataFetcher deleteReplyDataFetcher) {
        this.deleteReplyDataFetcher = deleteReplyDataFetcher;
    }

    public ReplyDataFetcher getReplyDataFetcher() {
        return replyDataFetcher;
    }

    public void setReplyDataFetcher(ReplyDataFetcher replyDataFetcher) {
        this.replyDataFetcher = replyDataFetcher;
    }

    public UpdateReplyDataFetcher getUpdateReplyDataFetcher() {
        return updateReplyDataFetcher;
    }

    public void setUpdateReplyDataFetcher(UpdateReplyDataFetcher updateReplyDataFetcher) {
        this.updateReplyDataFetcher = updateReplyDataFetcher;
    }

    public LoginFilter getLoginFilter() {
        return loginFilter;
    }

    public void setLoginFilter(LoginFilter loginFilter) {
        this.loginFilter = loginFilter;
    }
}
