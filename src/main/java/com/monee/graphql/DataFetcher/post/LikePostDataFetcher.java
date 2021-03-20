package com.monee.graphql.DataFetcher.post;

import com.monee.dao.LikeDao;
import com.monee.service.PostService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LikePostDataFetcher implements DataFetcher<ResultApi> {

    private final PostService postService;
    private static Logger log = LoggerFactory.getLogger(LikePostDataFetcher.class);

    public LikePostDataFetcher(PostService postService) {
        this.postService = postService;
    }

    @Override
    public ResultApi get(DataFetchingEnvironment environment) throws Exception {

        String accountSeqStr = environment.getArgument("account_seq");
        String postSeqStr = environment.getArgument("post_seq");
        Boolean isAdd = (Boolean) environment.getArgument("isAdd");
        Long accountSeq = Long.valueOf(accountSeqStr);
        Long postSeq = Long.valueOf(postSeqStr);

        boolean rs = false;
        ResultApi result = new ResultApi();

        if(isAdd == null){
            result.setStatus(ResultApi.statusCode.BAD_REQUEST);
            result.setSuccess(false);
            if(postService.isLikedPost(accountSeq,postSeq)){
                result.setStatus(ResultApi.statusCode.OK);
                result.setSuccess(true);
            }
            return result;
        }


        log.info("like");
        if(isAdd){
             rs = postService.addLike(accountSeq,postSeq);
            log.info("like rs : " + rs);
        }else {
            rs = postService.cancleLike(accountSeq,postSeq);
            log.info("like rs : " + rs);
        }

        if(rs) {
            result.setStatus(ResultApi.statusCode.OK);
            result.setSuccess(true);
            return result;
        }
        result.setStatus(ResultApi.statusCode.BAD_REQUEST);
        result.setSuccess(false);
        result.setData(null);
        return result;
    }
}
