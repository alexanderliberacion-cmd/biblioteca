package biblioteca.Spring.Boot.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtUtil jwtUtil;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, JwtUtil jwtUtil) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtUtil = jwtUtil;
    }

    //Codifica la contraseña
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) //apaga el csrf que no es necesario con los jwts
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .anonymous(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(HttpMethod.GET, "/socio/listar").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/socio/socio").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/socio/actualizar/").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/socio/borrar/").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/socio/usuarios-pendientes").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/socio/activacion/").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/libro/listar").permitAll()
                                .requestMatchers(HttpMethod.POST, "/libro/libro").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/libro/actualizar/").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/libro/borrar/").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/prestamo/listar").hasAnyRole("ADMIN", "SOCIO")
                                .requestMatchers(HttpMethod.GET, "/prestamo/prestamo/libros-mas-prestados", "/prestamo/prestamo/meses-mas-prestamos", "/prestamo/prestamo/socios-atrasados").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/prestamo/prestamo").hasAnyRole("ADMIN" ,"SOCIO")
                                .requestMatchers(HttpMethod.PUT, "/prestamo/actualizar/").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/prestamo/devolver/").hasAnyRole("ADMIN","SOCIO")
                                .requestMatchers(HttpMethod.DELETE, "/prestamo/borrar/").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/ejemplar/listar").permitAll()
                                .requestMatchers(HttpMethod.POST, "/ejemplar/ejemplar").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/ejemplar/borrar/").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated()
                )
                //Crea una sesion sin estados
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); //corre el filtro de jwt antes que el de usuario/contraseña
        return http.build();
    }

    @Bean
   public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
   }
   @Bean
    public CorsConfigurationSource corsConfigurationSource() {
       CorsConfiguration configuration = new CorsConfiguration();
       configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://alexanderliberacion-cmd.github.io"));
       configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH","DELETE"));
       configuration.setAllowedHeaders (List.of("Content-Type","Authorization"));
       UrlBasedCorsConfigurationSource source  = new  UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/**", configuration);
       return source;
   }

}
