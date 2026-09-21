package biblioteca.Spring.Boot.Security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioDetailsService usuarioDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UsuarioDetailsService user) {
        this.jwtUtil = jwtUtil;
        this.usuarioDetailsService = user;
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    //Filtro de jwt
    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization"); //agarra el header de autorizacion
        //Si es null y no empieza por bearer  hace el filtro y regresa
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7); //Recorta el header
        try {
            String username = jwtUtil.extractUsername(token); //Da el token ya cortado extrayendo el username
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = usuarioDetailsService.loadUserByUsername(username);
                //Si el token y el user estan validados crea una nueva authtoken con el user, credenciales y autoridades y hace un setAuthentication con el.
                if (jwtUtil.validateToken(token,userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }


        //Si no es null y el contexto y autenticacion tampoco carga el usuario por username.
        //Activa el filtro
        filterChain.doFilter(request, response);
    }
}
