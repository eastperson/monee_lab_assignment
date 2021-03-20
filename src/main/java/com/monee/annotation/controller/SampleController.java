package com.monee.annotation.controller;

@Controller
public class SampleController {

    @ControllerMapping(value = "/api/sample",method = "GET")
    public void sample(){
        System.out.println("controller");
    }
}
