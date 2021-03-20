package com.monee.annotation;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class AnnotationTest {



    @Test
    void test(){

        Method[] declareMethods = AnnotationService.class.getDeclaredMethods();

        for(Method method : declareMethods){
            PrintAnnotation printAnnotation = method.getAnnotation(PrintAnnotation.class);
            System.out.println("[" + method.getName() + "]");
            for(int i=0; i < printAnnotation.number(); i++){
                System.out.println(printAnnotation.value());
            }
            System.out.println();
            try{
                method.invoke(new AnnotationService());
            } catch (Exception e) {}
            System.out.println();
        }
    }

}
