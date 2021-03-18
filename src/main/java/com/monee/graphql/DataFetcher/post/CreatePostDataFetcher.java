package com.monee.graphql.DataFetcher.post;

import com.monee.dao.PostDao;
import com.monee.model.Post;
import com.monee.service.PostService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatePostDataFetcher implements DataFetcher<ResultApi<Post>> {

    private final PostService postService;
    private static Logger log = LoggerFactory.getLogger(CreatePostDataFetcher.class);

    public CreatePostDataFetcher(PostService postService) {
        this.postService = postService;
    }

    @Override
    public ResultApi<Post> get(DataFetchingEnvironment environment) throws Exception {

        log.info("=================================create post");

        String title = environment.getArgument("title");

        log.info("=================================create post save");

        String content = environment.getArgument("content");

        log.info("=================================create post save");

        String authorStr = environment.getArgument("author_seq");

        log.info("=================================create post save");

        Long authorSeq = Long.valueOf(authorStr);

        log.info("=================================create post save");

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
