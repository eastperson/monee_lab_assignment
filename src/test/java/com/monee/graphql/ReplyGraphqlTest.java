package com.monee.graphql;

import com.monee.pool.ObjectPool;
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
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "query{Reply(seq:1){success,status,data{seq,content,author{seq,email,nickname,password}}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    @DisplayName("find all 댓글 테스트")
    @Test
    void reply_find_all() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "{allReplys{success,status,data{seq,content,author{seq,email,nickname}}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    @DisplayName("find by post id 댓글 테스트")
    @Test
    void reply_find_by_postSeq() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "{allReplys(post_seq:\"1\"){success,status,data{seq,content,author{seq,email,nickname}}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    @DisplayName("create reply 댓글 테스트")
    @Test
    void reply_create() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation{createReply(author_seq:\"1\",post_seq:\"1\",content:\"댓글 내용\"){success,status,data{seq,content,author{seq,email,nickname}}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    @DisplayName("update reply 댓글 테스트")
    @Test
    void reply_update() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation{updateReply(seq:1,content:\"댓글 수정\"){success,status,data{seq,content,author{seq,email,nickname}}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    @DisplayName("delete reply 댓글 테스트")
    @Test
    void reply_delete() throws IOException {
        ServiceGraphQLProvider accountServiceGraphQLProvider = getAccountServiceGraphQLProvider();

        String query = "mutation{deleteReply(seq:30){success,status,data{seq,content,author{seq,email,nickname}}}}";

        log.info(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        log.info(result.getData().toString());
    }

    private ServiceGraphQLProvider getAccountServiceGraphQLProvider() throws IOException {

        return ObjectPool.getInstance().getAccountServiceGraphQLProvider();
    }
}
