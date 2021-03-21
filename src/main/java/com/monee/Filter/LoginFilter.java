package com.monee.Filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.monee.dao.AccountDao;
import com.monee.model.Account;
import com.monee.security.Jwt;
import com.monee.service.AccountService;
import com.nimbusds.jose.Header;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class LoginFilter extends Filter {

    private static Logger log = LoggerFactory.getLogger(LoginFilter.class);


    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        URI uri = exchange.getRequestURI();
        int status = 200;
        String respText = "success";
        int length = -1;

        if(uri.getPath().equals("/filter/test") || uri.getPath().equals("/api/account") || uri.getPath().equals("/api/post") || uri.getPath().equals("/api/reply")){

                String token = null;
                try{
                    token = exchange.getRequestHeaders().get("Authorization").get(0);
                    log.info("Bearer with token : " + token);
                    token = token.split(" ")[1];
                    log.info("token : " + token);

                    String clientSecret = "clientSecret";
                    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(clientSecret))
                            .withIssuer("issuer")
                            .build();
                    Jwt.Claims claims = new Jwt.Claims(jwtVerifier.verify(token));
                    String email = claims.getEmail();
                    String password = claims.getPassword();
                    log.info("filter email : " + email);
                    log.info("filter password : " + password);

                    AccountDao accountDao = new AccountDao();
                    AccountService accountService = new AccountService(accountDao);

                    try{
                        Account account = accountService.findByEmail(email).orElseThrow(NullPointerException::new);

                        log.info("account : "+account);

                        //exchange.getRequestHeaders().add("user",account.getEmail());

                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                        log.error("not found account");
                    }
                } catch (NullPointerException e) {
                    log.info("authorization null");
                    String newUrl = "http://localhost:8080/";
                    status = 405;
                    exchange.getResponseHeaders().add("Location", newUrl);
                    length = respText.getBytes().length;
                    exchange.getResponseHeaders().set("Content-Type", "application/json");

                    exchange.sendResponseHeaders(status, length);
                    OutputStream output = exchange.getResponseBody();

                    output.write(respText.getBytes());
                    output.flush();
                    exchange.close();
                }
            } else {
                log.info("login pass..................");
            }
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return null;
    }
}
