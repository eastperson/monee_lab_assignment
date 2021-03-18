package com.monee.graphql.DataFetcher.post;

import com.monee.model.Post;
import com.monee.service.PostService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdatePostDataFetcher implements DataFetcher<ResultApi<Post>> {

    private PostService postService;
    private static Logger log = LoggerFactory.getLogger(UpdatePostDataFetcher.class);

    public UpdatePostDataFetcher(PostService postService){
        this.postService = postService;
    }

    @Override
    public ResultApi<Post> get(DataFetchingEnvironment environment) throws Exception {

        String seqStr = environment.getArgument("seq");
        Long seq = Long.valueOf(seqStr);
        String title = environment.getArgument("title");
        String content = environment.getArgument("content");

        ResultApi<Post> result = new ResultApi<>();

        if(title != null){
            Post post = postService.updateTitle(seq,title);
            result.setStatus(ResultApi.statusCode.OK);
            result.setSuccess(true);
            result.setData(post);
            return result;
        }
        if(content != null){
            Post post = postService.updateContent(seq,content);
            result.setStatus(ResultApi.statusCode.OK);
            result.setSuccess(true);
            result.setData(post);
            return result;
        }

        result.setData(null);
        result.setSuccess(false);
        result.setStatus(ResultApi.statusCode.NOT_FOUND);

        return result;
    }
}
