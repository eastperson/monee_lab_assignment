package com.monee.graphql.DataFetcher.post;

import com.monee.dao.PostDao;
import com.monee.model.Post;
import com.monee.service.PostService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class CreatePostDataFetcher implements DataFetcher<ResultApi<Post>> {

    private final PostService postService;

    public CreatePostDataFetcher(PostService postService) {
        this.postService = postService;
    }

    @Override
    public ResultApi<Post> get(DataFetchingEnvironment environment) throws Exception {

        System.out.println("=================================create post");

        String title = environment.getArgument("title");

        System.out.println("=================================create post save");

        String content = environment.getArgument("content");

        System.out.println("=================================create post save");

        String authorStr = environment.getArgument("author_seq");

        System.out.println("=================================create post save");

        Long authorSeq = Long.valueOf(authorStr);

        System.out.println("=================================create post save");

        Post post = postService.save(title,content,authorSeq);
        ResultApi<Post> result = new ResultApi<>();

        if(post != null){
            result.setSuccess(true);
            result.setStatus(ResultApi.statusCode.OK);
            result.setData(post);
            return  result;
        }
        result.setSuccess(false);
        result.setStatus(ResultApi.statusCode.NOT_FOUND);
        result.setData(post);

        return result;
    }
}
