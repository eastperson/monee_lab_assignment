package com.monee.controller.handler;

import com.google.gson.Gson;
import com.monee.dao.AccountDao;
import com.monee.dao.LikeDao;
import com.monee.dao.PostDao;
import com.monee.dao.ReplyDao;
import com.monee.dto.AccountDto;
import com.monee.graphql.AccountServiceGraphQLProvider;
import com.monee.graphql.DataFetcher.account.AccountDataFetcher;
import com.monee.graphql.DataFetcher.account.AllAccountDataFetcher;
import com.monee.graphql.DataFetcher.account.CreateAccountDataFetcher;
import com.monee.graphql.DataFetcher.account.UpdateAccountDataFetcher;
import com.monee.graphql.DataFetcher.post.AllPostDataFetcher;
import com.monee.graphql.DataFetcher.post.CreatePostDataFetcher;
import com.monee.graphql.DataFetcher.post.DeletePostDataFetcher;
import com.monee.graphql.DataFetcher.post.LikePostDataFetcher;
import com.monee.graphql.DataFetcher.post.PostDataFetcher;
import com.monee.graphql.DataFetcher.post.UpdatePostDataFetcher;
import com.monee.graphql.DataFetcher.reply.AllReplyDataFetcher;
import com.monee.graphql.DataFetcher.reply.CreateReplyDataFetcher;
import com.monee.graphql.DataFetcher.reply.DeleteReplyDataFetcher;
import com.monee.graphql.DataFetcher.reply.ReplyDataFetcher;
import com.monee.graphql.DataFetcher.reply.UpdateReplyDataFetcher;
import com.monee.model.Account;
import com.monee.service.AccountService;
import com.monee.service.PostService;
import com.monee.service.ReplyService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import graphql.ExecutionResult;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
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

    public RestControllerHandler(){

    }
    public RestControllerHandler(AccountService accountService){
        this.accountService = accountService;
    }

    @SneakyThrows
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("handle======================");

        AccountDao accountDao = new AccountDao();
        AccountDataFetcher accountDataFetcher = new AccountDataFetcher(accountDao);
        AllAccountDataFetcher allAccountDataFetcher = new AllAccountDataFetcher(accountDao);
        AccountService accountService = new AccountService(accountDao);
        CreateAccountDataFetcher createAccountDataFetcher = new CreateAccountDataFetcher(accountService);
        UpdateAccountDataFetcher updateAccountDataFetcher = new UpdateAccountDataFetcher(accountService);

        PostDao postDao = new PostDao();
        LikeDao likeDao = new LikeDao();
        PostService postService = new PostService(postDao,likeDao);
        PostDataFetcher postDataFetcher = new PostDataFetcher(postService);
        AllPostDataFetcher allPostDataFetcher = new AllPostDataFetcher(postService);
        CreatePostDataFetcher createPostDataFetcher = new CreatePostDataFetcher(postService);
        UpdatePostDataFetcher updatePostDataFetcher = new UpdatePostDataFetcher(postService);
        DeletePostDataFetcher deletePostDataFetcher = new DeletePostDataFetcher(postService);
        LikePostDataFetcher likePostDataFetcher = new LikePostDataFetcher(postService);

        ReplyDao replyDao = new ReplyDao(accountDao, postDao);
        ReplyService replyService = new ReplyService(replyDao);
        AllReplyDataFetcher allReplyDataFetcher = new AllReplyDataFetcher(replyService);
        CreateReplyDataFetcher createReplyDataFetcher = new CreateReplyDataFetcher(replyService);
        DeleteReplyDataFetcher deleteReplyDataFetcher = new DeleteReplyDataFetcher(replyService);
        ReplyDataFetcher replyDataFetcher = new ReplyDataFetcher(replyService);
        UpdateReplyDataFetcher updateReplyDataFetcher = new UpdateReplyDataFetcher(replyService);

        AccountServiceGraphQLProvider accountServiceGraphQLProvider = new AccountServiceGraphQLProvider(accountDao, allAccountDataFetcher, accountDataFetcher, createAccountDataFetcher, updateAccountDataFetcher,
                postDataFetcher, allPostDataFetcher, createPostDataFetcher, updatePostDataFetcher, deletePostDataFetcher
                , allReplyDataFetcher, createReplyDataFetcher, deleteReplyDataFetcher, replyDataFetcher, updateReplyDataFetcher,likePostDataFetcher);
        accountServiceGraphQLProvider.loadSchema();

        Gson gson = new Gson();

        System.out.println("request header : " + exchange.getRequestHeaders().keySet());
        System.out.println("request header Content-type : " + exchange.getRequestHeaders().get("Content-type"));
        System.out.println("request method : " + exchange.getRequestMethod());

        int status = 200;

        if("POST".equals(exchange.getRequestMethod())){

            System.out.println("===============================");

            Headers responseHeaders = exchange.getResponseHeaders();

            responseHeaders.set("Content-Type", "application/json");

            URI uri = exchange.getRequestURI();

            System.out.println("uri path : " + uri.getPath());

            Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());

            System.out.println("params : "+ params);

            String respText = null;

            if(uri.getPath().equals("/api/test")){

                String noNameText = "Anonymous";

                String name = params.getOrDefault("name", List.of(noNameText)).stream().findFirst().orElse(noNameText);

                System.out.println("name : " + name);

                respText = format("Hello %s!", name);
            }

            if(uri.getPath().equals("/api/reply")){
                BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

                int b = 0;

                if (exchange.getRequestHeaders().get("Content-type").contains("application/json")){

                    System.out.println("=======================================json reqeust");

                    String str = "";
                    str += br.readLine().trim();

                    while((b = br.read()) != -1) {
                        try {
                            str += br.readLine().trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                            str += "}";
                        }

                        System.out.println(str);
                    }

                    System.out.println("request body : "+str);
                    Map<String,String> map = gson.fromJson(str,HashMap.class);
                    String query = map.get("query");

                    ExecutionResult execute = accountServiceGraphQLProvider.execute(query);
                    System.out.println("execute : "+execute.getData().toString());

                    //AccountDto dto = gson.fromJson(str, AccountDto.class);

                    //Account account = accountService.signup(dto);

                    respText = gson.toJson(execute.getData().toString());

                }
            }

            if(uri.getPath().equals("/api/post")){
                BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

                int b = 0;

                if (exchange.getRequestHeaders().get("Content-type").contains("application/json")){

                    System.out.println("=======================================json reqeust");

                    String str = "";
                    str += br.readLine().trim();

                    while((b = br.read()) != -1) {
                        try {
                            str += br.readLine().trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                            str += "}";
                        }

                        System.out.println(str);
                    }

                    System.out.println("request body : "+str);
                    Map<String,String> map = gson.fromJson(str,HashMap.class);
                    String query = map.get("query");

                    ExecutionResult execute = accountServiceGraphQLProvider.execute(query);
                    System.out.println("execute : "+execute.getData().toString());

                    //AccountDto dto = gson.fromJson(str, AccountDto.class);

                    //Account account = accountService.signup(dto);

                    respText = gson.toJson(execute.getData().toString());

                }
            }

            if(uri.getPath().equals("/api/account")){
                BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

                int b = 0;

                if (exchange.getRequestHeaders().get("Content-type").contains("application/json")){

                    System.out.println("=======================================json reqeust");

                    String str = "";
                    str += br.readLine().trim();

                    while((b = br.read()) != -1) {
                        try {
                            str += br.readLine().trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                            str += "}";
                        }

                        System.out.println(str);
                    }

                    System.out.println("request body : "+str);
                    Map<String,String> map = gson.fromJson(str,HashMap.class);
                    String query = map.get("query");

                    ExecutionResult execute = accountServiceGraphQLProvider.execute(query);
                    System.out.println("execute : "+execute.getData().toString());

                    //AccountDto dto = gson.fromJson(str, AccountDto.class);

                    //Account account = accountService.signup(dto);

                    respText = gson.toJson(execute.getData().toString());

                }
            }

            if(uri.getPath().equals("/api/account/login")){
                // TODO 인증문제
            }

            if(uri.getPath().equals("/api/account/logout")){
                // TODO 인증문제
            }

            if(uri.getPath().equals("/api/account/signup")){
                BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

                int b = 0;

                if (exchange.getRequestHeaders().get("Content-type").contains("application/json")){

                    System.out.println("=======================================json reqeust");

                    String str = "";
                    str += br.readLine().trim();

                    while((b = br.read()) != -1) {
                        try {
                            str += br.readLine().trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                            str += "}";
                        }

                        System.out.println(str);
                    }

                    System.out.println("request body : "+str);

                    AccountDto dto = gson.fromJson(str, AccountDto.class);

                    Account account = accountService.signup(dto);

                    respText = gson.toJson(account);

                }

                if(exchange.getRequestHeaders().get("Content-type").contains("application/x-www-form-urlencoded")){

                    System.out.println("=======================================form reqeust");

                    String str = "";

                    do {
                            str += br.readLine().trim();
                        } while((b = br.read()) != -1);

                    System.out.println("str : " + str);

                    Map<String, List<String>> form = splitQuery(str);

                    System.out.println("form data : " + form);

                    String email = form.get("email").get(0);
                    String nickname = form.get("nickname").get(0);
                    String password = form.get("password").get(0);

                    Set<Account.Role> roles = new HashSet<>();

                    roles.add(Account.Role.USER);

                    AccountDto dto = new AccountDto();
                    dto.setEmail(email);
                    dto.setNickname(nickname);
                    dto.setPassword(password);

                    System.out.println("account dto : " + dto);

                    Account account = accountService.signup(dto);

                    System.out.println(account);

                    respText = gson.toJson(account);

                    System.out.println("respText : " + respText);

                    String newUrl = "http://localhost:8080/";

                    exchange.getResponseHeaders().add("Location",newUrl);
                    status = 301;

                }

                OutputStream responseBody = exchange.getResponseBody();
                OutputStreamWriter ow = new OutputStreamWriter(responseBody,"UTF-8");
                BufferedWriter bw = new BufferedWriter(ow);

            }

            if(uri.getPath().equals("/api/post/new")){
                // TODO 인증문제
                // TODO GraphQL
            }
            if(uri.getPath().equals("/api/post/update")){
                // TODO 인증문제
                // TODO GraphQL
            }
            if(uri.getPath().equals("/api/post/delete")){
                // TODO 인증문제
                // TODO GraphQL
            }
            if(uri.getPath().equals("/api/reply/new")){
                // TODO 인증문제
                // TODO GraphQL
            }
            if(uri.getPath().equals("/api/reply/update")){
                // TODO 인증문제
                // TODO GraphQL
            }
            if(uri.getPath().equals("/api/reply/delete")){
                // TODO 인증문제
                // TODO GraphQL
            }

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
