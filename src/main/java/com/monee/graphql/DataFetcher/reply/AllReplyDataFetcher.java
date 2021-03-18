package com.monee.graphql.DataFetcher.reply;

import com.monee.graphql.DataFetcher.post.AllPostDataFetcher;
import com.monee.model.Post;
import com.monee.model.Reply;
import com.monee.service.ReplyService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

public class AllReplyDataFetcher implements DataFetcher<ResultApi<List<Reply>>> {

    private ReplyService replyService;

    public AllReplyDataFetcher(ReplyService replyService){
        this.replyService = replyService;
    }


    @Override
    public ResultApi<List<Reply>> get(DataFetchingEnvironment environment) throws Exception {

        String postSeqStr = environment.getArgument("post_seq");

        Long postSeq = null;

        if(postSeqStr != null) postSeq = Long.valueOf(postSeqStr);

        ResultApi result = new ResultApi();

        if(postSeq != null) {
            result.setStatus(ResultApi.statusCode.OK);
            result.setSuccess(true);
            result.setData(replyService.findByPostSeq(postSeq));
            return result;
        }

        System.out.println("=======================all reply");
        result.setStatus(ResultApi.statusCode.OK);
        result.setSuccess(true);
        result.setData(replyService.findAll());
        return result;
    }
}
