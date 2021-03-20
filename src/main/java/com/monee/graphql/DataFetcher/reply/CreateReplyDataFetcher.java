package com.monee.graphql.DataFetcher.reply;

import com.monee.model.Post;
import com.monee.model.Reply;
import com.monee.service.ReplyService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateReplyDataFetcher implements DataFetcher<ResultApi<Reply>> {

    private final ReplyService replyService;
    private static Logger log = LoggerFactory.getLogger(CreateReplyDataFetcher.class);

    public CreateReplyDataFetcher(ReplyService replyService){
        this.replyService = replyService;
    }


    @Override
    public ResultApi<Reply> get(DataFetchingEnvironment environment) throws Exception {

        log.info("create reply");

        String authorSeqStr = environment.getArgument("author_seq");
        Long authorSeq = Long.valueOf(authorSeqStr);
        String postSeqStr = environment.getArgument("post_seq");
        Long postSeq = Long.valueOf(postSeqStr);
        String content = environment.getArgument("content");

        Reply newReply = replyService.save(authorSeq,postSeq,content);
        Reply reply = replyService.findByIdWithAccount(newReply.getSeq());
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
