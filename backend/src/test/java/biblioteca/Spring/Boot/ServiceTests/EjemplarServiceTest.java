package biblioteca.Spring.Boot.ServiceTests;


import biblioteca.Spring.Boot.DTO.EjemplarDTO;
import biblioteca.Spring.Boot.Entities.EjemplarEntity;
import biblioteca.Spring.Boot.Entities.LibroEntity;
import biblioteca.Spring.Boot.Exceptions.RecursoNoEncontradoException;
import biblioteca.Spring.Boot.Exceptions.OperacionNoPermitida;
import biblioteca.Spring.Boot.Mappers.EjemplarMapper;
import biblioteca.Spring.Boot.Repositories.EjemplarRepository;
import biblioteca.Spring.Boot.Repositories.LibroRepository;
import biblioteca.Spring.Boot.Repositories.PrestamoRepository;
import biblioteca.Spring.Boot.Service.EjemplarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EjemplarServiceTest {

    @Mock
    private EjemplarRepository repository;

    @InjectMocks
    private EjemplarService service;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private EjemplarMapper ejemplarMapper;

    @Mock
    private PrestamoRepository prestamoRepository;

    @Test
    @DisplayName("Debe crear un ejemplar correctamente")
    void crearEjemplarExitosamente() {
        var libro = new LibroEntity();
        var ejemplarDto = new EjemplarDTO(2, "DISPONIBLE", "3434739473943");
        var ejemplar = new EjemplarEntity();
        when(ejemplarMapper.dtoAEjemplar(ejemplarDto)).thenReturn(ejemplar);
        when(libroRepository.findById(ejemplarDto.isbn())).thenReturn(Optional.of(libro));
        when(repository.save(any(EjemplarEntity.class))).thenReturn(ejemplar);
        var ejemplarDtoResultado = new EjemplarDTO(2, "DISPONIBLE", "3434739473943");
        when(ejemplarMapper.ejemplarADto(any(EjemplarEntity.class))).thenReturn(ejemplarDtoResultado);
        var resultado = service.crearEjemplar(ejemplarDtoResultado);
        assertNotNull(resultado);

        verify(libroRepository, times(1)).findById("3434739473943");
    }

    @Test
    @DisplayName("Debe lanzar una excepcion al no tener libro")
    void crearEjemplarSinLibro() {
        var ejemplarDto = new EjemplarDTO(2, "DISPONIBLE", "3434739473943");
        when(libroRepository.findById(ejemplarDto.isbn())).thenThrow(new RecursoNoEncontradoException("Libro no encontrado"));

        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            service.crearEjemplar(ejemplarDto);
        });
        assertEquals("Libro no encontrado", exception.getMessage());
        verify(libroRepository, times(1)).findById("3434739473943");
    }

    @Test
    @DisplayName("Debe listar los ejemplares")
    void listarEjemplares() {
        var ejemplarDto = new EjemplarDTO(2, "DISPONIBLE", "3434739473943");
        var ejemplar = new EjemplarEntity();
        when(repository.findAll()).thenReturn(List.of(ejemplar));
        when(ejemplarMapper.ejemplarADto(ejemplar)).thenReturn(ejemplarDto);
        var resultado = service.listarTodos();
        assertNotNull(resultado);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe lanzar una excepcion al no encontrar ejemplares")
    void listarEjemplaresFallidoAlNoEncontrarNinguno() {
        when(repository.findAll()).thenThrow(new RecursoNoEncontradoException("No hay ejemplares"));
        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            service.listarTodos();
        });
        assertEquals("No hay ejemplares", exception.getMessage());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deberia eliminar el ejemplar")
    void eliminarEjemplar() {
        Integer idEjemplar = 1;
        when(prestamoRepository.buscarPrestamoActivoPorEjemplar(idEjemplar)).thenReturn(Optional.empty());
         service.eliminarEjemplar(idEjemplar);
         verify(repository, times(1)).deleteById(idEjemplar);
    }

    @Test
    @DisplayName("Deberia eliminar el ejemplar")
    void eliminarEjemplarFallidoPorUsoEnPrestamo() {
        Integer idEjemplar = 1;
        when(prestamoRepository.buscarPrestamoActivoPorEjemplar(idEjemplar)).thenThrow(new OperacionNoPermitida("Prestamo no encontrado"));
        OperacionNoPermitida exception = assertThrows(OperacionNoPermitida.class, () -> {
            service.eliminarEjemplar(idEjemplar);
        });
        assertEquals("Prestamo no encontrado", exception.getMessage());
        verify(repository, never()).deleteById(idEjemplar);
    }
}
