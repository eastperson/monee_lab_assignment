package com.monee;

import com.monee.Filter.LoginFilter;
import com.monee.controller.handler.ControllerHandler;
import com.monee.controller.handler.RestControllerHandler;
import com.monee.dao.AccountDao;
import com.monee.pool.ObjectPool;
import com.monee.service.AccountService;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class CoreApplication {
    private static Logger log = LoggerFactory.getLogger(CoreApplication.class);
    public static void main(String[] args) throws IOException, NoSuchMethodException {

        int serverPort = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        ObjectPool pool = ObjectPool.getInstance();

        LoginFilter filter = pool.getLoginFilter();
        ControllerHandler controllerHandler = pool.getControllerHandler();
        RestControllerHandler restControllerHandler = pool.getRestControllerHandler();

        server.createContext("/", controllerHandler);
        server.createContext("/login", controllerHandler);
        server.createContext("/signup", controllerHandler);
        server.createContext("/filter/test", controllerHandler).getFilters().add(filter);
        server.createContext("/api/account/login",restControllerHandler);
        server.createContext("/api/account/signup",restControllerHandler);
        server.createContext("/api/account/logout",restControllerHandler);
        server.createContext("/api/account",restControllerHandler).getFilters().add(filter);
        server.createContext("/api/post", restControllerHandler).getFilters().add(filter);
        server.createContext("/api/reply", restControllerHandler).getFilters().add(filter);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        log.info("server starting..................");

    }
}
