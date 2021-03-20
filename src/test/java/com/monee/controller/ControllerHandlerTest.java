package com.monee.controller;

import com.monee.trash.ControllerHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ControllerHandlerTest {

    private static Logger log = LoggerFactory.getLogger(ControllerHandlerTest.class);

    @DisplayName("쿼리 파싱 테스트")
    @Test
    void splitQueryTest(){

        String param = "param=1&param=2&nickname=ep";

        ControllerHandler controllerHandler = new ControllerHandler();
        Map<String, List<String>> params = controllerHandler.splitQuery(param);

        log.info(params.toString());

    }

}
