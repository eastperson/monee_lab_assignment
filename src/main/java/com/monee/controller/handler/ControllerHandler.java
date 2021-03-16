package com.monee.controller.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.net.URLDecoder;

import java.util.regex.Pattern;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class ControllerHandler implements HttpHandler {

    private String root = "C:\\Users\\kjuio\\IdeaProjects\\monee_lab_assignment\\src\\main\\resources\\templates";


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        Headers headers = exchange.getRequestHeaders();

        if (requestMethod.equalsIgnoreCase("GET")){

            System.out.println("===============================");

            Headers responseHeaders = exchange.getResponseHeaders();

            responseHeaders.set("Content-Type", "text/html");

            URI uri = exchange.getRequestURI();

            System.out.println("uri path : " + uri.getPath());
            System.out.println("uri == / " + uri.getPath().equals("/"));

            Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());

            System.out.println("params : "+ params);

            String noNameText = "Anonymous";

            String name = params.getOrDefault("name", List.of(noNameText)).stream().findFirst().orElse(noNameText);

            System.out.println("name : " + name);

            String respText = String.format("Hello %s!", name);

            OutputStream responseBody = exchange.getResponseBody();

            OutputStreamWriter ow = new OutputStreamWriter(responseBody,"UTF-8");
            BufferedWriter bw = new BufferedWriter(ow);

            BufferedReader br = null;

            if(uri.getPath().equals("/")){
                br = new BufferedReader(new InputStreamReader(new FileInputStream(root + "/index.html"),"UTF-8"));
                System.out.println("return index.html");
            } else {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(root + uri.getPath()),"UTF-8"));
            }

            exchange.sendResponseHeaders(200, 0);

            int b = 0;

            while((b = br.read()) != -1){
                bw.write(b);
            }
            bw.flush();

            exchange.close();
            bw.close();
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
