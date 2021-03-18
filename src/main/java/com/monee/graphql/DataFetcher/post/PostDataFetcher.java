package com.monee.graphql.DataFetcher.post;

import com.monee.model.Post;
import com.monee.service.PostService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class PostDataFetcher implements DataFetcher<ResultApi<Post>> {

    private PostService postService;

    public PostDataFetcher(PostService postService){
        this.postService = postService;
    }

    @Override
    public ResultApi<Post> get(DataFetchingEnvironment environment) throws Exception {

        String seqStr = environment.getArgument("seq");
        Long seq = Long.valueOf(seqStr);

        Post post = postService.findByIdWithAll(seq);
        System.out.println(post);

        ResultApi<Post> result = new ResultApi<>();
        if(post != null){
            result.setStatus(ResultApi.statusCode.OK);
            result.setSuccess(true);
            result.setData(post);
            return  result;
        }

        result.setSuccess(false);
        result.setStatus(ResultApi.statusCode.NOT_FOUND);
        result.setData(post);
        return result;
    }
}
