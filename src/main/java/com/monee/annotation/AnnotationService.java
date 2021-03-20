package com.monee.annotation;

public class AnnotationService {

    @PrintAnnotation
    public void method1(){
        System.out.println("실행 1");
    }

    @PrintAnnotation("*")
    public void method2(){
        System.out.println("실행 2");
    }

    @PrintAnnotation(value = "#",number = 20)
    public void method3(){
        System.out.println("실행 3");
    }
}
