package com.monee;

import com.monee.Filter.LoginFilter;
import com.monee.controller.handler.CustomHttpHandler;
import com.monee.trash.ControllerHandler;
import com.monee.trash.RestControllerHandler;
import com.monee.pool.ObjectPool;
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

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        ObjectPool pool = ObjectPool.getInstance();

        LoginFilter filter = pool.getLoginFilter();
        ControllerHandler controllerHandler = pool.getControllerHandler();
        CustomHttpHandler customHttpHandler = pool.getCustomHttpHandler();

        server.createContext("/", controllerHandler);
        server.createContext("/login", controllerHandler);
        server.createContext("/signup", controllerHandler);
        server.createContext("/filter/test", controllerHandler).getFilters().add(filter);
        server.createContext("/api/account/login",customHttpHandler);
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
        log.info("thread run");
        try {
            serverStart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
