package com.monee.graphql.DataFetcher.account;

import com.monee.dto.AccountDto;
import com.monee.model.Account;
import com.monee.service.AccountService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class UpdateAccountDataFetcher implements DataFetcher<ResultApi<Account>>{

        private final AccountService accountService;

        public UpdateAccountDataFetcher(AccountService accountService){
            this.accountService = accountService;
        }

        @Override
        public ResultApi<Account> get(DataFetchingEnvironment environment) throws Exception {

            System.out.println("update account nickname data fetcher");

            String seqStr =environment.getArgument("seq");
            Long seq = Long.valueOf(seqStr);
            String nickname = environment.getArgument("nickname");

            String password = environment.getArgument("password");

            System.out.println("seq : "+seq);

            Account account = null;

            ResultApi<Account> result = new ResultApi<>();
            result.setSuccess(false);
            result.setStatus(404);

            if(nickname != null){
                System.out.println("nickname : " + nickname);
                account = accountService.updateNickname(seq,nickname);
                result.setSuccess(true);
                result.setStatus(ResultApi.statusCode.OK);
                result.setData(account);
            }

            if(password != null) {
                System.out.println("password : " + password);
                account = accountService.updatePassword(seq,password);
                result.setSuccess(true);
                result.setStatus(ResultApi.statusCode.OK);
                result.setData(account);
            }

            System.out.println("account : " + account);
            result.setData(account);
            result.setSuccess(false);
            result.setStatus(ResultApi.statusCode.NOT_FOUND);

            return result;
        }

}
