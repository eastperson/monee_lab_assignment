package com.monee;

import com.monee.controller.handler.ControllerHandler;
import com.monee.controller.handler.RestControllerHandler;
import com.monee.controller.resolver.ControllerResolver;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;

public class CoreApplication {
    public static void main(String[] args) throws IOException, NoSuchMethodException {

        int serverPort = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        server.createContext("/", new ControllerHandler());
        server.createContext("/login", new ControllerHandler());
        server.createContext("/signup", new ControllerHandler());
        server.createContext("/api/login",new RestControllerHandler());
        server.createContext("/api/signup",new RestControllerHandler());
        server.createContext("/api/logout",new RestControllerHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("server starting..................");

    }
}
