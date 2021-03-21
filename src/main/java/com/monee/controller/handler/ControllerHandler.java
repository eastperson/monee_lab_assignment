package com.monee.controller.handler;

import com.google.gson.Gson;
import com.monee.controller.annotation.ApiController;
import com.monee.controller.annotation.Controller;
import com.monee.controller.annotation.ControllerMapping;
import com.monee.errors.NotControllerException;
import com.monee.pool.ObjectPool;
import com.monee.utils.ResponseEntity;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class ControllerHandler {

    private static Logger log = LoggerFactory.getLogger(ControllerHandler.class);
    private String path;

    public void handle(HttpExchange exchange, Object[] controllerPool) throws IOException {

        log.info("controller handler");

        URI uri = exchange.getRequestURI();
        log.info("request uri : " + uri);

        for(Object controller : controllerPool){
            if(!checkIsMappingAble(exchange,controller)){
                log.info("resquest is not mapping");
                continue;
            }
            log.info("request uri mapping complete");

            urlMapping(exchange,controller);
        }
        exchange.close();

    }

    private static boolean checkIsMappingAble(HttpExchange exchange,Object controller) {

        log.info("check is mapping uri");

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
        log.info("controller is not null");

        Class<?> clazz = controller.getClass();
        if(!clazz.isAnnotationPresent(Controller.class) && !clazz.isAnnotationPresent(ApiController.class)){
            try{
                throw new NotControllerException("not controller type mapping. required controller annotation");
            } catch (NotControllerException ne) {
                ne.printStackTrace();
                log.error(ne.getMessage());
                return false;
            }
        }
        log.info("controller is controller class");

        for(Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ControllerMapping.class)) {
                ControllerMapping controllerMapping = method.getAnnotation(ControllerMapping.class);
                if (uri.getPath().equals(controllerMapping.value()) && requestMethod.equalsIgnoreCase(controllerMapping.method())) {
                    return true;
                }
            }
        }
        log.info("controller's method is not mappable");

        return false;
    }

    private static void urlMapping(HttpExchange exchange, Object object) throws IOException {
        Class<?> clazz = object.getClass();

        URI uri = exchange.getRequestURI();
        String requestMethod = exchange.getRequestMethod();
        Headers headers = exchange.getRequestHeaders();
        List<String> list = headers.get("Content-type");
        log.info("request header : " + exchange.getRequestHeaders().keySet());
        log.info("request header Content-type : " + exchange.getRequestHeaders().get("Content-type"));
        log.info("request method : " + exchange.getRequestMethod());

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

            if(clazz.isAnnotationPresent(ApiController.class) && method.isAnnotationPresent(ControllerMapping.class)){
                log.debug("method name : "+method.getName());
                log.debug("api controller");
                log.info("api controller dsa");
                method.setAccessible(true);

                ControllerMapping controllerMapping = method.getAnnotation(ControllerMapping.class);
                String mappingPath = controllerMapping.value();
                String controllerMethod = controllerMapping.method().toUpperCase();
                String responseType = controllerMapping.responseType();

                if(uri.getPath().equals(mappingPath) && requestMethod.equalsIgnoreCase(controllerMethod)){

                    ObjectPool pool = ObjectPool.getInstance();
                    ResponseEntity result = new ResponseEntity();
                    log.info("controller type : rest controller mapping complete");
                    log.info("mappingPath : " + mappingPath);
                    log.info("request method : " + requestMethod);

                    log.info("handle");

                    Gson gson = ObjectPool.getInstance().getGson();
                    int status = 200;

                    // 리스폰스 타입 설정
                    Headers responseHeaders = exchange.getResponseHeaders();
                    responseHeaders.set("Content-Type", responseType);
                    String respText = null;
                    log.info("response content type : " + responseType);

                    // 리퀘스트 param 설정
                    Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());
                    BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
                    int b = 0;
                    if(exchange.getRequestHeaders().get("Content-type").contains("application/json")){
                        log.info("json reqeust");

                        String str = "";
                        str += br.readLine().trim();

                        while((b = br.read()) != -1) {
                            try {
                                str += br.readLine().trim();
                            } catch (Exception e) {
                                e.printStackTrace();
                                log.info(e.getMessage());
                                str += "}";
                            }

                            log.info(str);
                        }
                        log.info("request body : "+str);
                        Map<String,Object> payload = gson.fromJson(str, HashMap.class);
                        log.info("method invoke");
                        log.info("method name : "+method.getName());
                        log.info("method annotation : " + method.getAnnotation(ControllerMapping.class));
                        log.info("method parameter types : " + Arrays.toString(method.getParameterTypes()));
                        log.info("method parameters : " + Arrays.toString(method.getParameters()));
                        log.info("method return type : "+method.getReturnType());
                        try {
                            result = (ResponseEntity) method.invoke(clazz.newInstance(),params,payload);
                            respText = (String) result.getResultApi();
                            log.info("resp test : " + respText);
                        } catch (IllegalAccessException | InvocationTargetException | InstantiationException  e) {
                            e.printStackTrace();
                            log.error(e.getMessage());
                            result.setStatus(400);
                            result.setSuccess(false);
                        }
                    } else {

                        log.info("method invoke");
                        try {
                            result =  (ResponseEntity) method.invoke(object,params);
                            respText = (String) result.getResultApi();
                        } catch (IllegalAccessException | InvocationTargetException  e) {
                            e.printStackTrace();
                            log.error(e.getMessage());
                            result.setStatus(400);
                            result.setSuccess(false);
                        }

                    }
                    log.info("exchange response body : " + respText);
                    log.info("response headers : "+exchange.getResponseHeaders() );

                    log.info("status : " + result.getStatus());
                    log.info("success : " + result.isSuccess());

                    exchange.sendResponseHeaders(result.getStatus(),respText.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(respText.getBytes());
                    output.flush();
                    exchange.close();
                }
            }
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
