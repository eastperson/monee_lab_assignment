package com.monee;

import com.monee.Filter.LoginFilter;
import com.monee.controller.handler.ControllerHandler;
import com.monee.controller.handler.RestControllerHandler;
import com.monee.controller.resolver.ControllerResolver;
import com.monee.dao.AccountDao;
import com.monee.graphql.AccountServiceGraphQLProvider;
import com.monee.graphql.DataFetcher.AccountDataFetcher;
import com.monee.graphql.DataFetcher.AllAccountDataFetcher;
import com.monee.model.Account;
import com.monee.service.AccountService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;

public class CoreApplication {
    public static void main(String[] args) throws IOException, NoSuchMethodException {

        int serverPort = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        AccountDao accountDao = new AccountDao();
        AccountService accountService = new AccountService(accountDao);

        LoginFilter filter = new LoginFilter();

        server.createContext("/", new ControllerHandler());
        server.createContext("/login", new ControllerHandler());
        server.createContext("/signup", new ControllerHandler());
        server.createContext("/filter/test", new ControllerHandler()).getFilters().add(filter);
        server.createContext("/api/account/login",new RestControllerHandler(accountService));
        server.createContext("/api/account/signup",new RestControllerHandler(accountService));
        server.createContext("/api/account/logout",new RestControllerHandler(accountService));
        server.createContext("/api/account/view",new RestControllerHandler(accountService));
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("server starting..................");

    }
}
