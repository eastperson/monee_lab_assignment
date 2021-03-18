package com.monee.graphql.DataFetcher.reply;

import com.monee.model.Post;
import com.monee.model.Reply;
import com.monee.service.ReplyService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class ReplyDataFetcher implements DataFetcher<ResultApi<Reply>> {

    private final ReplyService replyService;

    public ReplyDataFetcher(ReplyService replyService) {
        this.replyService = replyService;
    }

    @Override
    public ResultApi<Reply> get(DataFetchingEnvironment environment) throws Exception {
        String seqStr = environment.getArgument("seq");
        Long seq = Long.valueOf(seqStr);

        Reply reply = replyService.findByIdWithAccount(seq);
        System.out.println(reply);

        ResultApi<Reply> result = new ResultApi<>();
        if(reply != null){
            result.setStatus(ResultApi.statusCode.OK);
            result.setSuccess(true);
            result.setData(reply);
            return  result;
        }

        result.setSuccess(false);
        result.setStatus(ResultApi.statusCode.NOT_FOUND);
        result.setData(reply);
        return result;
    }
}
