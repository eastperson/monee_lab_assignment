package com.monee.graphql.DataFetcher.post;

import com.monee.dao.PostDao;
import com.monee.graphql.DataFetcher.account.AllAccountDataFetcher;
import com.monee.model.Post;
import com.monee.service.PostService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

public class AllPostDataFetcher implements DataFetcher<ResultApi<List<Post>>> {

    private final PostService postService;

    public AllPostDataFetcher(PostService postService) {
        this.postService = postService;
    }

    @Override
    public ResultApi<List<Post>> get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        System.out.println("=======================all post");
        ResultApi result = new ResultApi();
        result.setStatus(ResultApi.statusCode.OK);
        result.setSuccess(true);
        result.setData(postService.findAll());
        return result;
    }
}
