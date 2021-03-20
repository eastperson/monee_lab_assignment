package com.monee;

import com.monee.Filter.LoginFilter;
import com.monee.annotation.controller.CustomHttpHandler;
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

import static java.lang.Thread.sleep;

public class CoreApplication implements Runnable {
    private static Logger log = LoggerFactory.getLogger(CoreApplication.class);

    public static void main(String[] args) throws IOException, NoSuchMethodException {
        log.info("===================================");
        serverStart();
    }

    private static void serverStart() throws IOException {
        int serverPort = 8080;

        log.info("===================================");

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        log.info("===================================");

        ObjectPool pool = ObjectPool.getInstance();

        log.info("===================================");

        LoginFilter filter = pool.getLoginFilter();
        ControllerHandler controllerHandler = pool.getControllerHandler();
        RestControllerHandler restControllerHandler = pool.getRestControllerHandler();
        CustomHttpHandler customHttpHandler = pool.getCustomHttpHandler();

        server.createContext("/", controllerHandler);
        server.createContext("/login", controllerHandler);
        server.createContext("/signup", controllerHandler);
        server.createContext("/filter/test", controllerHandler).getFilters().add(filter);
        server.createContext("/api/account/login",restControllerHandler);
        server.createContext("/api/account/signup",customHttpHandler);
        server.createContext("/api/account",customHttpHandler).getFilters().add(filter);
        server.createContext("/api/post", customHttpHandler).getFilters().add(filter);
        server.createContext("/api/reply", customHttpHandler).getFilters().add(filter);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        log.info("server starting..................");
    }

    @Override
    public void run() {
        log.info("===================================");
        try {
            serverStart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
