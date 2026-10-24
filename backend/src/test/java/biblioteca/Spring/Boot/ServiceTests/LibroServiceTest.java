package biblioteca.Spring.Boot.ServiceTests;

import biblioteca.Spring.Boot.DTO.LibroDTO;
import biblioteca.Spring.Boot.Entities.LibroEntity;
import biblioteca.Spring.Boot.Exceptions.OperacionNoPermitidaException;
import biblioteca.Spring.Boot.Exceptions.RecursoNoEncontradoException;
import biblioteca.Spring.Boot.Exceptions.OperacionNoPermitida;
import biblioteca.Spring.Boot.Mappers.LibroMapper;
import biblioteca.Spring.Boot.Repositories.EjemplarRepository;
import biblioteca.Spring.Boot.Repositories.LibroRepository;
import biblioteca.Spring.Boot.Service.LibroService;
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
public class LibroServiceTest {

    @Mock
    private LibroRepository repository;

    @InjectMocks
    private LibroService service;

    @Mock
    private LibroMapper mapper;

    @Mock
    private EjemplarRepository repositoryEjemplar;

    @Test
    @DisplayName("Debe crear un libro")
    void crearLibro() {
        var libroDto = new LibroDTO("134343434", "prueba", "yo");
        var libroEntity = new LibroEntity();
        when(repository.findById(libroDto.isbn())).thenReturn(Optional.empty());
        when(mapper.dtoALibro(libroDto)).thenReturn(libroEntity);
        when(repository.save(libroEntity)).thenReturn(libroEntity);
        when(mapper.libroADto(libroEntity)).thenReturn(libroDto);
        var resultado = service.crearLibro(libroDto);
        assertNotNull(resultado);
        verify(repository, times(1)).findById(libroDto.isbn());
    }

    @Test
    @DisplayName("Debe lanzar excepcion")
    void crearLibroFallidoPorYaExistir() {
        var libroDto = new LibroDTO("134343434", "prueba", "yo");
        when(repository.findById(libroDto.isbn())).thenThrow(new OperacionNoPermitida("El libro ya existe"));
        OperacionNoPermitida exception = assertThrows(OperacionNoPermitida.class, () -> service.crearLibro(libroDto));
        assertEquals("El libro ya existe", exception.getMessage());
        verify(repository, times(1)).findById(libroDto.isbn());
    }

    @Test
    @DisplayName("Debe listar los libros")
    void listarLibros() {
        var libroDto = new LibroDTO("134343434", "prueba", "yo");
        var libroEntity = new LibroEntity();
        when(repository.findAll()).thenReturn(List.of(libroEntity));
        when(mapper.libroADto(libroEntity)).thenReturn(libroDto);
        var resultado = service.listarLibros();
        assertNotNull(resultado);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe lanzar excepcion al no encontrar ninguno")
    void listarLibrosFallidoPorNinguno() {
        when(repository.findAll()).thenThrow(new RecursoNoEncontradoException("No se encuentra ningun libro"));
        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () -> service.listarLibros());
        assertEquals("No se encuentra ningun libro", exception.getMessage());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe actualizar el libro")
    void actualizarLibro() {
        var libroEntity = new LibroEntity();
        libroEntity.setIsbn("134343434");
        when(repository.findById(libroEntity.getIsbn())).thenReturn(Optional.of(libroEntity));
        var libroNuevo = new LibroDTO("134343434", "yo", "prueba2");
        libroEntity.setAutor(libroNuevo.autor());
        libroEntity.setTitulo(libroNuevo.titulo());
        when(repository.save(libroEntity)).thenReturn(libroEntity);
        when(mapper.libroADto(libroEntity)).thenReturn(libroNuevo);
        service.actualizarLibro(libroEntity.getIsbn(), libroNuevo);
    }

    @Test
    @DisplayName("Debe lanzar excepcion por no encontrar el libro")
    void actualizarLibroFallidoPorNinguno() {
        var libroEntity = new LibroEntity();
        var libroDto2 = new LibroDTO("134343434", "yo", "prueba2");
        when(repository.findById(libroEntity.getIsbn())).thenThrow(new RecursoNoEncontradoException("No se encuentra el libro para actualizar"));
        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class,() -> service.actualizarLibro(libroEntity.getIsbn(), libroDto2));
        assertEquals("No se encuentra el libro para actualizar", exception.getMessage());
        verify(repository, times(1)).findById(libroEntity.getIsbn());
    }

    @Test
    @DisplayName("Debe eliminar el libro")
    void eliminarLibro() {
        String isbn  = "134343434";
        when(repositoryEjemplar.existsByIsbnIsbn(isbn)).thenReturn(false);
        doNothing().when(repository).deleteById(isbn);
        service.eliminarLibro(isbn);
        verify(repository, times(1)).findById(isbn);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al tener ejemplares")
    void eliminarLibroFallidoPorTenerEjemplares() {
        String isbn  = "134343434";
        when(repositoryEjemplar.existsByIsbnIsbn(isbn)).thenReturn(true);
        OperacionNoPermitidaException exception = assertThrows(OperacionNoPermitidaException.class, () -> service.eliminarLibro(isbn));
        assertEquals("El libro tiene ejemplares", exception.getMessage());
        verify(repository, never()).findById(isbn);

    }
}
