package biblioteca.Spring.Boot.ServiceTests;

import biblioteca.Spring.Boot.DTO.PrestamoDTO;
import biblioteca.Spring.Boot.Entities.EjemplarEntity;
import biblioteca.Spring.Boot.Entities.PrestamoEntity;
import biblioteca.Spring.Boot.Entities.SocioEntity;
import biblioteca.Spring.Boot.Exceptions.OperacionNoPermitidaException;
import biblioteca.Spring.Boot.Exceptions.RecursoNoEncontradoException;
import biblioteca.Spring.Boot.Mappers.PrestamoMapper;
import biblioteca.Spring.Boot.Mappers.SocioMapper;
import biblioteca.Spring.Boot.Repositories.EjemplarRepository;
import biblioteca.Spring.Boot.Repositories.PrestamoRepository;
import biblioteca.Spring.Boot.Repositories.SocioRepository;
import biblioteca.Spring.Boot.Service.PrestamoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrestamoServiceTest {

    @InjectMocks
    private PrestamoService prestamoService;

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private SocioRepository socioRepository;

    @Mock
    private EjemplarRepository ejemplarRepository;

    @Mock
    private PrestamoMapper prestamoMapper;

    @Mock
    private SocioMapper socioMapper;

    @Test
    @DisplayName("Debe crear un prestamo")
    void crearPrestamo() {
        Integer idSocio = 1;
        Integer idEjemplar = 1;
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.now().plusDays(15);
        LocalDate fechaDevolucion = LocalDate.now().plusDays(5);

        var socio = new SocioEntity();
        when(socioRepository.findById(idSocio)).thenReturn(Optional.of(socio));
        var ejemplar = new  EjemplarEntity();
        when(ejemplarRepository.findById(idEjemplar)).thenReturn(Optional.of(ejemplar));
        ejemplar.setEstado("PRESTADO");

        var resultado = prestamoService.crearPrestamo(idSocio, idEjemplar);
        assertNotNull(resultado);
        assertEquals("PRESTADO", ejemplar.getEstado());
    }

    @Test
    @DisplayName("Debe lanzar excepcion por no tener idSocio valido")
    void crearPrestamoFallidoPorIdSocioInvalido() {
        Integer idSocio = 0;
        when(socioRepository.findById(idSocio)).thenThrow(new OperacionNoPermitidaException("El idSocio no es valido"));
        OperacionNoPermitidaException exception = assertThrows(OperacionNoPermitidaException.class, () -> {
            prestamoService.crearPrestamo(idSocio, idSocio);
        });
        assertEquals("El idSocio no es valido", exception.getMessage());
    }

    @Test
    @DisplayName("Debe listar los prestamos")
    void listarPrestamos() {
        Integer idSocio = 1;
        Integer idEjemplar = 1;
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.now().plusDays(15);
        LocalDate fechaDevolucion = LocalDate.now().plusDays(5);
        var prestamoDTO = new PrestamoDTO(fechaInicio, fechaLimite, fechaDevolucion, idSocio, idEjemplar);
        var prestamoEntity = new PrestamoEntity();
        when(prestamoRepository.findAll()).thenReturn(List.of(prestamoEntity));
        when(prestamoMapper.prestamoADto(prestamoEntity)).thenReturn(prestamoDTO);
        var resultado = prestamoService.listarPrestamos();
        assertNotNull(resultado);
        verify(prestamoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe lanzar excepcion al no encontrar prestamos")
    void listarPrestamosFallidoPorNoEncontrar() {
        when(prestamoRepository.findAll()).thenThrow(new RecursoNoEncontradoException("No se encuentran prestamos"));
        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            prestamoService.listarPrestamos();
        });
        assertEquals("No se encuentran prestamos", exception.getMessage());
        verify(prestamoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe actualizar el prestamo")
    void actualizarPrestamo() {
        Integer id = 1;
        var prestamoEntity = new PrestamoEntity();
        Integer idSocio = 1;
        Integer idEjemplar = 1;
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.now().plusDays(15);
        LocalDate fechaDevolucion = LocalDate.now().plusDays(5);
        var prestamoDTO = new PrestamoDTO(fechaInicio, fechaLimite, fechaDevolucion, idSocio, idEjemplar);

        prestamoEntity.setFechaLimite(LocalDate.now().plusDays(15));
        when(prestamoRepository.findById(id)).thenReturn(Optional.of(prestamoEntity));
        when(prestamoRepository.save(any(PrestamoEntity.class))).thenReturn(prestamoEntity);
        when(prestamoMapper.prestamoADto(prestamoEntity)).thenReturn(prestamoDTO);
        var resultado = prestamoService.actualizarPrestamo(id);
        assertNotNull(resultado);
        verify(prestamoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debe lanzar una excepcion por id incorrecto")
    void actualizarPrestamoFallidoPorIdIncorrecto() {
        Integer id = 0;
        when(prestamoRepository.findById(id)).thenThrow(new OperacionNoPermitidaException("El id del prestamo no es valido"));
        OperacionNoPermitidaException exception = assertThrows(OperacionNoPermitidaException.class, () -> {
            prestamoService.actualizarPrestamo(id);
        });
        assertEquals("El id del prestamo no es valido", exception.getMessage());
        verify(prestamoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debe borrar el prestamo")
    void borrarPrestamo() {
        Integer id = 1;
        var prestamoEntity = new PrestamoEntity();
        prestamoEntity.setFechaDevolucion(LocalDate.now());
        when(prestamoRepository.findById(id)).thenReturn(Optional.of(prestamoEntity));
        prestamoService.borrarPrestamo(id);
        verify(prestamoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debe dar excepcion por estar activo")
    void borrarPrestamoFallidoPorEstado() {
        Integer id = 1;
        when(prestamoRepository.findById(id)).thenThrow(new OperacionNoPermitidaException("El prestamo esta activo, no se puede borrar"));
        OperacionNoPermitidaException exception = assertThrows(OperacionNoPermitidaException.class, () -> {
            prestamoService.borrarPrestamo(id);
        });
        assertEquals("El prestamo esta activo, no se puede borrar", exception.getMessage());
        verify(prestamoRepository, times(1)).findById(id);
    }

}
