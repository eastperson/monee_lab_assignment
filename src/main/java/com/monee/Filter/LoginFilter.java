package com.monee.Filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.monee.dao.AccountDao;
import com.monee.model.Account;
import com.monee.security.Jwt;
import com.monee.service.AccountService;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class LoginFilter extends Filter {
    @SneakyThrows
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        URI uri = exchange.getRequestURI();
        int status = 200;
        String respText = "success";
        int length = -1;
        System.out.println("filter.............................");

        if(uri.getPath().equals("/filter/test") || uri.getPath().equals("/api/account") || uri.getPath().equals("/api/post") || uri.getPath().equals("/api/reply")){
            System.out.println("filter test.............................");
            String token = null;
            try{
                token = exchange.getRequestHeaders().get("Authorization").get(0);
                System.out.println("Bearer with token : " + token);
                token = token.split(" ")[1];
                System.out.println("token : " + token);
            }catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("authorization null");
            }

            String clientSecret = "clientSecret";
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(clientSecret))
                    .withIssuer("issuer")
                    .build();
            Jwt.Claims claims = new Jwt.Claims(jwtVerifier.verify(token));
            String email = claims.getEmail();
            String password = claims.getPassword();
            System.out.println("filter email : " + email);
            System.out.println("filter password : " + password);

            AccountDao accountDao = new AccountDao();
            AccountService accountService = new AccountService(accountDao);

            Account account = accountService.findByEmail(email).orElseThrow(NullPointerException::new);

            System.out.println(account);

            if(account == null) {

                System.out.println("login fail ................");
                String newUrl = "http://localhost:8080/";
                status = 405;
                exchange.getResponseHeaders().add("Location",newUrl);
                length = respText.getBytes().length;
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                exchange.sendResponseHeaders(status, length);
                OutputStream output = exchange.getResponseBody();

                output.write(respText.getBytes());
                output.flush();
                exchange.close();
            } else {
                System.out.println("login pass..................");

            }
        }
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return null;
    }
}
