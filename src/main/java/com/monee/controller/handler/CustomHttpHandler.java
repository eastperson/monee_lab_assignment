package com.monee.controller.handler;

import com.monee.controller.api.AccountApiController;
import com.monee.controller.api.PostApiController;
import com.monee.controller.api.ReplyApiController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CustomHttpHandler implements HttpHandler {

    private static Logger log = LoggerFactory.getLogger(CustomHttpHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        log.info("custom http handler");

        Object[] controllerPool = new Object[]{
                new AccountApiController(),
                new PostApiController(),
                new ReplyApiController()
        };

        ControllerHandler controllerHandler = new ControllerHandler();
        controllerHandler.handle(exchange,controllerPool);
    }
}
