package com.monee.annotation.controller;

import com.monee.controller.AccountApiController;
import com.monee.controller.PostApiController;
import com.monee.controller.ReplyApiController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.eclipse.jetty.http.HttpParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CustomHttpHandler implements HttpHandler {

    private static Logger log = LoggerFactory.getLogger(CustomHttpHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        log.info("custom handler============================================");

        Object[] controllerPool = new Object[]{
                new AccountApiController(),
                new PostApiController(),
                new ReplyApiController()
        };

        ControllerHandler controllerHandler = new ControllerHandler();
        controllerHandler.handle(exchange,controllerPool);
    }
}
