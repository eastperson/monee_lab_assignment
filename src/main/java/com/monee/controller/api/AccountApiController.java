package com.monee.controller.api;

import com.google.gson.Gson;
import com.monee.controller.annotation.ApiController;
import com.monee.controller.annotation.ControllerMapping;
import com.monee.dto.AccountDto;
import com.monee.dto.LoginResult;
import com.monee.graphql.ServiceGraphQLProvider;
import com.monee.model.Account;
import com.monee.pool.ObjectPool;
import com.monee.security.Jwt;
import com.monee.service.AccountService;
import com.monee.utils.ResponseEntity;
import com.monee.utils.ResultApi;
import graphql.ExecutionResult;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiController
public class AccountApiController {

    private static Logger log = LoggerFactory.getLogger(AccountApiController.class);

    private ServiceGraphQLProvider accountServiceGraphQLProvider;

    public AccountApiController()  {
        ObjectPool pool = ObjectPool.getInstance();
        this.accountServiceGraphQLProvider = pool.getAccountServiceGraphQLProvider();
        try {
            accountServiceGraphQLProvider.loadSchema();
            log.info("generate complete");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("generate IOException error");
        }
    }

    @ControllerMapping(value = "/api/account", method = "POST", responseType = "application/json")
    public ResponseEntity<String> response(Map<String, List<String>> params, Map<String,String> payload){

        log.info("account api controller");
        log.info("grahpql response");

        String query = payload.get("query").toString();
        ExecutionResult execute = this.accountServiceGraphQLProvider.execute(query);

        return new ResponseEntity<>(200,true,execute.getData().toString());
    }

    @ControllerMapping(value = "/api/account/signup", method = "POST", responseType = "application/json")
    public ResponseEntity<String> signup(Map<String, List<String>> params, Map<String,Object> payload){

        log.info("account api controller");
        log.info("signup response");

        log.info("payload : "+payload);
        log.info("request class : "+payload.get("request").getClass());
        log.info("request : "+payload.get("request").toString());

        Gson gson = ObjectPool.getInstance().getGson();
        AccountDto dto = gson.fromJson((String) payload.get("request").toString(),AccountDto.class);

        ObjectPool pool = ObjectPool.getInstance();
        AccountService accountService = pool.getAccountService();

        Account account = null;
        try {
            account = accountService.signup(dto);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        log.info("account : " + account);

        String email = dto.getEmail();
        String password = dto.getPassword();
        ResultApi<LoginResult> result = new ResultApi<>();
        result.setSuccess(false);
        result.setStatus(ResultApi.statusCode.BAD_REQUEST);
        int status = 400;
        boolean success = false;
        //exchange.sendResponseHeaders(ResultApi.statusCode.BAD_REQUEST,result.toString().length());

        log.info("result : " + result);

        try {
            if(accountService.login(email,password)){
                log.info("login success");
                account = accountService.findByEmail(email).orElseThrow(NullPointerException::new);
                Jwt jwt = new Jwt("issuer","clientSecret",60*60*7);
                String accessToken = account.newJwt(jwt);
                result.setData(new LoginResult(accessToken,account));
                result.setSuccess(true);
                result.setStatus(ResultApi.statusCode.CREATED);
                status = 201;
                success = true;
                log.info(String.valueOf(result));
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(status,success,gson.toJson(result));
    }

    @ControllerMapping(value = "/api/account/login", method = "POST", responseType = "application/json")
    public ResponseEntity<String> login(Map<String, List<String>> params, Map<String,Object> payload){

        log.info("account api controller");
        log.info("login response");

        Gson gson = ObjectPool.getInstance().getGson();
        Map<String,String> map = gson.fromJson( payload.get("request").toString(), HashMap.class);
        log.info("map : "+map);

        String email = map.get("email");
        String password = map.get("password");

        log.info("password : "+ map.get(password));

        ObjectPool pool = ObjectPool.getInstance();
        AccountService accountService = pool.getAccountService();

        ResultApi<LoginResult> result = new ResultApi<>();
        result.setSuccess(false);
        result.setStatus(ResultApi.statusCode.BAD_REQUEST);
        int status = 400;
        boolean success = false;

        log.info("email : "+email);
        log.info("password : " + password);
        log.info("result : " + result);

        try {
            if(accountService.login(email,password)){
                log.info("login success");
                Account account = accountService.findByEmail(email).orElseThrow(NullPointerException::new);
                Jwt jwt = new Jwt("issuer","clientSecret",60*60*7);
                String accessToken = account.newJwt(jwt);
                result.setData(new LoginResult(accessToken,account));
                result.setSuccess(true);
                result.setStatus(ResultApi.statusCode.OK);
                log.info(String.valueOf(result));
                status = 200;
                success = true;
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(status,success,gson.toJson(result));
    }

}
