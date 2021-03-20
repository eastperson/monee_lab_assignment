package com.monee.controller;

import com.monee.annotation.controller.ApiController;
import com.monee.annotation.controller.ControllerMapping;
import com.monee.graphql.AccountServiceGraphQLProvider;
import com.monee.pool.ObjectPool;
import com.monee.utils.ResponseEntity;
import graphql.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@ApiController
public class PostApiController {

    private static Logger log = LoggerFactory.getLogger(PostApiController.class);

    private AccountServiceGraphQLProvider accountServiceGraphQLProvider;

    public PostApiController()  {
        ObjectPool pool = ObjectPool.getInstance();
        this.accountServiceGraphQLProvider = new AccountServiceGraphQLProvider(pool.getAccountDao(),pool.getAllAccountDataFetcher()
                ,pool.getAccountDataFetcher(),pool.getCreateAccountDataFetcher(),pool.getUpdateAccountDataFetcher(),pool.getPostDataFetcher(),pool.getAllPostDataFetcher()
                ,pool.getCreatePostDataFetcher(),pool.getUpdatePostDataFetcher(),pool.getDeletePostDataFetcher(),pool.getAllReplyDataFetcher(),pool.getCreateReplyDataFetcher()
                ,pool.getDeleteReplyDataFetcher(),pool.getReplyDataFetcher(),pool.getUpdateReplyDataFetcher(),pool.getLikePostDataFetcher());
        try {
            accountServiceGraphQLProvider.loadSchema();
            log.info("generate complete");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("generate error");
        }
    }

    @ControllerMapping(value = "/api/post", method = "POST", responseType = "application/json")
    public ResponseEntity<String> response(Map<String, List<String>> params, Map<String,String> payload){

        log.info("============================post api controller");

        String query = payload.get("query");
        ExecutionResult execute = this.accountServiceGraphQLProvider.execute(query);
        log.info("execute : "+execute.getData().toString());

        return new ResponseEntity<>(200,true,execute.getData().toString());
    }
}
