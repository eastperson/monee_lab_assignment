package com.monee.annotation;

import java.lang.reflect.Method;

public class PrintAnnotationExample {
    public static void main(String[] args) {
        Method[] declareMethods = AnnotationService.class.getDeclaredMethods();

        for(Method method : declareMethods){

            if(method.isAnnotationPresent(PrintAnnotation.class)){
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
}
