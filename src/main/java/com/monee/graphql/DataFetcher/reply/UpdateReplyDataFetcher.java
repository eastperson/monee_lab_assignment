package com.monee.graphql.DataFetcher.reply;

import com.monee.model.Post;
import com.monee.model.Reply;
import com.monee.service.ReplyService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.log4j.Log4j2;

public class UpdateReplyDataFetcher implements DataFetcher<ResultApi<Reply>> {

    private final ReplyService replyService;

    public UpdateReplyDataFetcher(ReplyService replyService) {
        this.replyService = replyService;
    }

    @Override
    public ResultApi<Reply> get(DataFetchingEnvironment environment) throws Exception {
        String seqStr = environment.getArgument("seq");
        Long seq = Long.valueOf(seqStr);

        String content = environment.getArgument("content");
        ResultApi<Reply> result = new ResultApi<>();

        if(content != null){
            Reply reply = replyService.updateContent(seq,content);
            result.setStatus(ResultApi.statusCode.OK);
            result.setSuccess(true);
            result.setData(reply);
            return result;
        }

        result.setData(null);
        result.setSuccess(false);
        result.setStatus(ResultApi.statusCode.NOT_FOUND);

        return result;
    }
}
