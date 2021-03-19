package com.monee.utill;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.monee.graphql.PostGraphqlTest;
import com.monee.model.Account;
import com.monee.security.Jwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtTest {

    private static Logger log = LoggerFactory.getLogger(JwtTest.class);

    @DisplayName("jwt 생성 test")
    @Test
    void jwtTest(){

        String issuer = "issuer";
        String clientSecret = "moneelab";
        int expirySeconds = 60*60*7;

        Jwt jwt = new Jwt(issuer,clientSecret,expirySeconds);

        log.info("jwt issuer : "+jwt.getIssuer());
        log.info("jwt expiry : " + jwt.getExpirySeconds());
        log.info("jwt client secret : " + jwt.getClientSecret());
        log.info("jwt algorithm : " + jwt.getAlgorithm().getName());
        log.info("jwt verifier : " + jwt.getJwtVerifier());

    }

    @DisplayName("jwt 생성 test")
    @Test
    void jwt_create(){

        String issuer = "issuer";
        String clientSecret = "moneelab";
        int expirySeconds = 60*60*7;

        Jwt jwt = new Jwt(issuer,clientSecret,expirySeconds);
        Account account = new Account("email","nickname","password");
        account.setSeq(1L);
        String token = account.newJwt(jwt);
        log.info("account : "+account.toString());
        log.info("token "+token);

    }

    @DisplayName("jwt verify test")
    @Test
    void jwt_verfiy(){

        String issuer = "issuer";
        String clientSecret = "clientSecret";
        int expirySeconds = 60*60*24*30;

        Jwt jwt = new Jwt(issuer,clientSecret,expirySeconds);
        Account account = new Account("kjuioqqq@naver.com","eastperson","123123");
        account.setSeq(1L);
        String token = account.newJwt(jwt);
        log.info("account : "+account.toString());
        log.info("token "+token);
        log.info(jwt.verify(token).toString());

    }
}
