package br.com.springbootapi.security.utils;

import br.com.springbootapi.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenUtils {

    private static final String SECRET = "secretkey";
    private static List<String> unvalidTokens = new ArrayList<>();

    public static String generateToken(Usuario usuario) {
        String token = Jwts.builder()
                .claim("id", usuario.getId())
                .claim("username", usuario.getEmail())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        unvalidTokens.remove(token);

        return token;
    }

    public static String getUsernameFromToken(String token) {
        Claims claim = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();

        return claim.get("username").toString();
    }

    public static int getIdFromToken(String token) {
        Claims claim = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();

        return (int) claim.get("id");
    }

    public static void unvalidateToken(String token) {
        unvalidTokens.add(token);
    }

    public static boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);

            return !unvalidTokens.contains(token);
        } catch (Exception e) {
            return false;
        }
    }
}