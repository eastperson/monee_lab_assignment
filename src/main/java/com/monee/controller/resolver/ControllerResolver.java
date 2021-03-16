package com.monee.controller.resolver;

import com.monee.controller.handler.ControllerHandler;
import com.monee.controller.handler.RestControllerHandler;
import javassist.ClassClassPath;
import javassist.bytecode.stackmap.TypeData;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ControllerResolver {

    Map<String, Class<?>> pool = new HashMap<>();

    public ControllerResolver(){

        pool.put("/", ControllerHandler.class);
        pool.put("/login",ControllerHandler.class);
        pool.put("/signup",ControllerHandler.class);

        pool.put("/api/login", RestControllerHandler.class);
        pool.put("/api/signup", RestControllerHandler.class);
        pool.put("/api/logout", RestControllerHandler.class);

    }

    public Map<String, Class<?>> getPool() {
        return pool;
    }
}
