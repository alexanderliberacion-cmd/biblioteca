package biblioteca.Spring.Boot.ServiceTests;

import biblioteca.Spring.Boot.DTO.LoginRequestDTO;
import biblioteca.Spring.Boot.DTO.RegisterRequestDTO;
import biblioteca.Spring.Boot.Entities.Rol;
import biblioteca.Spring.Boot.Entities.SocioEntity;
import biblioteca.Spring.Boot.Entities.UsuarioEntity;
import biblioteca.Spring.Boot.Exceptions.OperacionNoPermitida;
import biblioteca.Spring.Boot.Repositories.UsuarioRepository;
import biblioteca.Spring.Boot.Security.JwtUtil;
import biblioteca.Spring.Boot.Service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;


    @Test
    @DisplayName("Deberia de loguearse de forma correcta")
    void loginExitoso() {
        var socio = new SocioEntity();
        var usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1L);
        usuarioEntity.setEmail("alexanderliberacion@gmail.com");
        usuarioEntity.setPassword("1245");
        usuarioEntity.setRol(Rol.BASIC);
        usuarioEntity.setSocio(socio);
        LoginRequestDTO userDTO = new LoginRequestDTO("alexanderliberacion@gmail.com", "1245");
        var token = new UsernamePasswordAuthenticationToken(userDTO.email(), userDTO.password());
        when(authenticationManager.authenticate(token)).thenReturn(new UsernamePasswordAuthenticationToken(userDTO.email(), userDTO.password()));
        when(usuarioRepository.findByEmail(userDTO.email())).thenReturn(Optional.of(usuarioEntity));
        when(jwtUtil.generateToken(usuarioEntity.getEmail())).thenReturn("token");
        var resultado = authService.login(userDTO);
        assertNotNull(resultado);

        verify(authenticationManager, times(1)).authenticate(token);
        verify(usuarioRepository, times(1)).findByEmail(userDTO.email());
    }

    @Test
    @DisplayName("Deberia de lanzar excepcion al no encontrar el email")
    void loginFallidoPorNoEncontrarEmail() {
        LoginRequestDTO userDTO = new LoginRequestDTO("", "1245");
        var token = new UsernamePasswordAuthenticationToken(userDTO.email(), userDTO.password());
        when(authenticationManager.authenticate(token)).thenReturn(new UsernamePasswordAuthenticationToken(userDTO.email(), userDTO.password()));
        when(usuarioRepository.findByEmail(userDTO.email())).thenReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authService.login(userDTO);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(userDTO.email());
    }

    @Test
    @DisplayName("Deberia de registrarse correctamente")
    void registroExitoso() {
        var userDTO = new RegisterRequestDTO("alexanderliberacion@hotmail.com", "1245");
        var usuarioEntity = new UsuarioEntity();
        usuarioEntity.setEmail("alexanderliberacion@hotmail.com");
        usuarioEntity.setPassword(passwordEncoder.encode(userDTO.password()));
        usuarioEntity.setRol(Rol.BASIC);
        when(usuarioRepository.findByEmail(userDTO.email())).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuarioEntity)).thenReturn(usuarioEntity);
        when(jwtUtil.generateToken(usuarioEntity.getEmail())).thenReturn("token");
        var resultado = authService.register(userDTO);
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).findByEmail(userDTO.email());

    }

    @Test
    @DisplayName("Deberia de saltar que el email ya existe")
    void registroFallidoPorEmailExistente() {
        var userDTO = new RegisterRequestDTO("alexanderliberacion@hotmail.com", "1245");
        var usuarioEntity = new UsuarioEntity();
        usuarioEntity.setEmail("alexanderliberacion@hotmail.com");
        usuarioEntity.setPassword(passwordEncoder.encode(userDTO.password()));
        usuarioEntity.setRol(Rol.BASIC);
        when(usuarioRepository.findByEmail(userDTO.email())).thenReturn(Optional.of(usuarioEntity));
        OperacionNoPermitida exception = assertThrows(OperacionNoPermitida.class, () -> {
            authService.register(userDTO);
        });
        assertEquals("Email ya usado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(userDTO.email());
    }


}
