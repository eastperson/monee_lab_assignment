package com.monee.graphql.DataFetcher.account;

import com.monee.dao.AccountDao;
import com.monee.dao.ReplyDao;
import com.monee.dto.AccountDto;
import com.monee.model.Account;
import com.monee.service.AccountService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CreateAccountDataFetcher implements DataFetcher<ResultApi<Account>> {

    private final AccountService accountService;
    private static Logger log = LoggerFactory.getLogger(CreateAccountDataFetcher.class);

    public CreateAccountDataFetcher(AccountService accountService){
        this.accountService = accountService;
    }

    @Override
    public ResultApi<Account> get(DataFetchingEnvironment environment) throws Exception {

        log.info("create account data fetcher");

        String email = environment.getArgument("email");
        String nickname = environment.getArgument("nickname");
        String password = environment.getArgument("password");

        log.info("email : "+email);
        log.info("nickname : " + nickname);
        log.info("password : " + password);

        AccountDto accountDto = new AccountDto();
        accountDto.setEmail(email);
        accountDto.setNickname(nickname);
        accountDto.setPassword(password);

        log.info("dto : " + accountDto);

        Account account = accountService.signup(accountDto);

        log.info("account : " + account);

        ResultApi<Account> result = new ResultApi<>();

        if(account != null){
            result.setSuccess(true);
            result.setStatus(ResultApi.statusCode.OK);
            result.setData(account);
            return result;
        }
        result.setStatus(ResultApi.statusCode.NOT_FOUND);
        result.setSuccess(false);

        return result;
    }
}
