package com.monee.graphql.DataFetcher.reply;

import com.monee.model.Post;
import com.monee.model.Reply;
import com.monee.service.ReplyService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteReplyDataFetcher implements DataFetcher<ResultApi<Reply>> {

    private final ReplyService replyService;
    private static Logger log = LoggerFactory.getLogger(DeleteReplyDataFetcher.class);

    public DeleteReplyDataFetcher(ReplyService replyService) {
        this.replyService = replyService;
    }

    @Override
    public ResultApi<Reply> get(DataFetchingEnvironment environment) throws Exception {

        log.info("delete");

        String seqStr = environment.getArgument("seq");
        Long seq = Long.valueOf(seqStr);

        ResultApi<Reply> resultApi = new ResultApi();

        Reply reply = replyService.findById(seq);

        if(replyService.delete(seq)){
            resultApi.setStatus(ResultApi.statusCode.OK);
            resultApi.setSuccess(true);
            resultApi.setData(reply);
            return resultApi;
        }

        resultApi.setSuccess(false);
        resultApi.setStatus(ResultApi.statusCode.NOT_FOUND);
        resultApi.setData(null);

        return resultApi;
    }

}
