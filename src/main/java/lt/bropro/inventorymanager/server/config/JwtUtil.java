package lt.bropro.inventorymanager.server.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey"; // Use a strong secret key

    public String generateToken(String email) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY);
        return builder.compact();
    }

    public boolean validateToken(String token, String email) {
        final String extractedEmail = extractUsername(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}

