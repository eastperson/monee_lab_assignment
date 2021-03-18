package com.monee.graphql.DataFetcher.reply;

import com.monee.model.Post;
import com.monee.model.Reply;
import com.monee.service.ReplyService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class CreateReplyDataFetcher implements DataFetcher<ResultApi<Reply>> {

    private final ReplyService replyService;

    public CreateReplyDataFetcher(ReplyService replyService){
        this.replyService = replyService;
    }


    @Override
    public ResultApi<Reply> get(DataFetchingEnvironment environment) throws Exception {

        System.out.println("======================create reply");

        String authorSeqStr = environment.getArgument("author_seq");
        System.out.println("======================create reply");
        Long authorSeq = Long.valueOf(authorSeqStr);
        System.out.println("======================create reply");
        String postSeqStr = environment.getArgument("post_seq");
        System.out.println("======================create reply");
        Long postSeq = Long.valueOf(postSeqStr);
        System.out.println("======================create reply");
        String content = environment.getArgument("content");
        System.out.println("======================create reply");

        Reply newReply = replyService.save(authorSeq,postSeq,content);
        System.out.println("======================create reply");
        System.out.println("======================create new reply : " + newReply);
        Reply reply = replyService.findByIdWithAccount(newReply.getSeq());
        System.out.println("======================create reply");
        ResultApi<Reply> result = new ResultApi<>();

        if(reply != null){
            result.setSuccess(true);
            result.setStatus(ResultApi.statusCode.OK);
            result.setData(reply);
            return  result;
        }
        result.setSuccess(false);
        result.setStatus(ResultApi.statusCode.NOT_FOUND);
        result.setData(reply);

        return result;
    }
}
