package com.monee.annotation.controller;

@Controller
public class SampleController {

    @ControllerMapping(value = "/api/sample",method = "GET",responseType = "application/json")
    public void sample(){
        System.out.println("controller");
    }
}
