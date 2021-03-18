package com.monee.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.monee.model.Account;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Date;

public final class Jwt {

    private final String issuer;

    private final String clientSecret;

    private final int expirySeconds;

    private final Algorithm algorithm;

    private final JWTVerifier jwtVerifier;

    public Jwt(String issuer, String clientSecret, int expirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    public String create(Claims claims) {
        Date now = new Date();
        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        if (expirySeconds > 0) {
            builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L));
        }
        builder.withClaim("userKey", claims.userKey);
        builder.withClaim("email", claims.email);
        builder.withClaim("nickname", claims.nickname);
        return builder.sign(algorithm);
    }

    public Claims verify(String token) throws JWTVerificationException {
        return new Claims(jwtVerifier.verify(token));
    }

    public String getIssuer() {
        return issuer;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public int getExpirySeconds() {
        return expirySeconds;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public JWTVerifier getJwtVerifier() {
        return jwtVerifier;
    }

    static public class Claims {
        public Long getUserKey() {
            return userKey;
        }

        public String getEmail() {
            return email;
        }

        public String getNickname() {
            return nickname;
        }

        public String getPassword() {
            return password;
        }

        public Date getIat() {
            return iat;
        }

        public Date getExp() {
            return exp;
        }

        Long userKey;
        String email;
        String nickname;
        String password;
        Date iat;
        Date exp;

        private Claims() {/*empty*/}

        public Claims(DecodedJWT decodedJWT) {
            Claim userKey = decodedJWT.getClaim("userKey");
            if (!userKey.isNull()) {
                this.userKey = userKey.asLong();
            }
            Claim email = decodedJWT.getClaim("email");
            if (!email.isNull()) {
                this.email = email.asString();
            }
            Claim nickname = decodedJWT.getClaim("nickname");
            if (!nickname.isNull()) {
                this.nickname = nickname.asString();
            }
            this.iat = decodedJWT.getIssuedAt();
            this.exp = decodedJWT.getExpiresAt();
        }

        public static Claims of(long userKey, Account account) {
            Claims claims = new Claims();
            claims.userKey = userKey;
            claims.email = account.getEmail();
            claims.nickname = account.getNickname();
            return claims;
        }

        long iat() {
            return iat != null ? iat.getTime() : -1;
        }

        long exp() {
            return exp != null ? exp.getTime() : -1;
        }

        void eraseIat() {
            iat = null;
        }

        void eraseExp() {
            exp = null;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("userKey", userKey)
                    .append("email", email)
                    .append("nickname",nickname)
                    .append("iat", iat)
                    .append("exp", exp)
                    .toString();
        }
    }

    static public class Builder {
        private String issuer;
        private String clientSecret;
        private int expirySeconds;
        private Algorithm algorithm;
        private JWTVerifier jwtVerifier;

        public Builder() {/*empty*/}

        public Builder(Jwt jwt) {
            this.algorithm = algorithm;
            this.clientSecret = clientSecret;
            this.expirySeconds = expirySeconds;
            this.jwtVerifier = jwtVerifier;
            this.issuer = issuer;
        }

        public Jwt.Builder issuer(String issuer){
            this.issuer = issuer;
            return this;
        }

        public Jwt.Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Jwt.Builder expirySeconds(int expirySeconds){
            this.expirySeconds = expirySeconds;
            return this;
        }


        public Jwt build(){
            return new Jwt(issuer,clientSecret,expirySeconds);
        }
    }

}