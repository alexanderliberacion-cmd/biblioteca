package biblioteca.Spring.Boot.Service;

import biblioteca.Spring.Boot.DTO.EjemplarDTO;
import biblioteca.Spring.Boot.Entities.EjemplarEntity;
import biblioteca.Spring.Boot.Entities.LibroEntity;
import biblioteca.Spring.Boot.Exceptions.RecursoNoEncontradoException;
import biblioteca.Spring.Boot.Repositories.EjemplarRepository;
import biblioteca.Spring.Boot.Repositories.LibroRepository;
import biblioteca.Spring.Boot.Repositories.PrestamoRepository;
import biblioteca.Spring.Boot.mappers.EjemplarMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EjemplarService {
    private final EjemplarRepository ejemplarRepository;
    private final LibroRepository libroRepository;
    private final EjemplarMapper ejemplarMapper;
    private final PrestamoRepository prestamoRepository;

    public EjemplarService(EjemplarRepository ejemplarRepository, LibroRepository libroRepository, EjemplarMapper ejemplarMapper, PrestamoRepository prestamoRepository) {
        this.ejemplarRepository = ejemplarRepository;
        this.libroRepository = libroRepository;
        this.ejemplarMapper = ejemplarMapper;
        this.prestamoRepository = prestamoRepository;
    }

    //Crear ejemplar
    public Optional<EjemplarDTO> crearEjemplar(EjemplarDTO ejemplarDTO) {
        Optional<LibroEntity> libroEncontrado  = libroRepository.findById(ejemplarDTO.isbn()); //Encontramos el libro del ejemplar

        if(!libroEncontrado.isPresent()) {
           return Optional.empty();
        }
        EjemplarEntity nuevoEjemplar = ejemplarMapper.dtoAEjemplar(ejemplarDTO);
        nuevoEjemplar.setIsbn(libroEncontrado.get()); //Conseguimos el isbn del libro encontrado y se lo damos al ejemplar
        nuevoEjemplar.setIdEjemplar(null);
        EjemplarEntity ejemplarGuardado =  ejemplarRepository.save(nuevoEjemplar);
        return Optional.of(ejemplarMapper.ejemplarADto(ejemplarGuardado));
    }

    //Buscar Ejemplar
    public Optional<EjemplarDTO> buscarEjemplar(Integer idEjemplar) {
        return ejemplarRepository.findById(idEjemplar).map(ejemplarMapper::ejemplarADto);
    }

    //Listar Ejemplares
    public List<EjemplarDTO> listarTodos() {
        return ejemplarRepository.findAll()
                .stream()
                .map(ejemplarMapper::ejemplarADto)
                .toList();
    }

    //Actualizar ejemplar
    public Optional<EjemplarDTO> actualizarEjemplar(Integer idEjemplar, EjemplarDTO ejemplarNuevo) {
        EjemplarEntity ejemplarEncontrado =  ejemplarRepository.findById(idEjemplar).orElseThrow(() -> new RecursoNoEncontradoException("Ejemplar no encontrado"));
        LibroEntity libroEncontrado = libroRepository.findById(ejemplarNuevo.isbn()).orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado"));
        ejemplarEncontrado.setIsbn(libroEncontrado);
        ejemplarEncontrado.setEstado(ejemplarNuevo.estado());
        EjemplarEntity ejemplarActualizado = ejemplarRepository.save(ejemplarEncontrado);
        return Optional.of(ejemplarMapper.ejemplarADto(ejemplarActualizado));
    }

    //Borrar Ejemplar
    public void eliminarEjemplar(Integer idEjemplar) {
        if(!prestamoRepository.buscarPrestamoActivoPorEjemplar(idEjemplar).isPresent()) {
            ejemplarRepository.deleteById(idEjemplar);
        }
    }
}