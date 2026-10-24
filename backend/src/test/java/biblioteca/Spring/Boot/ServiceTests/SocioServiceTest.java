package biblioteca.Spring.Boot.ServiceTests;

import biblioteca.Spring.Boot.DTO.SocioDTO;
import biblioteca.Spring.Boot.DTO.UsuarioDTO;
import biblioteca.Spring.Boot.Entities.Rol;
import biblioteca.Spring.Boot.Entities.SocioEntity;
import biblioteca.Spring.Boot.Entities.UsuarioEntity;
import biblioteca.Spring.Boot.Exceptions.OperacionNoPermitida;
import biblioteca.Spring.Boot.Exceptions.OperacionNoPermitidaException;
import biblioteca.Spring.Boot.Exceptions.RecursoNoEncontradoException;
import biblioteca.Spring.Boot.Mappers.SocioMapper;
import biblioteca.Spring.Boot.Mappers.UsuarioMapper;
import biblioteca.Spring.Boot.Repositories.PrestamoRepository;
import biblioteca.Spring.Boot.Repositories.SocioRepository;
import biblioteca.Spring.Boot.Repositories.UsuarioRepository;
import biblioteca.Spring.Boot.Service.SocioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class SocioServiceTest {

    @Mock
    private SocioRepository socioRepository;

    @InjectMocks
    private SocioService socioService;

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SocioMapper socioMapper;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Test
    @DisplayName("Debe crear un socio")
    void crearSocio() {
        var socioEntity = new SocioEntity();
        var socioDto = new SocioDTO(1, "Alexander", "alexanderliberacion@gmail.com");
        when(socioMapper.dtoASocio(socioDto)).thenReturn(socioEntity);
        when(socioRepository.save(socioEntity)).thenReturn(socioEntity);
        when(socioMapper.socioADto(socioEntity)).thenReturn(socioDto);
        var resultado = socioService.crearSocio(socioDto);
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al no tener email")
    void crearSocioInvalido() {
        var socioDto = new SocioDTO(1, "Alexander", "");
        when(socioMapper.dtoASocio(socioDto)).thenThrow(new OperacionNoPermitidaException("No hay email valido"));
        OperacionNoPermitidaException exception = assertThrows(OperacionNoPermitidaException.class, () -> {
            socioService.crearSocio(socioDto);
        });
        assertEquals("No hay email valido", exception.getMessage());
    }

    @Test
    @DisplayName("Debe listar los socios")
    void listarSocios() {
        var socioEntity = new SocioEntity();
        var socioDto = new SocioDTO(1, "Alexander", "alexanderliberacion@gmail.com");
        when(socioRepository.findAll()).thenReturn(List.of(socioEntity));
        when(socioMapper.socioADto(socioEntity)).thenReturn(socioDto);
        var resultado = socioService.listarTodos();
        assertNotNull(resultado);
        verify(socioRepository).findAll();
    }

    @Test
    @DisplayName("Debe lanzar excepcion al no encontrar ninguno")
    void listarSociosFallidoPorNoHaberNinguno() {
        when(socioRepository.findAll()).thenThrow(new RecursoNoEncontradoException("No se ha encontrado dto"));
        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            socioService.listarTodos();
        });
        assertEquals("No se ha encontrado dto", exception.getMessage());
        verify(socioRepository).findAll();
    }

    @Test
    @DisplayName("Debe actualizar al socio")
    @WithMockUser(username = "alexanderliberacion@gmail.com", roles = "ADMIN")
    void actualizarSocio() {
        Integer idSocio = 1;
        var socioNuevo = new SocioDTO(1, "Alexander", "alexanderliberacion@gmail.com");
        var socioAntiguo = new SocioEntity();
        var usuario = new UsuarioEntity();
        usuario.setRol(Rol.ADMIN);
        usuario.setEmail("alexanderliberacion@gmail.com");
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(socioRepository.findById(idSocio)).thenReturn(Optional.of(socioAntiguo));
        socioAntiguo.setNombre(socioNuevo.nombre());
        socioAntiguo.setEmail(socioNuevo.email());
        when(socioRepository.save(socioAntiguo)).thenReturn(socioAntiguo);
        when(socioMapper.socioADto(socioAntiguo)).thenReturn(socioNuevo);
        var resultado = socioService.actualizarSocio(idSocio, socioNuevo);
        assertNotNull(resultado);
        verify(socioRepository).findById(idSocio);
        verify(usuarioRepository).findByEmail(usuario.getEmail());
    }

    @Test
    @DisplayName("Debe lanzar excepcion al no encontrar socio")
    @WithMockUser(username = "alexanderliberacion@gmail.com", roles = "ADMIN")
    void actualizarSocioInvalido() {
        Integer idSocio = 1;
        var socioNuevo = new SocioDTO(0, "Alexander", "alexanderliberacion@gmail.com");
        var usuario = new UsuarioEntity();
        usuario.setRol(Rol.ADMIN);
        usuario.setEmail("alexanderliberacion@gmail.com");
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(socioRepository.findById(idSocio)).thenThrow(new RecursoNoEncontradoException("No se ha encontrado socio"));
        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            socioService.actualizarSocio(idSocio, socioNuevo);
        });
        assertEquals("No se ha encontrado socio", exception.getMessage());
    }

    @Test
    @DisplayName("Debe eliminar al socio")
    void eliminarSocio() {
        Integer idSocio = 1;
        when(prestamoRepository.existsBySocioIdIdSocio(idSocio)).thenReturn(false);
        socioService.eliminarSocio(idSocio);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al no encontrar socio con el id")
    void eliminarSocioPorIdInvalido() {
        Integer idSocio = 0;
        when(prestamoRepository.existsBySocioIdIdSocio(idSocio)).thenThrow(new OperacionNoPermitidaException("No se ha encontrado socio"));
        OperacionNoPermitidaException exception = assertThrows(OperacionNoPermitidaException.class, () -> {
            socioService.eliminarSocio(idSocio);
        });
        assertEquals("No se ha encontrado socio", exception.getMessage());
    }

    @Test
    @DisplayName("Debe listar los usuarios pendientes")
    void listarUsuariosPendientes() {
        var usuario = new UsuarioEntity();
        var socio = new SocioEntity();
        var usuarioDto = new UsuarioDTO(1L, "alexanderliberacion@gmail.com", "1234", Rol.BASIC,socio );
        usuario.setRol(Rol.BASIC);
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(usuarioMapper.usuarioADTO(usuario)).thenReturn(usuarioDto);
        var resultado = socioService.listaUsuariosPendientes();
        assertNotNull(resultado);
        verify(usuarioRepository).findAll();
    }

    @Test
    @DisplayName("Debe lanzar excepcion al no encontrar usuario")
    void listarUsuariosPendientesFallidoPorNoHaber() {
        when(usuarioRepository.findAll()).thenThrow(new RecursoNoEncontradoException("No se ha encontrado usuario"));
        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            socioService.listaUsuariosPendientes();
        });
        assertEquals("No se ha encontrado usuario", exception.getMessage());
    }


}
