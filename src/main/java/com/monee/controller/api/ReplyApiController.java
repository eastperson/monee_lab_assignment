package com.monee.controller.api;

import com.monee.controller.annotation.ApiController;
import com.monee.controller.annotation.ControllerMapping;
import com.monee.graphql.ServiceGraphQLProvider;
import com.monee.pool.ObjectPool;
import com.monee.utils.ResponseEntity;
import graphql.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@ApiController
public class ReplyApiController {

    private static Logger log = LoggerFactory.getLogger(ReplyApiController.class);

    private ServiceGraphQLProvider accountServiceGraphQLProvider;

    public ReplyApiController()  {
        ObjectPool pool = ObjectPool.getInstance();
        this.accountServiceGraphQLProvider = pool.getAccountServiceGraphQLProvider();
        try {
            accountServiceGraphQLProvider.loadSchema();
            log.info("generate complete");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("generate error");
        }
    }

    @ControllerMapping(value = "/api/reply", method = "POST", responseType = "application/json")
    public ResponseEntity<String> response(Map<String, List<String>> params, Map<String,String> payload){

        log.info("reply api controller");

        String query = payload.get("query");
        ExecutionResult execute = this.accountServiceGraphQLProvider.execute(query);
        log.info("execute : "+execute.getData().toString());

        return new ResponseEntity<>(200,true,execute.getData().toString());
    }
}
