package biblioteca.Spring.Boot.Service;

import biblioteca.Spring.Boot.DTO.LibroDTO;
import biblioteca.Spring.Boot.Entities.LibroEntity;
import biblioteca.Spring.Boot.Exceptions.OperacionNoPermitidaException;
import biblioteca.Spring.Boot.Exceptions.RecursoNoEncontradoException;
import biblioteca.Spring.Boot.Repositories.EjemplarRepository;
import biblioteca.Spring.Boot.Repositories.LibroRepository;
import biblioteca.Spring.Boot.mappers.LibroMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class LibroService {
    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper; //Mapeador
    private final EjemplarRepository ejemplarRepository;

    public LibroService(LibroRepository libroRepository, LibroMapper libroMapper, EjemplarRepository ejemplarRepository) {
        this.libroRepository = libroRepository;
        this.libroMapper = libroMapper;
        this.ejemplarRepository = ejemplarRepository;
    }

    //Crear libro
    public Optional<LibroDTO> crearLibro(LibroDTO libroDTO) {
        if (libroRepository.findById(libroDTO.isbn()).isPresent()) {
            return Optional.empty();
        }
        LibroEntity libroEntity = libroMapper.dtoALibro(libroDTO);
        libroEntity = libroRepository.save(libroEntity);
        return Optional.of(libroMapper.libroADto(libroEntity));
    }

    //Buscar libro
    public Optional<LibroDTO> buscarLibro(String isbn) {
        return libroRepository.findById(isbn)
                .map(libroMapper::libroADto);
    }

    //Listar libros
    public List<LibroDTO> listarLibros() {
        return libroRepository.findAll()
                .stream()
                .map(libroMapper::libroADto)
                .toList();
    }

    //Actualizar libro
    public Optional<LibroDTO> actualizarLibro(String isbn, LibroDTO libroNuevo) {
        LibroEntity libroNoEncontrado = libroRepository.findById(isbn).orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado"));
        libroNoEncontrado.setAutor(libroNuevo.autor());
        libroNoEncontrado.setTitulo(libroNuevo.titulo());
        LibroEntity libroActualizado = libroRepository.save(libroNoEncontrado);
        return Optional.of(libroMapper.libroADto(libroActualizado));
    }

    //Borrar Libro
    public void eliminarLibro(String isbn) {
       boolean tieneEjemplares = ejemplarRepository.existsByIsbnIsbn(isbn);
       if (!tieneEjemplares) {
           libroRepository.deleteById(isbn);
       } else  {
           throw new OperacionNoPermitidaException("El libro tiene ejemplares");
       }
    }
}