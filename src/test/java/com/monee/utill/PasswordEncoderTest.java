package com.monee.utill;



import com.monee.pool.ObjectPool;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.LoggerFactory;

public class PasswordEncoderTest {

    private static Logger log = LoggerFactory.getLogger(PasswordEncoderTest.class);

    @DisplayName("패스워드 인코더 테스트")
    @Test
    void password_encoder(){

        String password = "123123";

        log.info("original : " + password);

        String encodedPassword = BCrypt.hashpw(password,BCrypt.gensalt());

        log.info("encoded passowrd : " + encodedPassword);

        log.info("encoded password, password : "+BCrypt.checkpw(password,encodedPassword));


    }

    @Test
    void test(){
        ObjectPool.getInstance();
    }

}
