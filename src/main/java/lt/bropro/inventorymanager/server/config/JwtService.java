package lt.bropro.inventorymanager.server.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class JwtService {

    private static final String SECRET_KEY = "L9I578MnFcAZaCBGueDiMUAaYJU64JWmKeUICFUnXXnt/064eZMqZTPkdl40XN26QAVV+Rbb6O3Bd6U2es7X5tODfqhCjRlLgbx+ZN30vd7Gkv8958diHfeIfBlSQpjK9MnMAwWzKzX/k5EDZMdbzTFZRgS8wKlb5564kV5pq0zYpFoQ40kK+nMc/9o+RN5TcQfsRneVEjeteepEVPez70IK3/41ENqwluv/xJk3EFei8B86CrbzevYMQupFkGhzPFDm0ZlKEmKTq5S6djunXl1L7sGJzKVQ6GiZibHb4j/acsPfm+ZLGrLv08NCGT62QIn3V1m8CttHeqnirTGyudApF05JkpkPgw2sZ/ACQ1Q=\n";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        String token = Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
