package org.vivi.framework.securityjwt.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    private static final String SECRET = "demoandtestyyyymmmdddsssoooaaaeenwnsbdjfkvmdjdcnsksmcjdjvndjcmsnhsbcjdnsmcjsnhfklbmicjsia";

    private static final long EXPIRE = 60 * 24 * 7;

    public static final String HEADER = "Authorization";

    /**
     * 生成jwt token
     */
    public String generateToken(String username) {
        SecretKey signingKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        //过期时间
        LocalDateTime tokenExpirationTime = LocalDateTime.now().plusMinutes(EXPIRE);
        return Jwts.builder()
                .signWith(signingKey, Jwts.SIG.HS512)
                .header().add("typ", "JWT").and()
                .issuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .subject(username)
                .expiration(Timestamp.valueOf(tokenExpirationTime))
                .claims(Map.of("username", username))
                .compact();
    }

    public Claims getClaimsByToken(String token) {
        SecretKey signingKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (ExpiredJwtException e){
            claims = e.getClaims();
        }
        return claims;
    }

//    public Claims getClaimsByToken(String token) {
//        SecretKey signingKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
//
//        return Jwts.parser()
//                .verifyWith(signingKey)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }

    public Integer getUsernameFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return Integer.valueOf(jwt.getSubject());
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 检查token是否过期
     *
     * @return true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    /**
     * 获得token中的自定义信息,一般是获取token的username，无需secret解密也能获得
     * @param token
     * @param filed
     * @return
     */
    public String getClaimFiled(String token, String filed){
        try{
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(filed).asString();
        } catch (JWTDecodeException e){
            log.error("JwtUtil getClaimFiled error: ", e);
            return null;
        }
    }

    /*
     * 刷新token
     * */

    public String refreshToken(String token,Long refreshTime) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            String username = jwt.getSubject();
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + refreshTime);

            return JWT.create()
                    .withSubject(username)
                    .withIssuedAt(now)
                    .withExpiresAt(expiryDate)
                    .sign(algorithm);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        JwtTokenProvider jwtUtil = new JwtTokenProvider();
        String token = jwtUtil.generateToken("admin");
        System.out.println("token = " + token);

        Claims claims = jwtUtil.getClaimsByToken(token);
        System.out.println("claims = " + claims);

        String username = jwtUtil.getClaimFiled(token, "username");
        System.out.println("username = " + username);
    }
}
