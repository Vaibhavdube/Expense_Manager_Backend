package in.vaibhav.moneymanager.util;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    // secret key
    private static final String SECRET_KEY =
            "mysecretkeymysecretkeymysecretkey123456";

    // 1 day
    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24;

    // generate key
    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes()
        );
    }

    // generate jwt token
    public String generateToken(String email) {

        return Jwts
                .builder()

                // payload data
                .setSubject(email)

                // token creation time
                .setIssuedAt(new Date())

                // token expiry
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION_TIME
                        )
                )

                // sign token
                .signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    // extract email from token
    public String extractEmail(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // validate token
    public boolean validateToken(
            String token,
            String email
    ) {

        String extractedEmail =
                extractEmail(token);

        return extractedEmail.equals(email)
                && !isTokenExpired(token);
    }

    // check token expiry
    private boolean isTokenExpired(
            String token
    ) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // extract all claims
    private Claims extractAllClaims(
            String token
    ) {

        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

