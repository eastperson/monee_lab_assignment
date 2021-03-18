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

        log.info("======================create reply");

        String authorSeqStr = environment.getArgument("author_seq");
        log.info("======================create reply");
        Long authorSeq = Long.valueOf(authorSeqStr);
        log.info("======================create reply");
        String postSeqStr = environment.getArgument("post_seq");
        log.info("======================create reply");
        Long postSeq = Long.valueOf(postSeqStr);
        log.info("======================create reply");
        String content = environment.getArgument("content");
        log.info("======================create reply");

        Reply newReply = replyService.save(authorSeq,postSeq,content);
        log.info("======================create reply");
        log.info("======================create new reply : " + newReply);
        Reply reply = replyService.findByIdWithAccount(newReply.getSeq());
        log.info("======================create reply");
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
