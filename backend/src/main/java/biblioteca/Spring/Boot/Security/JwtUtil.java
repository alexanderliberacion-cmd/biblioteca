package biblioteca.Spring.Boot.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {


    //Generar jwt token
    @Value("${JWT_SECRET}")
     private String SECRET_STRING; //crea la clave secreta para la jwt


    private SecretKey key; //Clave secreta codificada en hs256

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    }

    //Generamos el token construyendo el username, la fecha de creacion y de expiracion y firmandolo con la clave.
     public  String generateToken(String username){
         int fechaExpiracion3Horas = 1000 * 60 * 60 * 3;
         Date now = new Date();
         Date expiration = new Date(now.getTime() + fechaExpiracion3Horas); //tiempo de expiracion
         return Jwts.builder()
                 .subject(username)
                 .issuedAt(now)
                 .expiration(expiration)
                 .signWith(key)
                 .compact(); //Esto hace que sea mas compacta la clave.
     }
    //Extrae el subject del jwt
    public  String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }



    //Verificacion del token
    public  boolean validateToken(String token, UserDetails userDetails) {
         try{
             return extractUsername(token).equals(userDetails.getUsername());
         } catch(Exception ex) {
             return false;
         }
    }

}

