package biblioteca.Spring.Boot.Service;

import biblioteca.Spring.Boot.DTO.LoginRequestDTO;
import biblioteca.Spring.Boot.DTO.LoginResponseDTO;
import biblioteca.Spring.Boot.DTO.RegisterRequestDTO;
import biblioteca.Spring.Boot.Entities.Rol;
import biblioteca.Spring.Boot.Entities.UsuarioEntity;
import biblioteca.Spring.Boot.Exceptions.RecursoYaExistente;
import biblioteca.Spring.Boot.Repositories.UsuarioRepository;
import biblioteca.Spring.Boot.Security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(@RequestBody LoginRequestDTO userDTO) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDTO.email(), userDTO.password()); //Crea el token con el email y password
        authenticationManager.authenticate(token); //Lo autentica
        UsuarioEntity usuario = usuarioRepository.findByEmail(userDTO.email()).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado")); //Busca a el usuario
        String tokenGenerado = jwtUtil.generateToken(usuario.getEmail()); //Genera un token para el usuario
        return new LoginResponseDTO(usuario.getEmail(), usuario.getRol().name(), tokenGenerado); //Recibe la respuesta
    }

    public LoginResponseDTO register(@RequestBody RegisterRequestDTO userDTO) {
        boolean emailExiste = usuarioRepository.findByEmail(userDTO.email()).isPresent();
        if (emailExiste) {
            throw new RecursoYaExistente("Email ya usado");
        }
        UsuarioEntity usuario = new UsuarioEntity(); //Crea el usuario y abajo se le da el email, contraseña hasheada y el rol
        usuario.setEmail(userDTO.email());
        usuario.setPassword(passwordEncoder.encode(userDTO.password()));
        usuario.setRol(Rol.BASIC);
        usuarioRepository.save(usuario); //Guarda el usuario en el repositorio
        String tokenGenerado = jwtUtil.generateToken(usuario.getEmail()); //genera el token
        return new LoginResponseDTO(usuario.getEmail(), usuario.getRol().name(), tokenGenerado);
    }
}
