package com.monee.trash;

import com.google.gson.Gson;
import com.monee.dto.AccountDto;
import com.monee.dto.LoginResult;
import com.monee.graphql.ServiceGraphQLProvider;
import com.monee.model.Account;
import com.monee.pool.ObjectPool;
import com.monee.security.Jwt;
import com.monee.service.AccountService;
import com.monee.utils.ResultApi;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import graphql.ExecutionResult;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class RestControllerHandler implements HttpHandler  {

    private AccountService accountService;
    private static Logger log = LoggerFactory.getLogger(RestControllerHandler.class);
    private Jwt jwt;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        ObjectPool pool = ObjectPool.getInstance();

        log.info("handle======================");

        accountService = pool.getAccountService();

        ServiceGraphQLProvider accountServiceGraphQLProvider = new ServiceGraphQLProvider(pool.getAccountDao(),pool.getAllAccountDataFetcher()
            ,pool.getAccountDataFetcher(),pool.getCreateAccountDataFetcher(),pool.getUpdateAccountDataFetcher(),pool.getPostDataFetcher(),pool.getAllPostDataFetcher()
            ,pool.getCreatePostDataFetcher(),pool.getUpdatePostDataFetcher(),pool.getDeletePostDataFetcher(),pool.getAllReplyDataFetcher(),pool.getCreateReplyDataFetcher()
            ,pool.getDeleteReplyDataFetcher(),pool.getReplyDataFetcher(),pool.getUpdateReplyDataFetcher(),pool.getLikePostDataFetcher());
        accountServiceGraphQLProvider.loadSchema();

        Gson gson = new Gson();

        log.info("request header : " + exchange.getRequestHeaders().keySet());
        log.info("request header Content-type : " + exchange.getRequestHeaders().get("Content-type"));
        log.info("request method : " + exchange.getRequestMethod());

        int status = 200;

        if("POST".equals(exchange.getRequestMethod())){

            Headers responseHeaders = exchange.getResponseHeaders();

            responseHeaders.set("Content-Type", "application/json");

            URI uri = exchange.getRequestURI();

            log.info("uri path : " + uri.getPath());

            Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());

            log.info("params : "+ params);

            String respText = null;

            if(uri.getPath().equals("/api/test")){

                String noNameText = "Anonymous";

                String name = params.getOrDefault("name", List.of(noNameText)).stream().findFirst().orElse(noNameText);

                log.info("name : " + name);

                respText = format("Hello %s!", name);
            }

            if(uri.getPath().equals("/api/reply")){
                BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

                int b = 0;

                if (exchange.getRequestHeaders().get("Content-type").contains("application/json")){

                    log.info("json reqeust");

                    String str = "";
                    str += br.readLine().trim();

                    while((b = br.read()) != -1) {
                        try {
                            str += br.readLine().trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info(e.getMessage());
                            str += "}";
                        }

                        log.info(str);
                    }

                    log.info("request body : "+str);
                    Map<String,String> map = gson.fromJson(str,HashMap.class);
                    String query = map.get("query");

                    ExecutionResult execute = accountServiceGraphQLProvider.execute(query);
                    log.info("execute : "+execute.getData().toString());

                    respText = gson.toJson(execute.getData().toString());

                }
            }

            if(uri.getPath().equals("/api/post")){
                BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

                int b = 0;

                if (exchange.getRequestHeaders().get("Content-type").contains("application/json")){

                    log.info("json reqeust");

                    String str = "";
                    str += br.readLine().trim();

                    while((b = br.read()) != -1) {
                        try {
                            str += br.readLine().trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info(e.getMessage());
                            str += "}";
                        }

                        log.info(str);
                    }

                    log.info("request body : "+str);
                    Map<String,String> map = gson.fromJson(str,HashMap.class);
                    String query = map.get("query");

                    ExecutionResult execute = accountServiceGraphQLProvider.execute(query);
                    log.info("execute : "+execute.getData().toString());

                    respText = gson.toJson(execute.getData().toString());

                }
            }

            if(uri.getPath().equals("/api/account")){
                BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

                int b = 0;

                if (exchange.getRequestHeaders().get("Content-type").contains("application/json")){

                    log.info("json reqeust");

                    String str = "";
                    str += br.readLine().trim();

                    while((b = br.read()) != -1) {
                        try {
                            str += br.readLine().trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info(e.getMessage());
                            str += "}";
                        }

                        log.info(str);
                    }

                    log.info("request body : "+str);
                    Map<String,String> map = gson.fromJson(str,HashMap.class);
                    String query = map.get("query");

                    ExecutionResult execute = accountServiceGraphQLProvider.execute(query);
                    log.info("execute : "+execute.getData().toString());

                    respText = gson.toJson(execute.getData().toString());

                }
            }

            if(uri.getPath().equals("/api/account/login")){
                BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

                int b = 0;

                if (exchange.getRequestHeaders().get("Content-type").contains("application/json")){

                    log.info("json reqeust");
                    log.info("api login");

                    String str = "";
                    str += br.readLine().trim();

                    while((b = br.read()) != -1) {
                        try {
                            str += br.readLine().trim();
                        } catch (Exception e) {
                            log.info(e.getMessage());
                            str += "}";
                        }

                        log.info(str);
                    }

                    log.info("request body : "+str);
                    Map<String,String> map = gson.fromJson(str,HashMap.class);
                    String email = map.get("email");
                    String password = map.get("password");
                    ResultApi<LoginResult> result = new ResultApi<>();
                    result.setSuccess(false);
                    result.setStatus(ResultApi.statusCode.BAD_REQUEST);
                    status = 400;

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
                        }
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }

                    respText = gson.toJson(result.toString());

                }
            }

            if(uri.getPath().equals("/api/account/signup")){
                BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

                int b = 0;

                if (exchange.getRequestHeaders().get("Content-type").contains("application/json")){

                    log.info("json reqeust");

                    String str = "";
                    str += br.readLine().trim();

                    while((b = br.read()) != -1) {
                        try {
                            str += br.readLine().trim();
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            str += "}";
                        }
                        log.info(str);
                    }

                    log.info("request body : "+str);

                    AccountDto dto = gson.fromJson(str, AccountDto.class);

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
                    status = 400;
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
                            log.info(String.valueOf(result));
                        }
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }

                    respText = gson.toJson(result);

                }

                if(exchange.getRequestHeaders().get("Content-type").contains("application/x-www-form-urlencoded")){

                    log.info("form reqeust");

                    String str = "";

                    do {
                            str += br.readLine().trim();
                        } while((b = br.read()) != -1);

                    log.info("str : " + str);

                    Map<String, List<String>> form = splitQuery(str);

                    log.info("form data : " + form);

                    String email = form.get("email").get(0);
                    String nickname = form.get("nickname").get(0);
                    String password = form.get("password").get(0);

                    Set<Account.Role> roles = new HashSet<>();

                    roles.add(Account.Role.USER);

                    AccountDto dto = new AccountDto();
                    dto.setEmail(email);
                    dto.setNickname(nickname);
                    dto.setPassword(password);

                    log.info("account dto : " + dto);

                    Account account = null;
                    try {
                        account = accountService.signup(dto);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    log.info(String.valueOf(account));

                    respText = gson.toJson(account);

                    log.info("respText : " + respText);

                    String newUrl = "http://localhost:8080/";

                    exchange.getResponseHeaders().add("Location",newUrl);
                    status = 301;

                }

                OutputStream responseBody = exchange.getResponseBody();
                OutputStreamWriter ow = new OutputStreamWriter(responseBody,"UTF-8");
                BufferedWriter bw = new BufferedWriter(ow);

            }


            log.info("exchange response body : " + respText);
            log.info("status : " + status);
            log.info("response headers : "+exchange.getResponseHeaders() );
            exchange.sendResponseHeaders(status, respText.getBytes().length);

            OutputStream output = exchange.getResponseBody();
            output.write(respText.getBytes());
            output.flush();
            exchange.close();

        } else {
            exchange.sendResponseHeaders(405,-1);
        }
    }

    public static Map<String, List<String>> splitQuery(String query) {
        if (query == null || "".equals(query)) {
            return Collections.emptyMap();
        }

        return Pattern.compile("&").splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split("="), 2))
                .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())));

    }

    private static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is a required encoding", e);
        }
    }
}
