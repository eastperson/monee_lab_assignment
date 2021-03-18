package com.monee.graphql.DataFetcher.post;

import com.monee.model.Post;
import com.monee.service.PostService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class DeletePostDataFetcher implements DataFetcher<ResultApi> {

    private final PostService postService;

    public DeletePostDataFetcher(PostService postService){
        this.postService = postService;
    }

    @Override
    public ResultApi<Post> get(DataFetchingEnvironment environment) throws Exception {

        System.out.println("===================delete");

        String seqStr = environment.getArgument("seq");
        Long seq = Long.valueOf(seqStr);

        ResultApi<Post> resultApi = new ResultApi();

        Post post = postService.findByIdWithAccount(seq);

        if(postService.delete(seq)){
            resultApi.setStatus(ResultApi.statusCode.OK);
            resultApi.setSuccess(true);
            resultApi.setData(post);
            return resultApi;
        }

        resultApi.setSuccess(false);
        resultApi.setStatus(ResultApi.statusCode.NOT_FOUND);
        resultApi.setData(null);

        return resultApi;
    }
}
