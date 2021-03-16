package com.monee.controller.handler;

import com.google.gson.Gson;
import com.monee.model.Account;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class RestControllerHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Gson gson = new Gson();

        System.out.println("request header : " + exchange.getRequestHeaders().keySet());
        System.out.println("request header Content-type : " + exchange.getRequestHeaders().get("Content-type"));
        System.out.println("request method : " + exchange.getRequestMethod());

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

                respText = String.format("Hello %s!", name);
            }

            if(uri.getPath().equals("/api/login")){

            }

            if(uri.getPath().equals("/api/logout")){

            }

            if(uri.getPath().equals("/api/signup")){
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


                    respText = str;

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

                    Account account = new Account(email,nickname,password);

                    System.out.println("account : " + account);

                    respText = gson.toJson(account);

                    System.out.println("respText : " + respText);


                }

                OutputStream responseBody = exchange.getResponseBody();
                OutputStreamWriter ow = new OutputStreamWriter(responseBody,"UTF-8");
                BufferedWriter bw = new BufferedWriter(ow);

            }

            exchange.sendResponseHeaders(200, respText.getBytes().length);

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
