package com.monee.annotation.controller;

import com.monee.errors.NotControllerException;
import com.monee.pool.ObjectPool;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Locale;
import java.util.Objects;

public class ControllerHandler {

    private static Logger log = LoggerFactory.getLogger(ControllerHandler.class);
    private String path;

    private static boolean checkIsMappingAble(HttpExchange exchange,Object controller) {

        URI uri = exchange.getRequestURI();
        String requestMethod = exchange.getRequestMethod();

        if(Objects.isNull(controller)){
            try{
                throw new NullPointerException("Controller Mapping Error");
            } catch (NullPointerException ne) {
                ne.printStackTrace();
                log.error(ne.getMessage());
                return false;
            }
        }

        Class<?> clazz = controller.getClass();
        if(!clazz.isAnnotationPresent(Controller.class) && !clazz.isAnnotationPresent(RestController.class)){
            try{
                throw new NotControllerException("not controller type mapping. required controller annotation");
            } catch (NotControllerException ne) {
                ne.printStackTrace();
                log.error(ne.getMessage());
                return false;
            }
        }

        for(Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ControllerMapping.class)) {
                ControllerMapping controllerMapping = method.getAnnotation(ControllerMapping.class);
                if (uri.getPath().equals(controllerMapping.value()) && requestMethod.equalsIgnoreCase(controllerMapping.method())) {
                    return true;
                }
            }
        }

        return false;
    }

    private static void urlMapping(HttpExchange exchange, Object object) {
        Class<?> clazz = object.getClass();

        ObjectPool pool = ObjectPool.getInstance();
        URI uri = exchange.getRequestURI();
        String requestMethod = exchange.getRequestMethod();
        Headers headers = exchange.getRequestHeaders();

        for(Method method : clazz.getDeclaredMethods()){
            if(clazz.isAnnotationPresent(Controller.class) && method.isAnnotationPresent(ControllerMapping.class)){
                    log.debug("method name : "+method.getName());
                    log.debug("controller");
                    method.setAccessible(true);

                    ControllerMapping controllerMapping = method.getAnnotation(ControllerMapping.class);
                    String mappingPath = controllerMapping.value();
                    String controllerMethod = controllerMapping.method().toUpperCase(Locale.ROOT);
                    if(uri.getPath().equals(mappingPath) && requestMethod.equalsIgnoreCase(controllerMethod)){
                        log.info("controller type : controller mapping complete");
                        log.info("mappingPath : " + mappingPath);
                        log.info("request method : " + requestMethod);
                        log.info("method invoke");

                        try {
                            method.invoke(object);
                        } catch (IllegalAccessException | InvocationTargetException  e) {
                            e.printStackTrace();
                            log.error(e.getMessage());
                        }
                    }
            }

            if(clazz.isAnnotationPresent(RestController.class) && method.isAnnotationPresent(ControllerMapping.class)){
                log.debug("method name : "+method.getName());
                log.debug("controller");
                method.setAccessible(true);

                ControllerMapping controllerMapping = method.getAnnotation(ControllerMapping.class);
                String mappingPath = controllerMapping.value();
                String controllerMethod = controllerMapping.method().toUpperCase(Locale.ROOT);
                if(uri.getPath().equals(mappingPath) && requestMethod.equalsIgnoreCase(controllerMethod)){
                    log.info("controller type : rest controller mapping complete");
                    log.info("mappingPath : " + mappingPath);
                    log.info("request method : " + requestMethod);
                    log.info("method invoke");

                    try {
                        method.invoke(object);
                    } catch (IllegalAccessException | InvocationTargetException  e) {
                        e.printStackTrace();
                        log.error(e.getMessage());
                    }
                }
            }
        }
    }

    public static void handle(HttpExchange exchange, Object controller) {

        URI uri = exchange.getRequestURI();
        log.info("request uri : " + uri);

        if(!checkIsMappingAble(exchange,controller)) return;
        log.info("request uri mapping complete");

        urlMapping(exchange,controller);

    }
}
