package com.monee.graphql.DataFetcher.account;

import com.monee.dao.AccountDao;
import com.monee.dto.AccountDto;
import com.monee.model.Account;
import com.monee.service.AccountService;
import com.monee.utils.ResultApi;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

public class CreateAccountDataFetcher implements DataFetcher<ResultApi<Account>> {

    private final AccountService accountService;

    public CreateAccountDataFetcher(AccountService accountService){
        this.accountService = accountService;
    }

    @Override
    public ResultApi<Account> get(DataFetchingEnvironment environment) throws Exception {

        System.out.println("create account data fetcher");

        String email = environment.getArgument("email");
        String nickname = environment.getArgument("nickname");
        String password = environment.getArgument("password");

        System.out.println("email : "+email);
        System.out.println("nickname : " + nickname);
        System.out.println("password : " + password);

        AccountDto accountDto = new AccountDto();
        accountDto.setEmail(email);
        accountDto.setNickname(nickname);
        accountDto.setPassword(password);

        System.out.println("dto : " + accountDto);

        Account account = accountService.signup(accountDto);

        System.out.println("account : " + account);

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
