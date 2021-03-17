package com.monee.Filter;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class LoginFilter extends Filter {
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        URI uri = exchange.getRequestURI();
        int status = 200;
        String respText = "success";
        int length = -1;


        System.out.println("filter.............................");

        if(uri.getPath().equals("/filter/test")){
            System.out.println("filter test.............................");
            String authorization = exchange.getRequestHeaders().get("Authorization").get(0);
            if(authorization.equals("pass")) {

                System.out.println("pass................");
                String newUrl = "http://localhost:8080/";
                status = 301;
                exchange.getResponseHeaders().add("Location",newUrl);
                length = respText.getBytes().length;
            } else {
                System.out.println("fail..................");
                status = 405;
                respText = "fail";
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json");

            exchange.sendResponseHeaders(status, length);
            OutputStream output = exchange.getResponseBody();

            output.write(respText.getBytes());
            output.flush();
            exchange.close();
        }
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return null;
    }
}
