package com.monee.annotation.controller;

@RestController
public class SampleController2 {

    @ControllerMapping(value = "/api/sample2",method = "POST")
    public void sample(){
        System.out.println("rest controller");
    }

}
